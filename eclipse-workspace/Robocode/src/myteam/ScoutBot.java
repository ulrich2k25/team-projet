package myteam;

import java.awt.Color;
import java.io.IOException;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class ScoutBot extends TeamRobot {

	public void run() {
		setBodyColor(Color.BLUE);
		setGunColor(Color.WHITE);
		setRadarColor(Color.CYAN);

		while (true) {
			setTurnRadarRight(360); // Scan en continu
			ahead(200); // Avance pour explorer
			turnRight(90); // Tourne pour changer de direction
			avoidWalls(); // Évite les murs
		}
	}

	private void avoidWalls() {
		if (getX() <= 50 || getY() <= 50 || getX() >= getBattleFieldWidth() - 50
				|| getY() >= getBattleFieldHeight() - 50) {
			turnRight(90); // Si trop proche d’un mur, tourne
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (!isTeammate(e.getName())) {
			double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(getHeading() + e.getBearing()));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(getHeading() + e.getBearing()));

			try {
				sendMessage("SniperBot", new double[] { enemyX, enemyY });
				out.println("Enemy at (" + enemyX + ", " + enemyY + ") sent to SniperBot");
			} catch (IOException ex) {
				out.println("Error sending message: " + ex);
			}
		}
	}
}
