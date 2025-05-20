package q1;

import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

  class FileLogger{
	
	 static final int MAX_BACKUP=4;
	 private static final Random random=new Random();
	
	public static void log(String baseFilename, String message) {
        String eventDate_Time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fullMessage = eventDate_Time + " , " + message;

        for (int i = 0; i <= MAX_BACKUP; i++) {
            String filename = (i == 0) ? "log.txt" : baseFilename + i + ".txt";
            try {
                // Simulate 40% failure
                if (random.nextInt(100) < 40) {
                    throw new IOException("Simulated write failure");
                }

                FileWriter writer = new FileWriter(filename, true);
                writer.write(fullMessage + "\n");
                writer.close();
                return;
            } catch (IOException e) {
                System.err.println("Logging failed for " + filename + ": " + e.getMessage());
            }
        }
        
        try {
            FileWriter writer = new FileWriter("principal_log.txt", true);
            writer.write(fullMessage + " Logging failed on all backup files. Message stored in principal_log.txt as last resort \n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Final logging to principal_log.txt failed: " + e.getMessage());
        }
	}
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
		int previousValue = 0;
		
		System.out.println("Please enter the temperature limit");
		cap=sc.nextDouble();
		temperature=generateTemperature(cap);
		
		System.out.println("The random generated temperature is "+ temperature+ "degrees");
		
		humidity=generateHumidity();
		System.out.println("The random generated Humidity is "+ humidity+ "%");
		
		
		int[] criticalSensor=new int[sensors_number];
		
		for(int i=0; i<sensors_number; i++) {
			criticalSensor[i]=generateCriticalSensor();
			System.out.println("The value of sensor 3."+(i+1)+" is "+ criticalSensor[i]);
		}
		
		int result=majorityVoter(criticalSensor);
		if(result!=-1) {
			previousValue=result;
		}else
			System.out.println("All sensor values are different so falling back to previously recorded result");
		
		System.out.println("The correct value from all three sensors is "+previousValue);
	}

	private static double generateTemperature(double cap) {
		return random.nextDouble() *cap;
	}
	
	private static double generateHumidity() {
		return random.nextDouble()*100;
	}
	
	private static int generateCriticalSensor() {
		return (int)(random.nextDouble()*10);
	}
	
	private static int majorityVoter(int [] criticalSensor) {
		boolean flag01 = false,flag12 = false,flag02 = false;
		int sensor0=criticalSensor[0];
		int sensor1=criticalSensor[1];
		int sensor2=criticalSensor[2];
		
		if(sensor0==sensor1)
			flag01=true;
		if(sensor1==sensor2)
			flag12=true;
		if(sensor0==sensor2)
			flag02=true;
		
		if(flag01 && flag02) {
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
	
	private static void logDiscrepancy(int s0, int s1, int s2) {
        StringBuilder outliers = new StringBuilder();
        if (s0 != s1 && s0 != s2) outliers.append("Sensor 3.1 ");
        if (s1 != s0 && s1 != s2) outliers.append("Sensor 3.2 ");
        if (s2 != s0 && s2 != s1) outliers.append("Sensor 3.3 ");

        String message = "Discrepancy detected. Outlier(s): " + outliers.toString().trim();
        FileLogger.log("base", message);
    }
}
