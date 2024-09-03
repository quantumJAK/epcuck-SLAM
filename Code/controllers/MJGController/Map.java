public class Map {

	public static int mapSize = 50;
	public int[][] eMap;

	public static int epuckX0 = 20;
	public static int epuckY0 = 20;

	public boolean direction;
	public boolean vertical;

	public Map() {
		generateCleanMap();
		putEpuckOnMap();
	}

	public Map(int epuckX, int epuckY) {
		epuckX0 = epuckX;
		epuckY0 = epuckY;
		generateCleanMap();
		putEpuckOnMap();
	}

	public Map(int epuckX, int epuckY, int mapsize) {
		epuckX0 = epuckX;
		epuckY0 = epuckY;
		mapSize = mapsize;
		generateCleanMap();
		putEpuckOnMap();
	}

	public void generateCleanMap()
	{
		eMap = new int[mapSize][mapSize];
		for (int i = 0; i < mapSize; i++)
		{
			for (int j = 0; j < mapSize; j++)
			{
				eMap[i][j] = 3;
			}
		}
	}

	public void putEpuckOnMap()
	{
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				eMap[epuckY0 + i][epuckX0 + j] = 2;
			}
		}
	}

	public void drawSensorsData(int left_sensor, int front_sensor, int right_sensor, int epuckX0, int epuckY0)
	{	
		
		for (int i = 0; i < eMap[0].length; i++){
			System.out.println("");
			for (int j =0 ; j < eMap.length; j++){
				System.out.print(eMap[i][j]+" ");}
		}
		System.out.println("");
		for (int i = 0; i < 3; i++) 
		{
			if (vertical){
				if (direction)
				{
					System.out.println("y:"+(epuckY0 - 1 + i)+" x:"+(epuckX0 - 2));
					eMap[epuckY0 - 1 + i][epuckX0 - 2] = left_sensor; // West side
					eMap[epuckY0 - 1 + i][epuckX0 + 2] = right_sensor; // East
																		// side
					eMap[epuckY0 + 2][epuckX0 - 1 + i] = front_sensor; // North side
				} else
				{
					eMap[epuckY0 - 1 + i][epuckX0 - 2] = right_sensor; // West
																		// side
					eMap[epuckY0 - 1 + i][epuckX0 + 2] = left_sensor; // East side
					eMap[epuckY0 - 2][epuckX0 - 1 + i] = front_sensor; // South side
				}
			}
			else
			{
				if (direction)
				{
					eMap[epuckY0 - 1 + i][epuckX0 + 2] = front_sensor; // East side
					eMap[epuckY0 + 2][epuckX0 - 1 + i] = left_sensor; // North
																	// side
					eMap[epuckY0 - 2][epuckX0 - 1 + i] = right_sensor; // South
																		// side
				} else
				{
					eMap[epuckY0 - 1 + i][epuckX0 - 2] = front_sensor; // West side
					eMap[epuckY0 + 2][epuckX0 - 1 + i] = right_sensor; // North
																		// side
					eMap[epuckY0 - 2][epuckX0 - 1 + i] = left_sensor; // South
																	// side
				}
				
			}
		}	
			for (int i = 0; i < 3; i++){
				for (int j = 0 ; j < 3; j++){
					eMap[epuckY0 - 1 + i][epuckX0 - 1 + j] = 2;	//tu stoi robot
				}
			}
	}
	
	

	public boolean checkNorth()
	{
		boolean somethingIsOnTheNorth = false;
		for (int i = 0; i < 3; i++)
		{
			if (eMap[epuckY0 + 2][epuckX0 - 1 + i] == 1)
			{
				somethingIsOnTheNorth = true;
			}
		}
		return somethingIsOnTheNorth;
	}

	public boolean checkSouth()
	{
		boolean somethingIsOnTheSouth = false;
		for (int i = 0; i < 3; i++)
		{
			if (eMap[epuckY0 - 2][epuckX0 - 1 + i] == 1)
			{
				somethingIsOnTheSouth = true;
			}
		}
		return somethingIsOnTheSouth;
	}
	
	public boolean checkWest()
	{
		boolean somethingIsOnTheWest = false;
		for (int i = 0; i < 3; i++)
		{
			if (eMap[epuckY0 - 1 + i][epuckX0 - 2] == 1)
			{
				somethingIsOnTheWest = true;
			}
		}
		return somethingIsOnTheWest;
	}
	
	public boolean checkEast()
	{
		boolean somethingIsOnTheEast = false;
		for (int i = 0; i < 3; i++)
		{
			if (eMap[epuckY0 - 1 + i][epuckX0 + 2] == 1)
			{
				somethingIsOnTheEast = true;
			}
		}
		return somethingIsOnTheEast;
	}

}
