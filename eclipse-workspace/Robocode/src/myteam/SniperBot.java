package myteam;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class SniperBot extends TeamRobot {

	private Point2D.Double targetLocation = null;

	@Override
	public void run() {
		setColors(Color.pink, Color.white, Color.red);

		while (true) {
			if (targetLocation != null) {
				moveToTarget(targetLocation);
				targetLocation = null;
			} else {
				patrol();
			}

			setTurnGunRight(360); // Constant scanning
			execute();
		}
	}

	@Override
	public void onMessageReceived(MessageEvent event) {
		if (event.getMessage() instanceof Point2D.Double) {
			targetLocation = (Point2D.Double) event.getMessage();
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		if (isTeammate(event.getName())) {
			return; // Ignore teammates
		}

		double absoluteBearing = getHeadingRadians() + event.getBearingRadians();

		// Predictive targeting
		double bulletPower = Math.min(3.0, getEnergy());
		double bulletSpeed = 20 - 3 * bulletPower;
		long time = (long) (event.getDistance() / bulletSpeed);
		double futureX = getX() + event.getVelocity() * time * Math.sin(event.getHeadingRadians());
		double futureY = getY() + event.getVelocity() * time * Math.cos(event.getHeadingRadians());
		double futureBearing = Utils.normalAbsoluteAngle(Math.atan2(futureX - getX(), futureY - getY()));

		double gunTurn = Utils.normalRelativeAngle(futureBearing - getGunHeadingRadians());
		setTurnGunRightRadians(gunTurn);
		execute();

		// ✅ Tir uniquement si le canon est bien orienté
		if (Math.abs(getGunTurnRemaining()) < Math.toRadians(5)) {
			fire(bulletPower);
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		setBack(50);
		setTurnRight(45);
		execute();
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		setBack(20);
		setTurnRight(45);
		execute();
	}

	private void moveToTarget(Point2D.Double target) {
		double dx = target.getX() - getX();
		double dy = target.getY() - getY();
		double angleToTarget = Utils.normalAbsoluteAngle(Math.atan2(dx, dy));

		setTurnRightRadians(Utils.normalRelativeAngle(angleToTarget - getHeadingRadians()));
		setAhead(Math.hypot(dx, dy));
		execute();
	}

	private void patrol() {
		setAhead(100);
		setTurnRight(90);
		execute();
	}
}
