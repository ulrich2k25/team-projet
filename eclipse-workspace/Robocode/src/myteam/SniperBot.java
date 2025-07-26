package myteam;

import java.awt.Color;

import robocode.MessageEvent;
import robocode.TeamRobot;

public class SniperBot extends TeamRobot {

	public void run() {
		setBodyColor(Color.RED);
		setGunColor(Color.BLACK);
		setRadarColor(Color.YELLOW);

		goToCenter();

		while (true) {
			turnRadarRight(360); // Cherche les ennemis
			dodge(); // Bouge un peu pour éviter d’être touché
		}
	}

	private void goToCenter() {
		double centerX = getBattleFieldWidth() / 2;
		double centerY = getBattleFieldHeight() / 2;
		goTo(centerX, centerY);
	}

	private void goTo(double x, double y) {
		double dx = x - getX();
		double dy = y - getY();
		double angle = Math.toDegrees(Math.atan2(dx, dy)) - getHeading();
		turnRight(normalizeBearing(angle));
		ahead(Math.hypot(dx, dy));
	}

	private double normalizeBearing(double angle) {
		while (angle > 180)
			angle -= 360;
		while (angle < -180)
			angle += 360;
		return angle;
	}

	private void dodge() {
		setTurnRight(10);
		ahead(50);
		back(50);
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof double[]) {
			double[] coords = (double[]) e.getMessage();
			double enemyX = coords[0];
			double enemyY = coords[1];

			double dx = enemyX - getX();
			double dy = enemyY - getY();
			double angleToEnemy = Math.toDegrees(Math.atan2(dx, dy));

			double gunTurn = normalizeBearing(angleToEnemy - getGunHeading());
			turnGunRight(gunTurn);
			fire(2); // Tirs précis
			out.println("Firing at (" + enemyX + ", " + enemyY + ")");
		}
	}
}
