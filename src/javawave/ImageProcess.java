package javawave;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;



public class ImageProcess {

    JFrame frame;
    JLabel lbIm1;
    BufferedImage imgOne;
    int width = 352;
    int height = 288;

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

    public BufferedImage shrink(BufferedImage img,int width, int height,int times){
        BufferedImage img2;
        for(int t=0;t<times;t++){
            width/=2;
            height/=2;
            img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++) {
                    int[][] arr = {{0, 1}, {1, 0},{0,0},{1,1}};
                    int[] newcolor = new int[3];
                    for(int[] a : arr){
                        int temp = img.getRGB(x*2+a[0],y*2+a[1]);
                        Color c = new Color(temp);
                        newcolor[0] += c.getRed()/4;
                        newcolor[1] += c.getGreen()/4;
                        newcolor[2] += c.getBlue()/4;
                    }
                    int pix = 0xff000000 | ((newcolor[0] & 0xff) << 16) | ((newcolor[1] & 0xff) << 8) | (newcolor[2] & 0xff);
                    img2.setRGB(x,y,pix);
                }
            }
            img = img2;
        }


        return img;
    }

    public void showIms(String args){

        // Read in the specified image
        imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, args, imgOne);
        BufferedImage img2 = shrink(imgOne,width, height,4);

        // Use label to display the image
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        lbIm1 = new JLabel(new ImageIcon(img2));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm1, c);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ImageProcess ren = new ImageProcess();
        ren.showIms("resources/interview/interview001.rgb");
    }

}
