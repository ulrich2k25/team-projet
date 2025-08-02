package myteam;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class ScoutBot extends TeamRobot {

	@Override
	public void run() {
		setColors(Color.blue, Color.white, Color.red);

		while (true) {
			setTurnGunRight(360); // Scan for enemies
			execute();
			ahead(100);
			execute();
			setTurnRight(45);
			execute();
			ahead(100);
			execute();
			setTurnLeft(45);
			execute();
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		if (isTeammate(event.getName())) {
			return; // Ignore teammates
		}

		double absoluteBearing = getHeadingRadians() + event.getBearingRadians();
		double enemyX = getX() + event.getDistance() * Math.sin(absoluteBearing);
		double enemyY = getY() + event.getDistance() * Math.cos(absoluteBearing);
		Point2D.Double enemyPosition = new Point2D.Double(enemyX, enemyY);

		// Predictive targeting
		double bulletPower = Math.min(3.0, getEnergy());
		double bulletSpeed = 20 - 3 * bulletPower;

		long time = (long) (event.getDistance() / bulletSpeed);
		double futureX = enemyX + event.getVelocity() * time * Math.sin(event.getHeadingRadians());
		double futureY = enemyY + event.getVelocity() * time * Math.cos(event.getHeadingRadians());
		double futureBearing = Utils.normalAbsoluteAngle(Math.atan2(futureX - getX(), futureY - getY()));

		double gunTurn = Utils.normalRelativeAngle(futureBearing - getGunHeadingRadians());
		setTurnGunRightRadians(gunTurn);
		execute();

		// ✅ Tire seulement si le canon est bien aligné
		if (Math.abs(getGunTurnRemaining()) < Math.toRadians(5)) {
			fire(bulletPower);
		}

		// Send enemy position to teammate
		try {
			broadcastMessage(enemyPosition);
		} catch (IOException e) {
			out.println("Unable to send position: " + e.getMessage());
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		setBack(50); // Move back when hit
		setTurnRight(45); // Change direction to avoid further hits
		execute();
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		setBack(20); // Move away from wall
		setTurnRight(45); // Change direction
		execute();
	}
}
