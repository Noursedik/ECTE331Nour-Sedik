package projectQ2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

 
public class ImageApplication{
	
	public static int level=255;
	public static int num_loops=5;
	
	
	
	public static void main(String[] args) {
		String fileName1="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Rain_Tree.jpg";
		String fileName2="C:\\Users\\isedd\\OneDrive\\Desktop\\Nour\\ECTE331 Project q2\\Wr.jpg";  
		
		int [] numThreads= {1,2,6,10};
		
		colourImage input_img= new colourImage();
        // read the image filename1 and store its dimension and pixel values in ImgStruct			
		imageReadWrite.readJpgImage(fileName1, input_img);
		 // write ImgStruct in the jpeg file filenName2	
		int duration=0;
		
		for(int i=0; i<num_loops; i++) {
			//1.1 create an empty output image
			colourImage output_img= new colourImage();
			
			//1.2 get height and width
			output_img.width=input_img.width;
			output_img.height=input_img.height;
			
			//1.3 create the pixel values
			output_img.pixels=new short[output_img.height][output_img.width][];
			
			//1.4 call histogram equalization
			singleHistogramEqualization(input_img, output_img);
			
			//1.5 Write the image
			imageReadWrite.writeJpgImage(output_img, fileName2);
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
	
	
	
	
	
	
	
	
	
	
	
	
		            
}

  
    	
/**
 * 
 * A class with 2 Utility methods to read the pixels and dimension of an image, and write the image data into a jpeg file
 *
 */
class imageReadWrite{

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

class matManipulation{
	/**
	 * reshape a matrix to a 1-D vector
	 */
	public static void mat2Vect (short [][] mat, int width, int height, short[] vect) {
		for(int i=0;i<height; i++)
			for (int j=0; j<width; j++)
				vect[j+i*width]=mat[i][j];
	}
	
}


class colourImage {
	/**
	 * A data structure to store a color image
	 */
	public int width;
	public int height;
	public short pixels[][][];
}

