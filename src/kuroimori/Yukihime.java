package kuroimori;

import java.awt.Graphics2D;
import java.util.Random;
import static java.lang.Math.*;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.HitByBulletEvent;
import robocode.util.Utils;

import static robocode.util.Utils.*;

public class Yukihime extends AdvancedRobot {

	/* TODO: enemypos -> relativeEnemeyPos */
	private double[] enemypos;

	private double[] prevEnemyPos = {-1, -1};
	//Stair Steps
	private final int steps = 2;

	private double[] enemyTarget;

	/**
	 * Setup every variable for a map overview
	 */
	public void setup()
	{
		//Setup Everything
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		//Scan Enviorment (360°)
		//turnRadarRight(360);

		//turn to north
		turnRight(normalRelativeAngleDegrees(0 - getHeading()));
		//Execute everything
		execute();
	}

	/**
	 * Get Relative Position (Vector) of an Enemy by (sin a, cos a) * |w|
	 * 
	 * @param enemy
	 * @return coordiantes of enemy
	 */
	public double[] getRelativePosition(ScannedRobotEvent enemy)
	{
		double distance = enemy.getDistance();
		double bearing =  enemy.getBearingRadians();

		double vectorXEnemy = sin(bearing + getHeadingRadians()) * distance;
		double vectorYEnemy = cos(bearing + getHeadingRadians()) * distance;

		double[] coordinates = {vectorXEnemy, vectorYEnemy};

		return coordinates;
	}
	/**
	 * Get Absolute Position by Relative
	 * 
	 * @param enemy
	 * @return coordinates
	 */
	public double[] getAbsolutePosition(ScannedRobotEvent enemy)
	{
		double[] coordinates = getRelativePosition(enemy);
		coordinates[0] = coordinates[0] + getX();
		coordinates[1] = coordinates[1] + getY();

		return coordinates;
	}

	/**
	 * True if scanned enemy is moving
	 * Attention! scanning is done outside!
	 * 
	 * @param enemy - Scanned enemy
	 * @return state
	 */
	public boolean isMoving(double[] enemyPos)
	{
		/* Convert to long for better comparing */
		long prevX = round(prevEnemyPos[0]);
		long prevY = round(prevEnemyPos[1]);
		long enemX = round(enemyPos[0]);
		long enemY = round(enemyPos[1]);

		out.println(enemX);

		if (prevX < 0 || prevY < 0) {

			prevEnemyPos = enemyPos;
			return false;

		} else if ( prevX != enemX || prevY != enemY ) {

			prevEnemyPos = enemyPos;
			return true;

		} else if (prevX == enemX && prevY == enemY) {

			return false;
		} else {

			out.println("Error in isMoving");
			return false;
		}
	}

	/**
	 * Move in a "stairy" way...
	 * 
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
			turnRight(90);
			ahead(coordinates[0]/steps);
			turnLeft(90);
		}
	}
	
	/**
	 * Allways tries to locate an enemy on the Map
	 * @param e
	 */
	public void turnRadarToEnemy(ScannedRobotEvent e)
	{
		double currradar = getRadarHeadingRadians(); 
		setTurnRadarRightRadians(0);
	}
	
	
	/**
	 * Finds an Enemy and tries to shoot at hime while he is moving
	 * @param e Enemy Scanned Obj
	 * @return Enemy Shooting Position
	 */
	public double[] shootEnemy(ScannedRobotEvent e)
	{
		double bulletStrength = 1;
		double bulletVelocity = 20 - 3 * bulletStrength; 
		double enemydis = e.getDistance();

		
		double enemyVelocity = e.getVelocity();
		double enemyHeading = e.getHeadingRadians();
		
		double velocity_x = enemyVelocity*sin(enemyHeading);
		double velocity_y = enemyVelocity*cos(enemyHeading);
		
		double timeConsume = enemydis/bulletVelocity;
		
		double[] enemyPos = getAbsolutePosition(e);
		
		double newenemyx = enemyPos[0]+(timeConsume*velocity_x);
		double newenemyy = enemyPos[1]+(timeConsume*velocity_y);
		
		if(newenemyx > getBattleFieldWidth())
		{
			newenemyx = getBattleFieldWidth();
		}
		else if(newenemyx < 0)
		{
			newenemyx = 0;
		}
		
		if(newenemyy > getBattleFieldHeight())
		{
			newenemyy = getBattleFieldHeight();
		}
		else if(newenemyy < 0)
		{
			newenemyy = 0;
		}
		
		//Turn myself by -> Change to turn gun to [tan((getX() - newenemyx) / (getY() - newenemyy))]
		double turn = normalRelativeAngle(atan2((getX() - newenemyx), (getY() - newenemyy)) - getGunHeadingRadians() - PI);
		out.println("TurnBy: " + turn);
		turnGunRightRadians(turn);
		execute();
		fire(bulletStrength);
		execute();
		
		double[] newpos = {newenemyx, newenemyy};  

		//setTurnRadarRight(360);
		return newpos;
	}
	
	public void run()
	{
		//FIXME: Change Back to normal
		setup();
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
		angleMoveTo(enemypos);
		while(true)
		{
			scan();
			//Scan Enviorment (360°)
			//setTurnRadarRight(360);
			//FIXME: Set Radar on Opponent
			execute();
			angleMoveTo(enemypos);
		}
	}

	/**
	 * @brief Move to Enemy
	 */
	public void onScannedRobot(ScannedRobotEvent e)
	{
		double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
		 
		setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
		
		enemypos = getRelativePosition(e);
		
		//out.println("Eneymy is moving: " + isMoving(getAbsolutePosition(e)));
		if(getEnergy() > 50)
		{
			//enemyTarget = shootEnemy(e);
		}
	}

	public void onHitByBullet(HitByBulletEvent event)
	{

		Random rand = new Random();
		enemypos[0] = (double)rand.nextInt((int)getBattleFieldHeight());
		enemypos[1] = (double)rand.nextInt((int)getBattleFieldWidth());

	}
	 public void onPaint(Graphics2D g) {
		 g.drawRect((int)enemyTarget[0]-20, (int)enemyTarget[1]-20, 40, 40);
	 }

}
