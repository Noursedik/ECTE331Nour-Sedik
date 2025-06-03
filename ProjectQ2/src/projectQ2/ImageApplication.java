package projectQ2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.imageio.ImageIO;

 
public class ImageApplication{
	
	public static int level=255;
	public static int num_loops=5;
	
	
	
	public static void main(String[] args) {
		String fileName1="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Rain_Tree.jpg";
		String fileName2="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Wr.jpg";  
		String fileName3="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Wr1.jpg"; 
		String fileName4="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Wr2.jpg"; 
		int [] numThreads= {1,2,6,10};
		
		colourImage input_img= new colourImage();
        // read the image filename1 and store its dimension and pixel values in ImgStruct			
		imageReadWrite.readJpgImage(fileName1, input_img);
		 
		long duration=0;
		long totalDuration=0;
		long timeStart=0;
		long timeEnd=0;
		
		for(int i=0; i<num_loops; i++) {
			//1.1 create an empty output image
			colourImage output_img= new colourImage();
			
			//1.2 get height and width
			output_img.width=input_img.width;
			output_img.height=input_img.height;
			
			//1.3 create the pixel values
			output_img.pixels=new short[output_img.height][output_img.width][];
			
			timeStart=System.nanoTime();
			
			//1.4 call histogram equalization
			singleHistogramEqualization(input_img, output_img);
			
			timeEnd=System.nanoTime();
			duration=timeEnd-timeStart;
			totalDuration=totalDuration+duration;
			System.out.printf("%d: Single Thread Histogram Equalization took %.3f milliseconds.%n", i + 1, duration / 1e6);
			//1.5 Write the image
			imageReadWrite.writeJpgImage(output_img, fileName2);
		}
		System.out.printf("Single Thread average execution time over %d runs: %.3f milliseconds.%n", num_loops, totalDuration / (num_loops * 1e6));
		
		//MultiThread Figure2.a
		for(int j:numThreads) {
			totalDuration=0;
			
			for(int i=0; i<num_loops; i++) {
				//1.1 create an empty output image
				colourImage output_img= new colourImage();
				
				//1.2 get height and width
				output_img.width=input_img.width;
				output_img.height=input_img.height;
				
				//1.3 create the pixel values
				output_img.pixels=new short[output_img.height][output_img.width][3];
				timeStart=System.nanoTime();
				
				//1.4 call histogram equalization
				try {
				multiFigure2aHistogramEqualization(input_img, output_img,j);
				}catch (InterruptedException e) {
				    e.printStackTrace();
				}

				
				//calculating execution time for each thread
				timeEnd=System.nanoTime();
				duration=timeEnd-timeStart;
				totalDuration=totalDuration+duration;
				
				System.out.printf("%d: Multi Thread %d Histogram Equalization (2.a) took %.3f milliseconds.%n", i + 1,j, duration / 1e6);
				//1.5 Write the image
				imageReadWrite.writeJpgImage(output_img, fileName3);
			}
			System.out.printf("Multi Thread %d average execution time (2.a) over %d runs: %.3f milliseconds.%n", j,num_loops, totalDuration / (num_loops * 1e6));
		}
			//MultiThread Figure2.b
			for(int j:numThreads) {
				totalDuration=0;
				
				for(int i=0; i<num_loops; i++) {
					//1.1 create an empty output image
					colourImage output_img= new colourImage();
					
					//1.2 get height and width
					output_img.width=input_img.width;
					output_img.height=input_img.height;
					
					//1.3 create the pixel values
					output_img.pixels=new short[output_img.height][output_img.width][3];
					timeStart=System.nanoTime();
					
					//1.4 call histogram equalization
					try {
					multiFigure2bHistogramEqualization(input_img, output_img,j);
					}catch (InterruptedException e) {
					    e.printStackTrace();
					}

					
					//calculating execution time for each thread
					timeEnd=System.nanoTime();
					duration=timeEnd-timeStart;
					totalDuration=totalDuration+duration;
					
					System.out.printf("%d: Multi Thread %d Histogram Equalization (2.b) took %.3f milliseconds.%n", i + 1,j, duration / 1e6);
					//1.5 Write the image
					imageReadWrite.writeJpgImage(output_img, fileName4);
				}
				System.out.printf("Multi Thread %d average execution time (2.b) over %d runs: %.3f milliseconds.%n", j,num_loops, totalDuration / (num_loops * 1e6));
			
		}
            
		// demo reshaping a 4*4 matrix into 16 1-D array
		int width=4, height=4;
		short mat [][]=new short[height][width];
		  for(int i=0; i<height; i++)
			  for(int j=0;j<width;j++)
				  mat[i][j]=(short)(i*width+j);	
	     
	     short vect []=new short[height*width];
	     matManipulation.mat2Vect(mat, width, height, vect);
	   
	     for(int i=0; i<height; i++) {	    
			  for(int j=0;j<width;j++)
				  System.out.printf("%3d ", mat[i][j]);
			  System.out.println();
	     }
	     
	     
	     for(int i=0; i<width*height; i++) 	    
				  System.out.printf("%d ", vect[i]);
	
	     
	     
	} // main
	
