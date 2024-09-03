import java.util.LinkedList;

import com.cyberbotics.webots.controller.DifferentialWheels;

public class EpuckRobot extends DifferentialWheels {

	// /
	// Axle lenght - 52mm
	// wheel radius = 20,5 mm
	// parametry do skrętu ustawione recznie :
	// setWheelsRotPS(2562.565/wheelRadius, -2562.565/wheelRadius);
	// parametry do skrętu z obliczen: setWheelsRotPS(2340/wheelRadius,
	// -2340/wheelRadius);

	private static final double SPEED_TIMES_FACTOR = 1;
	private static final int TIME_STEP = (int)(1000 / SPEED_TIMES_FACTOR); // ms
	private static final double PI = Math.PI;
	private IRSensorsService sensorsService = new IRSensorsService();
	private EpuckController epuckController = new EpuckController(true ,true);

	private double wheelRadius = 20.5;

	private double wheelTurnSpeed = 256.565 / wheelRadius; // speed which lets
															// epuck rotate 90
															// degrees through
															// one sec.
	private double wheelGoForwardSpeed = (7 * 10 * wheelTurnSpeed / (13 * PI))/3; // 7/3 cm

	private int updateCounter;

	private LinkedList<Integer> movesList = new LinkedList<Integer>();

	private boolean autogenMoves;

	public static void main(String[] args)
	{
		EpuckRobot robot = new EpuckRobot();
		robot.run();
	}

	public EpuckRobot() {
		super();
		initVariables();
	}

	private void initVariables()
	{
		updateCounter = 0;
		autogenMoves = false;
	}

	public void run()
	{
		while (step(TIME_STEP) != -1)
		{
			onUpdate();
		}
	}

	private void onUpdate()
	{
		incrementUpdateCounter();
		resolveEpuckControllerLogic();
		resolveMovesFromList();
	}

	private void resolveEpuckControllerLogic()
	{
		sensorsService.calculateReadsArray();
		epuckController.setMapPZ();
	}

	private void resolveMovesFromList()
	{
		if (autogenMoves)
		{
			movesList.add((int) (Math.random() * 3) - 1);
			movesList.add(2);
		}
		else{
			int nextCommand = epuckController.getNextCommand(25,25,sensorsService.getLeft()==1,sensorsService.getFront()==1,sensorsService.getRight()==1); //dzika operacja
			System.out.println("command got: "+nextCommand);
			movesList.add(nextCommand);
		}
		
		if (!movesList.isEmpty())
		{
			int move = movesList.getFirst();

			switch (move) {
			case -1:
				turnLeft();
				break;
			case 0:
				goForward();
				break;
			case 1:
				turnRight();
				break;
			default:
				stop();
				break;
			}

			movesList.removeFirst();
		} else
		{
			stop();
		}
	}

	private void goForward()
	{
		setWheelsRotPS(wheelGoForwardSpeed, wheelGoForwardSpeed);
	}

	private void turnRight()
	{
		setWheelsRotPS(wheelTurnSpeed, -wheelTurnSpeed);
	}

	private void turnLeft()
	{
		setWheelsRotPS(-wheelTurnSpeed, wheelTurnSpeed);
	}

	private void stop()
	{
		setWheelsRotPS(0, 0);
	}

	private void setWheelsRotPS(double leftDegPS, double rightDegPS)
	{
		double leftRad = leftDegPS * PI / 180;
		double rightRad = rightDegPS * PI / 180;

//		setSpeed(SPEED_TIMES_FACTOR * leftRad * 1 / getSpeedUnit(),
//				SPEED_TIMES_FACTOR * rightRad * 1 / getSpeedUnit());
		setSpeed(leftRad,rightRad);
	}

	private void incrementUpdateCounter()
	{
		if (!movesList.isEmpty())
		{
			System.out.println();
			System.out.println("Update No." + updateCounter + " move: "
					+ movesList.getFirst());
		} else
		{
			System.out.println();
			System.out.println("Update No." + updateCounter);
		}
		updateCounter++;
	}

}
