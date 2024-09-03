public class EpuckController {

	private static final int numOfMoves = 200;
	private static final int inf = 100000;

	private boolean[] rotate = new boolean[numOfMoves];
	private boolean[] clockwise = new boolean[numOfMoves];	//

	boolean[] vertical = new boolean[numOfMoves];
	boolean[] direction = new boolean[numOfMoves];
	int[] x = new int[numOfMoves];
	int[] y = new int[numOfMoves];
	int[] aim = new int[numOfMoves];

	public boolean isFirstMove = true;
	private int i = 0; // iteration

	public Map epuckMap;
	public static int epuckStartX = 4;
	public static int epuckStartY = 4;
	public static int mapSize = 20; // mapSize

	public EpuckController() {
		vertical[0] = true;
		direction[0] = true;
		x[0] = epuckStartX;
		y[0] = epuckStartY;
		epuckMap = new Map(epuckStartX, epuckStartY, mapSize);
	}

	public EpuckController(boolean epuckVertical, boolean epuckDirection) {
		vertical[0] = epuckVertical;
		direction[0] = epuckDirection;
		x[0] = epuckStartX;
		y[0] = epuckStartY;
		epuckMap = new Map(epuckStartX, epuckStartY, mapSize);
	}

	public int getNextCommand(int targetX, int targetY, boolean left_sensor, boolean front_sensor, boolean right_sensor)
	{
		int command = 3;

		generateNextMove(x[i], y[i], vertical[i], direction[i], targetX, targetY, left_sensor, front_sensor, right_sensor);

		if (rotate[i] && clockwise[i])
		{
			command = 1;
		}
		if (rotate[i] && !clockwise[i])
		{
			command = -1;
		}
		if (!rotate[i] && clockwise[i])
		{
			command = 0;
		}
		if (!rotate[i] && !clockwise[i])
		{
			command = 4;
		}

		incrementIterationNumber();

		return command;
	}
	public void generateNextAim(int epuckX, int epuckY, int epuckMap){
		
		
		
		
		
	}
	public void generateNextMove(int epuckX, int epuckY, boolean epuckPolarisation,
			boolean epuckDirection, int targetX, int targetY, boolean left_sensor, 
			boolean front_sensor, boolean right_sensor)
	{
		System.out.println("\nPoczatekIteracji:"+"\n"+"-----------------");
		System.out.println("PosX: "+x[i]+" PosY:"+y[i]);
		System.out.println("rotate: "+rotate[i]+" clockwise: "+clockwise[i]+" iteracja:"+i);
		System.out.println("polarysation: "+vertical[i]+" direction: "+direction[i]);
		int xDisplacement = targetX - x[i];
		int yDisplacement = targetY - y[i];
		
		double[] reward = { Math.sin(yDisplacement / (double)mapSize * Math.PI / 2),
				Math.sin(xDisplacement / (double)mapSize * Math.PI / 2),
				-Math.sin(yDisplacement / (double)mapSize * Math.PI / 2),
				-Math.sin(xDisplacement / (double)mapSize * Math.PI / 2) };
		System.out.println("0:"+reward[0]+" 1:"+reward[1]+" 2:"+reward[2]+" 3:"+reward[3]);
		double[] directionMultiplier = new double[4];
		if (vertical[i]) {
			if (direction[i])
			{
				directionMultiplier[0] = 0.75;
				directionMultiplier[1] = 0.1;
				directionMultiplier[2] = 0;
				directionMultiplier[3] = 0.1;
			} else
			{
				directionMultiplier[0] = 0;
				directionMultiplier[1] = 0.1;
				directionMultiplier[2] = 0.75;
				directionMultiplier[3] = 0.1;
			}
		}
		else{
			if (direction[i])
			{
				directionMultiplier[0] = 0.1;
				directionMultiplier[1] = 0.75;
				directionMultiplier[2] = 0.1;
				directionMultiplier[3] = 0;
			} else
			{
				directionMultiplier[0] = 0.1;
				directionMultiplier[1] = 0;
				directionMultiplier[2] = 0.1;
				directionMultiplier[3] = 0.75;
			}
		}

		for (int j = 0; j < 4; j++)
		{
			reward[j] = reward[j] * directionMultiplier[j];
		}
		for (int j=0;j<4;j++){
		System.out.println("Reward w kierunku"+j+" "+reward[j]);
		}
		// analyze sensors data

		if(vertical[i]) {
			if (left_sensor)
			{
				if (direction[i])
				{
					reward[3] = -inf;
				} else
				{
					reward[1] = -inf;
				}
			}
			if (front_sensor)
			{
				if (direction[i])
				{
					reward[0] = -inf;
				} else
				{
					reward[2] = -inf;
				}
			}
			if (right_sensor)
			{
				if (direction[i])
				{
					reward[1] = -inf;
				} else
				{
					reward[3] = -inf;
				}
			}
		}
		else{
			if(left_sensor){
				if (direction[i])
				{
					reward[0] = -inf;
				}
				else
				{
					reward[2] = -inf;
				}
			}
			if(front_sensor){
				if (direction[i] )
				{
					reward[1] = -inf;
				}
				else
				{
					reward[3] = -inf;
				}
			}
			if(right_sensor){
				if (direction[i])
				{
					reward[2] = -inf;
				} else
				{
					reward[0] = -inf;
				}
			}
		}
			
		aim[i] = maxInd(reward);
		System.out.println("Jedzie w kierunku:"+aim[i]);
		
		
		if (vertical[i]){
			if (aim[i] == 0){	
				rotate[i] = false;
				if (direction[i])	
					clockwise[i] = true;
				else	
					clockwise[i] = false;
			}
			else if (aim[i] == 1){
				rotate[i] = true;
				if (direction[i])
					clockwise[i] = true;
				else
					clockwise[i] = false;
			}
			else if (aim[i] == 2){
				rotate[i] = false;
				if (direction[i])
					clockwise[i] = false;
				else
					clockwise[i] = true;
			}
			else if (aim[i] == 3){
				rotate[i] = true;
				if (direction[i])
					clockwise[i] = false;
				else
					clockwise[i] = true;
	
			}	
		}
		else{
			if (aim[i] == 0){
				rotate[i] = true;
				if (direction[i])
					clockwise[i] = false;
				else
					clockwise[i] = true;
			}
			else if (aim[i] == 1){
				rotate[i] = false;
				if (direction[i])
					clockwise[i] = true;
				else
					clockwise[i] = false;
			}
			else if (aim[i] == 2){
				rotate[i] = true;
				if (direction[i])
					clockwise[i] = true;
				else
					clockwise[i] = false;
			}
			else if (aim[i] == 3){
				rotate[i] = false;
				if (direction[i])
					clockwise[i] = false;
				else
					clockwise[i] = true;
			}	
		}

		System.out.println("rotate: "+rotate[i]+" clockwise: "+clockwise[i]+" iteracja:"+i);
		y[i + 1] = y[i];
		x[i + 1] = x[i];
		vertical[i + 1] = vertical [i];
		direction[i + 1] = direction [i];
		if (vertical[i]){
			if (rotate[i]){
				vertical[i+1] = false;
				if (clockwise[i])
					direction[i+1] = true;
				else
					direction[i+1] = false;
			}
			else{
				if (direction[i]){
					if (clockwise[i])
						y[i + 1] = y[i] + 1;
					else
						y[i + 1] = y[i] - 1;
				}
				else{
					if (clockwise[i])
						y[i + 1] = y[i] - 1;
					else
						y[i + 1] = y[i] + 1;
			}
			}
		}
		else {
			if (rotate[i]){
				vertical[i+1] = true;
				if (clockwise[i])
					direction[i+1] = false;
				else
					direction[i+1] = true;
			}
			else{
				if (direction[i]){
					if (clockwise[i])
						x[i + 1] = x[i] + 1;
					else
						x[i + 1] = x[i] - 1;
				}
				else{
					if (clockwise[i])
						x[i + 1] = x[i] - 1;
					else
						x[i + 1] = x[i] + 1;
				}
			}
		}
	}

	public void setMapPZ()
	{
		epuckMap.vertical = vertical[i];
		epuckMap.direction = direction[i];
	}

	private static int maxInd(double[] dane)
	{

		double max = -100;
		int indMax=0;
		for (int i = 0; i < dane.length; i++)
		{
			if (dane[i] > max)
			{
				max = dane[i];
				indMax=i;
			}
		}
		return indMax;
	}

	public void incrementIterationNumber()
	{
		i = i + 1;
	}
//public static void main(String[] args){
//	EpuckController ep=new EpuckController();
//	int r = 0;
//	while (r < 100){
//		ep.generateNextMove(20 , 20, true, true, 45, 50, false, false, false);
//		ep.incrementIterationNumber();
//		r++;
//}
//}
}
