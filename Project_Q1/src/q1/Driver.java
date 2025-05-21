package q1;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//file logger utility class
 class FileLogger{
	
	 static final int MAX_BACKUP=4; //max number of backup log files
	 private static final Random random=new Random(); //creating a Random object
	
	// logging method
	public static void log(String baseFilename, String message) {
       String eventDate_Time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //adding time stamp
       String fullMessage = eventDate_Time + " , " + message;
      
       //Trying to write to the log and base files in-case of any writing failures
       for (int i = 0; i <= MAX_BACKUP; i++) {
           String filename = (i == 0) ? "log.txt" : baseFilename + i + ".txt"; //first log the file name is log then any other attempt the final name is basei
           try {
               // Simulate 40% failure rate
               if (random.nextInt(100) < 40) {
                   throw new IOException("Simulated write failure");
               }
               FileWriter writer = new FileWriter(filename, true);
               writer.write(fullMessage + "\n"); //Trying to write full message
               writer.close();
               System.out.println("Logging successful for "+filename);
               return; // Exit after successful log
           } catch (IOException e) {
               System.err.println("Logging failed for " + filename + ": " + e.getMessage());//error message for logging failure
           }
       }
      System.out.println("Failed to log on all base files so logging to principal file");
    // If all backups fail, write to principal_log.txt
       try {
           FileWriter writer = new FileWriter("principal_log.txt", true);
           writer.write(fullMessage + " Logging failed on all backup files. Message stored in principal_log.txt as last resort \n");
           writer.close();
       } catch (IOException e) {
           System.err.println("Final logging to principal_log.txt failed: " + e.getMessage());
       }
	}
}
//Main class
public class Driver {
	
	private static final Random random=new Random(); //creating a Random object
	private static Scanner sc= new Scanner(System.in); //creating a Scanner object
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Initializing variables
		double cap;
		final int sensors_number=3;
		double temperature;
		double humidity;
		int previousValue = 0;
		
		// Read temperature limit from user, generate and display temperature
		System.out.println("Please enter the temperature limit");
		cap=sc.nextDouble();
		temperature=generateTemperature(cap);
		
		System.out.println("The random generated temperature is "+ temperature+ "degrees");
		
		// Generate and display humidity
		humidity=generateHumidity();
		System.out.println("The random generated Humidity is "+ humidity+ "%");
		
		//declaring Critical Sensors array
		int[] criticalSensor=new int[sensors_number];
		
		//generating values for the critical sensors
		for(int i=0; i<sensors_number; i++) {
			criticalSensor[i]=generateCriticalSensor();
			System.out.println("The value of sensor 3."+(i+1)+" is "+ criticalSensor[i]);
		}
		
		//Majority voter used to determine correct value or fall back
		int result=majorityVoter(criticalSensor);
		if(result!=-1) {
			previousValue=result; // Update only if majority exists
		}else
			System.out.println("All sensor values are different so falling back to previously recorded result");
		
		System.out.println("The correct value from all three sensors is "+previousValue);
	}
	private static double generateTemperature(double cap) { //generating temperature within user limit method
		return random.nextDouble() *cap;
	}
	
	private static double generateHumidity() { //generating Humidity method (0%-100%)
		return random.nextDouble()*100;
	}
	
	private static int generateCriticalSensor() { //generating random values for critical sensors
		return (int)(random.nextDouble()*10);
	}
	
	private static int majorityVoter(int [] criticalSensor) {
		boolean flag01 = false,flag12 = false,flag02 = false;
		int sensor0=criticalSensor[0];
		int sensor1=criticalSensor[1];
		int sensor2=criticalSensor[2];
		
		//setting flags based on critical sensors values
		if(sensor0==sensor1)
			flag01=true;
		if(sensor1==sensor2)
			flag12=true;
		if(sensor0==sensor2)
			flag02=true;
		
		//comparing flags and recording discrepancies if 1 or more sensors are of different value
		if(flag01 && flag02) {
		System.out.println("All sensor values are the same so no need to log discrepancy");
		return sensor0;
	} else if(flag01 || flag02) {
		logDiscrepancy(sensor0,sensor1,sensor2);
		return sensor0;
	}else if(flag12) {
		logDiscrepancy(sensor0,sensor1,sensor2);
		return sensor1;
	}else {
		logDiscrepancy(sensor0,sensor1,sensor2);
		return -1;
	}
	}
	
	//recording discrepancies method
	private static void logDiscrepancy(int sensor0, int sensor1, int sensor2) {
		boolean flag01 = false,flag12 = false,flag02 = false;
		String outlier;
		
		if(sensor0==sensor1)
			flag01=true;
		if(sensor1==sensor2)
			flag12=true;
		if(sensor0==sensor2)
			flag02=true;
		
       if (flag12) 
    	   outlier= "Sensor 3.1 ";
       else if (flag02) 
    	   outlier="Sensor 3.2 ";
       else if (flag01) 
    	   outlier="Sensor 3.3 ";
       else 
    	   outlier="Sensor 3.1, Sensor 3.2, Sensor3.3";
       
       String message = "Discrepancy detected, Outlier(s): " + outlier;
       FileLogger.log("base", message);
   }
}
