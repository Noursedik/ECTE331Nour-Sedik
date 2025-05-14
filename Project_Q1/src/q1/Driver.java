package q1;

import java.util.Random;
import java.util.Scanner;

 class FileLogger{
	final int Max_Backup=4;
}
public class Driver {
	
	private static final Random random=new Random();
	private static Scanner sc= new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double cap;
		final int sensors_number=3; 
		double temperature;
		double humidity;
		
		System.out.println("Please enter the temperature limit");
		cap=sc.nextDouble();
		temperature=generateTemperature(cap);
		
		System.out.println("The random generated temperature is "+ temperature+ "degress");
		
		humidity=generateHumidity();
		System.out.println("The random generated Humidity is "+ humidity+ "%");
		
		
		int[] criticalSensor=new int[sensors_number];
		
		for(int i=0; i<sensors_number; i++) {
			criticalSensor[i]=generateCriticalSensor();
			System.out.println("The value of sensor 3."+(i+1)+" is "+ criticalSensor[i]);
		}
	}

	private static double generateTemperature(double cap) {
		return random.nextDouble() *cap;
	}
	
	private static double generateHumidity() {
		return random.nextDouble();
	}
	
	private static int generateCriticalSensor() {
		return (int)(random.nextDouble()*10);
	}
	
	private static double majorityVoter(double) {
		
	}
}
