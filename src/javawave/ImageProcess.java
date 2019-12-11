package javawave;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.text.IconView;


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
                        c.getColor("Gray");
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

    public int[][] makeGray(BufferedImage img)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        int num_pixel = w*h;
        double avg = 0;

        double[][] compare = new double[h][w];
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                // Normalize and gamma correct:
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                // Gamma compand and rescale to byte range:
//                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
//                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
//                img.setRGB(x, y, gray);
                double grayLevel = (255.0 * Math.pow(lum, 1.0 / 2.2));
                avg += grayLevel / num_pixel;
                compare[y][x] = grayLevel;
            }
        }
        int black = (255 << 16) + (255 << 8) + 255;
        int white = 0;
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                if(compare[y][x] >avg) img.setRGB(x, y, black);
                else img.setRGB(x, y, white);
            }
        }

        int[][] result = new int [h][w];
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                if(compare[y][x] >avg) result[y][x] = 1;
                else result[y][x] = 0;
            }
        }
        return result;
    }

    public void read_img_file(int[] board,BufferedImage img){
        int w = img.getWidth();
        int h = img.getHeight();
        int black = (255 << 16) + (255 << 8) + 255;
        int white = 0;
        for (int x = 0; x < h; ++x) {
            for (int y = 0; y < w; ++y) {
                if(board[x*w+y]==1){
                    img.setRGB(y, x, black);
                }
                else {
                    img.setRGB(y, x, white);
                }
            }
        }

    }



    public void save_to_file(int[][] input,String save_to_file_name){
        int row = input.length;
        int col = input[0].length;

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < row; i++)//for each row
        {
            for(int j = 0; j < col; j++)//for each column
            {
                builder.append(input[i][j]);//append to the output string
            }
        }
        builder.append("\n");
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(save_to_file_name,true));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[][] read_txt(int r,int c, String filename){
        int[][] board = new int[r][c];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = "";
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int idx=0;idx<line.length();idx++) {
                    board[row][idx] = Character.getNumericValue(line.charAt(idx));
                }
                row++;
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return board;
    }

    public void convert_video(int interval,int total_length, String relative_path,String save_to_file_name){
        for(int idx=1;idx<=total_length;idx+=interval){
            String pic_num = String.valueOf(idx);
            String current_path = relative_path.concat(("000"+pic_num).substring(pic_num.length())).concat(".rgb");
            imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            readImageRGB(width, height, current_path, imgOne);
            BufferedImage img2 = shrink(imgOne,width, height,4);
            int[][] result = makeGray(img2);
            save_to_file(result,save_to_file_name);
        }
    }

    public int compare_int_array(int[] a,int[] b){
        int count = 0;
        for(int i=0;i<a.length;i++){
            if(a[i] == b[i]) count++;
        }
        return count;
    }


    public static void show_img_from_array(){
        ImageProcess ren = new ImageProcess();
        int[][] r = ren.read_txt(15,22*18,"tempquerydata.txt");
        BufferedImage x = new BufferedImage(22, 18, BufferedImage.TYPE_INT_RGB);
        ren.read_img_file(r[0],x);
        ImageDisplay.show_single_Ims(x);
    }

    public static double[][] test(String testfile_path, String[] database_path){
        ImageProcess ren = new ImageProcess();
        int[][] q = ren.read_txt(15,22*18,testfile_path);
        int[][][] r = new int[7][][];
        for(int i=0;i<7;i++){
            r[i] = ren.read_txt(40,22*18,database_path[i]);
        }
        double[] possiblity = new double[7];
        for(int i=0;i<q.length;i++){
            int[] maxcount_per_catagory = new int[7];
            for(int j=0;j<7;j++){
                for(int k=0;k<40;k++){
                    int temp = ren.compare_int_array(q[i],r[j][k]);
                    maxcount_per_catagory[j] = Math.max(maxcount_per_catagory[j],temp);
                }
            }
//            System.out.println(Arrays.toString(maxcount_per_catagory)+findMaxIndex(maxcount_per_catagory));
            for(int j=0;j<7;j++){
                possiblity[j] += maxcount_per_catagory[j]/(double)(22*18);
            }
        }
        for(int i=0;i<7;i++){
            possiblity[i] /=q.length;
        }
//        System.out.println(Arrays.toString(possiblity));

        double[][] lines = new double[8][];
        for(int j=0;j<7;j++){
            double[] max_per_frame = new double[40];
            for(int k=0;k<40;k++){
                for(int i=0;i<q.length;i++){
                    double temp = ((double) ren.compare_int_array(q[i],r[j][k]))/396.0;
                    max_per_frame[k] = Math.max(max_per_frame[k],temp);

                }
            }
//            System.out.println(Arrays.toString(max_per_frame));
            lines[j] = max_per_frame;
        }

        lines[7] = possiblity;
        return lines;
    }

    public static int findMaxIndex(int [] arr) {
        int max = arr[0];
        int maxIdx = 0;
        for(int i = 1; i < arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    public static void convert(){
        ImageProcess ren = new ImageProcess();
        ren.convert_video(10,150,"resources/query_videos_2/SeenInexactMatch/HQ4/HQ4_","resources/data/SeenInexactMatchHQ4.txt");

        // used to convert the whole data base
//        ren.convert_video(15,600,"resources/flowers/flowers","resources/data/flowers.txt");
//        ren.convert_video(15,600,"resources/interview/interview","resources/data/interview.txt");
//        ren.convert_video(15,600,"resources/movie/movie","resources/data/movie.txt");
//        ren.convert_video(15,600,"resources/musicvideo/musicvideo","resources/data/musicvideo.txt");
//        ren.convert_video(15,600,"resources/sports/sports","resources/data/sports.txt");
//        ren.convert_video(15,600,"resources/starcraft/starcraft","resources/data/starcraft.txt");
//        ren.convert_video(15,600,"resources/traffic/traffic","resources/data/traffic.txt");


    }

    public static void main(String[] args) {
        String[] path = new String[7];
        path[0] = "resources/data/flowers.txt";
        path[1] = "resources/data/interview.txt";
        path[2] = "resources/data/movie.txt";
        path[3] = "resources/data/musicvideo.txt";
        path[4] = "resources/data/sports.txt";
        path[5] = "resources/data/starcraft.txt";
        path[6] = "resources/data/traffic.txt";
//        double[][] result = test("resources/data/SeenExactMatchQ3.txt", path);
//        convert();
        show_img_from_array();

    }

}
