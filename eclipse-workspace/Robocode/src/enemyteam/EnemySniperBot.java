package enemyteam;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class EnemySniperBot extends TeamRobot {

	@Override
	public void run() {
		setColors(Color.red, Color.black, Color.orange);
		setAdjustGunForRobotTurn(true);

		while (true) {
			ahead(150); // Avance agressivement
			turnGunRight(360); // Scanne pendant le mouvement
			turnRight(90); // Tourne pour changer de direction
			execute();
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		if (isTeammate(event.getName())) {
			return; // Ne pas tirer sur un allié
		}

		fire(2.5); // Tir agressif sur les ennemis
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		back(50); // Reculer pour esquiver
		turnRight(60); // Changement de direction
		execute();
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		back(30); // Reculer du mur
		turnLeft(90); // Tourner pour repartir
		execute();
	}
}
