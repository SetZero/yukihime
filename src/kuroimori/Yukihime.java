package kuroimori;

import java.util.Random;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.*;

public class Yukihime extends AdvancedRobot {

	private double[] enemypos;
	//Stair Steps
	private final int steps = 2;
	
	//0 = Standby
	//1 = Enemy
	//2 = Mappoint
	private int target = 1;
	/**
	 * @brief Setup every variable for a map overview
	 */
	public void setup()
	{
		//Setup Everything
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
		//Scan Enviorment (360°)
		turnRadarRight(360);

		//turn to north
		turnRight(normalRelativeAngleDegrees(0 - getHeading()));
		//Execute everything
		execute();
	}
	
	/**
	 * @brief Get Relative Position (Vector) of an Enemy by (sin a, cos a) * |w|
	 * @param enemy
	 * @return coordiantes of enemy
	 */
	public double[] getRelativePosition(ScannedRobotEvent enemy)
	{		
		double distance = enemy.getDistance();
		double bearing =  enemy.getBearingRadians();
		
		double vectorXEnemy = Math.sin(bearing + getHeadingRadians()) * distance;
		double vectorYEnemy = Math.cos(bearing + getHeadingRadians()) * distance;
		
		double[] coordinates = {vectorXEnemy, vectorYEnemy};
		
		return coordinates;
	}
	/**
	 * @brief Get Absolute Position by Relative
	 * @param enemy
	 * @return coordinates
	 */
	public double[] getAbsolutePosition(ScannedRobotEvent enemy)
	{
		double[] coordinates = getRelativePosition(enemy);
		coordinates[0] = coordinates[0] - getX();
		coordinates[1] = coordinates[1] - getY();
		return coordinates;
	}
	
	/**
	 * @brief Move in a "stairy" way...
	 * @param coordinates
	 */
	public void angleMoveTo(double[] coordinates)
	{
		//TODO: Check if nessecary
		//Turn to North
		//turnRight(normalRelativeAngleDegrees(0 - getHeading()));
		
		
		for(int i=0;i<steps;i++)
		{
			ahead(coordinates[1]/steps);
			setTurnRight(90);
			ahead(coordinates[0]/steps);
			setTurnLeft(90);
		}
	}
	
	public void shootEnemy()
	{
		
	}
	
	public void run() 
	{
		setup();	
		angleMoveTo(enemypos);
		while(true)
		{
			//Scan Enviorment (360°)
			setTurnRadarRight(360);
			execute();
			angleMoveTo(enemypos);
		}
	}
	
	/**
	 * @brief Move to Enemy
	 */
	public void onScannedRobot(ScannedRobotEvent e)
	{
		enemypos = getRelativePosition(e);
		out.println(enemypos[0] + ", " + enemypos[1]);
	}
	
	public void onHitByBullet(HitByBulletEvent event) 
	{
		target = 2;
		
		Random rand = new java.util.Random();
		enemypos[0] = (double)rand.nextInt((int)getBattleFieldHeight());
		enemypos[1] = (double)rand.nextInt((int)getBattleFieldWidth());
		
	}
	
}