	public static void singleHistogramEqualization(colourImage input, colourImage output){
		
		int size= input.height*input.width;
		//step 1 calculate histogram 
		for(int color=0; color<3; color++) {
			int[] histogram= new int[level+1];
			//might need level +1
			for(int i=0; i< input.height;i++){
				for(int j=0;j<input.width;j++) {
					histogram[input.pixels[i][j][color]]++;
					
				}
			}
			
			//step 2 calculate the cumulative histogram
			int[] cumulativeHist= new int [level+1];
			//set the first value of CulHistogram to first value of histogram
			cumulativeHist[0]=histogram[0];
			for(int i=1;i<=level;i++) {
				cumulativeHist[i]=cumulativeHist[i-1]+histogram[i];
				
			}
			
			//compute step 3
			// normalize the cumulative histogram
			for(int i=0; i<=level;i++) {
				cumulativeHist[i]=(cumulativeHist[i] *level)/size;
			}
			//performing histogram equalization
			for(int i=0; i< input.height;i++){
				for(int j=0;j<input.width;j++) {
					if(color==0) {
						output.pixels[i][j]=new short[3];
					}
					
					output.pixels[i][j][color]=(short) cumulativeHist[input.pixels[i][j][color]];
					
				}
			}
			
			
			
		}
	}
	
	
	
	public static void multiFigure2aHistogramEqualization(colourImage input, colourImage output, int numThreads) throws InterruptedException{
		
		int size= input.height*input.width;
		//step 1 create shared histogram using AtomicIntegerArray for thread-safe increments
		AtomicIntegerArray[] sharedHistogram = new AtomicIntegerArray[3];
		for(int color=0; color<3; color++) {
			sharedHistogram[color]= new AtomicIntegerArray(level + 1);
	}
	
		//define inner class for multi-threading, each thread computes histogram on a subset of rows
		class MultiThreadHistogram2a extends Thread{
			int startRow;
			int endRow;
			
			public MultiThreadHistogram2a(int startRow, int endRow) {
				this.startRow=startRow;
				this.endRow=endRow;
			}
			
			public void run() {
				//step 2 local histogram for this thread to avoid contention
				int [][]localHistogram=new int[3][level+1];
				//might need level +1
				//step 3 compute local histogram for assigned rows
				for(int color=0; color<3; color++) {
					for(int i=startRow; i< endRow;i++){
						for(int j=0;j<input.width;j++) {
							localHistogram[color][input.pixels[i][j][color]]++;
							
						}
					}
			}
			// step 4 safely add local histogram counts to shared histogram
			for(int color=0; color<3; color++) {
				for(int i=0;i<=level;i++) {
							sharedHistogram[color].getAndAdd(i, localHistogram[color][i]);
					}
				}
		}
		            
}       //step 5 create and start threads, dividing rows evenly among them
		Thread[] threads=new Thread[numThreads];
		int rowsPerThread= input.height/numThreads;
		
		for(int t=0; t<numThreads; t++) {
			int startRow= t*rowsPerThread;
			int endRow= (t==numThreads-1)? input.height : startRow+rowsPerThread;
			threads[t]= new MultiThreadHistogram2a( startRow, endRow);
			threads[t].start();
		}
		
		//step 6 wait for all threads to finish execution
		for(int t=0; t<numThreads; t++) {
			threads[t].join();
		}
		
		//step 7 compute cumulative histogram for each color channel from shared histogram
		int[][] cumulativeHist= new int [3][level+1];
		for(int color=0; color<3; color++) {
			cumulativeHist[color][0]=sharedHistogram[color].get(0);

			for(int i=1; i<=level;i++) {
				cumulativeHist[color][i]= cumulativeHist[color][i-1]+sharedHistogram[color].get(i);
			}
			// normalize the cumulative histogram
			for(int i=0; i<=level;i++) {
				cumulativeHist[color][i]= (cumulativeHist[color][i]*level)/size;
			}
		}
		
		//performing histogram equalization
		for(int i=0; i< input.height;i++){
			for(int j=0;j<input.width;j++) {
				for(int color=0; color<3 ; color++) {
					output.pixels[i][j][color]=(short) cumulativeHist[color][input.pixels[i][j][color]];
				}
				
			}
		}
}
	
	
	public static void multiFigure2bHistogramEqualization(colourImage input, colourImage output, int numThreads) throws InterruptedException{
		int size= input.height*input.width;
		
		//step 1 create shared histogram using AtomicIntegerArray for thread-safe increments
		AtomicIntegerArray[] sharedHistogram = new AtomicIntegerArray[3];
		for(int color=0; color<3; color++) {
			sharedHistogram[color]= new AtomicIntegerArray(level + 1);
		}
		
		class MultiThreadHistogram2b extends Thread{
			int threadNumber;
			
			public MultiThreadHistogram2b (int threadNumber){
				this.threadNumber=threadNumber;
			}
		
			public void run() {
				//step 2 local histogram for this thread to avoid contention
				int [][]localHistogram=new int[3][level+1];
				//might need level +1
				//step 3 compute local histogram
				for(int i=threadNumber; i<size; i+=numThreads) {
					int k= i/input.width;
					int j= i%input.width;
					
					for(int color=0; color<3; color++) {
						localHistogram[color][input.pixels[k][j][color]]++;
								
						}
				}
				// step 4 safely add local histogram counts to shared histogram
				for(int color=0; color<3; color++) {
					for(int i=0;i<=level;i++) {
								sharedHistogram[color].getAndAdd(i, localHistogram[color][i]);
					}
				}
			}
		}
		//step 5 create and start threads
		Thread[] threads=new Thread[numThreads];
		for(int i=0; i<numThreads; i++) {
			threads[i]=new MultiThreadHistogram2b(i);
			threads[i].start();
		}
		
		//step 6 wait for all threads to finish execution
		for(int t=0; t<numThreads; t++) {
					threads[t].join();
		}
		//step 7 compute cumulative histogram for each color channel from shared histogram
		int[][] cumulativeHist= new int [3][level+1];
		for(int color=0; color<3; color++) {
			cumulativeHist[color][0]=sharedHistogram[color].get(0);
			
			for(int i=1; i<=level;i++) {
				cumulativeHist[color][i]= cumulativeHist[color][i-1]+sharedHistogram[color].get(i);
				}
				// normalize the cumulative histogram
				for(int i=0; i<=level;i++) {
					cumulativeHist[color][i]= (cumulativeHist[color][i]*level)/size;
				}
			}
				
		//performing histogram equalization
		for(int i=0; i< input.height;i++){
			for(int j=0;j<input.width;j++) {
				for(int color=0; color<3 ; color++) {
					output.pixels[i][j][color]=(short) cumulativeHist[color][input.pixels[i][j][color]];
						}
					}
				}
			}
  
    	
/**
 * 
 * A class with 2 Utility methods to read the pixels and dimension of an image, and write the image data into a jpeg file
 *
 */
static class imageReadWrite{

