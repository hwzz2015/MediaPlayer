package javawave;


import java.awt.image.*;
import java.io.*;



public class ImageDisplay {


	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img)
	{
		try
		{
			int frameLength = width*height*3;

			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public BufferedImage[] showIms(String filename,int length, int width, int height){

		BufferedImage[] imgarray = new BufferedImage[length];
		String relative_path = filename;
		for(int idx =1; idx <=length;idx++) {
			imgarray[idx-1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			String pic_num = String.valueOf(idx);
			String current_path = relative_path.concat(("000"+pic_num).substring(pic_num.length())).concat(".rgb");
			readImageRGB(width, height, current_path, imgarray[idx-1]);
		}


		return  imgarray;

	}


	public static void main(String[] args) {
//		ImageDisplay ren = new ImageDisplay();
//		ren.showIms(args);
	}

}
