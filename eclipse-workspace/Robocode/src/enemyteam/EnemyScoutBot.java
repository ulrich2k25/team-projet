package enemyteam;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class EnemyScoutBot extends TeamRobot {

	@Override
	public void run() {
		setColors(Color.red, Color.black, Color.orange);
		setAdjustGunForRobotTurn(true);

		while (true) {
			ahead(150);
			turnGunRight(360); // scanner pendant qu'on avance
			turnRight(90);
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		if (!isTeammate(event.getName())) {
			fire(2.5); // tir agressif
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		setBack(50);
		turnRight(60);
	}

	@Override
	public void onHitWall(HitWallEvent event) {
		setBack(30);
		turnLeft(90);
	}
}