	public static void readJpgImage(String fileName, colourImage ImgStruct) {
		 try {
	            // Read the image file
	            File file = new File(fileName);
	            BufferedImage image = ImageIO.read(file);
	            
	            System.out.println("file: "+file.getCanonicalPath());
	            
	            // Check if the image is in sRGB color space
	            if (!image.getColorModel().getColorSpace().isCS_sRGB()) {
	                System.out.println("Image is not in sRGB color space");
	                return;
	            }
	            
	            // Get the width and height of the image
	            int width = image.getWidth();
	            int height = image.getHeight();
	            ImgStruct.width=width;
	            ImgStruct.height=height;
	            ImgStruct.pixels=new short[height][width][3];

	           // Loop over each pixel of the image and store its RGB color components in the array
	            for (int y = 0; y < height; y++) {
	                for (int x = 0; x < width; x++) {
	                    // Get the color of the current pixel
	                    int pixel = image.getRGB(x, y);
	                    Color color = new Color(pixel, true);

	                    // Store the red, green, and blue color components of the pixel in the array
	                    ImgStruct.pixels[y][x][0] = (short) color.getRed();
	                    ImgStruct.pixels[y][x][1] = (short) color.getGreen();
	                    ImgStruct.pixels[y][x][2] = (short) color.getBlue();
	                }
	            }            
	                       

	        } catch (IOException e) {
	            System.out.println("Error reading image file: " + e.getMessage());
	        }  	
	}

	public static void writeJpgImage(colourImage ImgStruct, String fileName) {
		 try {
	    	 int width = ImgStruct.width;
	         int height = ImgStruct.height;
	         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	         // Set the RGB color values of the BufferedImage using the pixel array
	         for (int y = 0; y < height; y++) {
	             for (int x = 0; x < width; x++) {
	                 int rgb = new Color(ImgStruct.pixels[y][x][0], ImgStruct.pixels[y][x][1], ImgStruct.pixels[y][x][2]).getRGB();
	                 image.setRGB(x, y, rgb);
	             }
	         }

	         // Write the BufferedImage to a JPEG file
	         File outputFile = new File(fileName);
	         ImageIO.write(image, "jpg", outputFile);

	     } catch (IOException e) {
	         System.out.println("Error writing image file: " + e.getMessage());
	     }       
	
       }//

}

static class matManipulation{
	/**
	 * reshape a matrix to a 1-D vector
	 */
	public static void mat2Vect (short [][] mat, int width, int height, short[] vect) {
		for(int i=0;i<height; i++)
			for (int j=0; j<width; j++)
				vect[j+i*width]=mat[i][j];
	}
	
}


static class colourImage {
	/**
	 * A data structure to store a color image
	 */
	public int width;
	public int height;
	public short pixels[][][];
}
}

