import java.util.ArrayList;

public class IRSensorsService{

	private ArrayList<IRSensor> listOfSensors = new ArrayList<IRSensor>();
	private ArrayList<Integer> listOfReads = new ArrayList<Integer>();
	private ArrayList<Double> listOfLimits = new ArrayList<Double>();
	
	public IRSensorsService(){
		registerAllSensors();
		registerGridPoints();
		populateListOfLimits();
	}

	private void registerAllSensors()
	{
		System.out.println("Registering Sensors");
		listOfSensors.add(new IRSensor("ps5"));
		//listOfSensors.add(new IRSensor("ps6"));
		listOfSensors.add(new IRSensor("ps7"));
		listOfSensors.add(new IRSensor("ps0"));
		//listOfSensors.add(new IRSensor("ps1"));
		listOfSensors.add(new IRSensor("ps2"));
		listOfSensors.get(0).enable(50);
		listOfSensors.get(1).enable(50);
		listOfSensors.get(2).enable(50);
		listOfSensors.get(3).enable(50);
		//listOfSensors.get(4).enable(50);
		//listOfSensors.get(5).enable(50);
		
	}
	
	private void registerGridPoints()
	{
		for (int i = 0; i<9; i++){
			listOfReads.add(0);
		}
	}
	
	private void populateListOfLimits()
	{
		listOfLimits.add((double) 350);
		listOfLimits.add((double) 350);
		listOfLimits.add((double) 350);
		listOfLimits.add((double) 350);
		//listOfLimits.add((double) 350);
		//listOfLimits.add((double) 350);
	}
	
	public void calculateReadsArray(){
		//if(listOfSensors.get(0).getValue() >= listOfLimits.get(0) || listOfSensors.get(1).getValue() >= listOfLimits.get(1)){
		if(listOfSensors.get(0).getValue() >= listOfLimits.get(0)){
			setObstacleLeft();
			}
		else{
			zeroObstacle(-1);
		}
		//if(listOfSensors.get(2).getValue() >= listOfLimits.get(2) || listOfSensors.get(3).getValue() >= listOfLimits.get(3)){
		if((listOfSensors.get(1).getValue() + listOfSensors.get(2).getValue()) / 2 >= listOfLimits.get(1)){
			setObstacleUp();
			}	
		else{
			zeroObstacle(0);
		}
		//if(listOfSensors.get(4).getValue() >= listOfLimits.get(4) || listOfSensors.get(5).getValue() >= listOfLimits.get(5)){
		if(listOfSensors.get(3).getValue() >= listOfLimits.get(2)){
			setObstacleRight();
		}
		else{
			zeroObstacle(1);
		}
		for(int i =0; i<listOfSensors.size();i++){
			System.out.print("  || "+i+": "+listOfSensors.get(i).getValue());
		}
		System.out.println();
		System.out.println(listOfReads.toString());
		
	}
	
	private void zeroObstacle(int leftTopRight)
	{
		switch(leftTopRight){
		case -1:
			for (int i = 0; i<3; i++){
				listOfReads.set(i,0);
			}
			break;
		
		case 0:
			for (int i = 3; i<6; i++){
				listOfReads.set(i,0);
			}
			break;
		case 1:
			for (int i = 6; i<9; i++){
				listOfReads.set(i,0);
			}
			break;
		}
	}

	public ArrayList<Integer> getReadsArray(){
		return listOfReads;
	}
	
	public int[] getReadsTable(){
		int[]table = new int [listOfReads.size()];
		
		for(int i =0; i<listOfReads.size();i++){
			table[i] = listOfReads.get(i);
		}
		
		return table;
	}
	
	public int getLeft(){
//		calculateReadsArray();
		return listOfReads.get(2);
	}
	
	public int getFront(){
//		calculateReadsArray();
		return listOfReads.get(5);
	}
	
	public int getRight(){
//		calculateReadsArray();
		return listOfReads.get(8);
	}
	
	private void setObstacleLeft(){
		for (int i = 0; i<3; i++){
			listOfReads.set(i,1);
		}
	}
	
	private void setObstacleUp(){
		for (int i = 3; i<6; i++){
			listOfReads.set(i,1);
		}
	}
	
	private void setObstacleRight(){
		for (int i = 6; i<9; i++){
			listOfReads.set(i,1);
		}
	}
	
	
	
}
