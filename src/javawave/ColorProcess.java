package javawave;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ColorProcess {
    public static void main(String[] args) throws IOException {
        String query_file_path = "resources/query/second/second";

        ColorProcess Cp = new ColorProcess();
//        Cp.tranlate_to_file();
        int[][][] total_result = Cp.read_txt("resources/data/all_colors.txt");

        int[][] current_color = Cp.convert_video(10,150,query_file_path);

        Cp.compare_color(current_color,total_result);

    }

    public void tranlate_to_file() throws IOException {
        int[][][] total_result = new int[7][40][3];
        total_result[0] = convert_video(15,600,"resources/flowers/flowers");
        total_result[1] = convert_video(15,600,"resources/interview/interview");
        total_result[2] = convert_video(15,600,"resources/movie/movie");
        total_result[3] = convert_video(15,600,"resources/musicvideo/musicvideo");
        total_result[4] = convert_video(15,600,"resources/sports/sports");
        total_result[5] = convert_video(15,600,"resources/starcraft/starcraft");
        total_result[6] = convert_video(15,600,"resources/traffic/traffic");

        save_to_file(total_result,"resources/data/all_colors.txt");
    }

    //return 40*3 or 15*3 ... int array
    public int[][] convert_video(int interval,int total_length, String relative_path) {
        int[][] result = new int[total_length/interval][3];
        try{
        for(int idx=1;idx<=total_length;idx+=interval){
            String pic_num = String.valueOf(idx);
            String current_path = relative_path.concat(("000"+pic_num).substring(pic_num.length())).concat(".rgb");


            BufferedImage img = new BufferedImage(352,288,BufferedImage.TYPE_INT_RGB);
            ImageDisplay.readImageRGB(352,288,current_path,img);
            List<int[]> colors = MMCQ.compute(img, 10);
            int[] first_color = colors.get(0);

            int array_idx = (idx-1)/interval;
            result[array_idx] = first_color;
//            ImageDisplay.show_single_Ims(img);
        }}catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //current_color 15*3, total_result 7*40*3
    public double[] compare_color(int[][] current_color,int[][][] total_result){
        double[] possiblity = new double[7];
        for(int i=0;i<current_color.length;i++){
//            System.out.println(i);
            int[] mincount_per_catagory = new int[7];
            for(int j=0;j<7;j++){
                for(int k=0;k<40;k++){
                    int temp = compare_int_array(current_color[i],total_result[j][k]);
                    mincount_per_catagory[j] += temp;
                }
            }

            int max = mincount_per_catagory[0];
            for (int n = 1; n < mincount_per_catagory.length; n++)
                if (mincount_per_catagory[n] > max)
                    max = mincount_per_catagory[n];

            for(int j=0;j<7;j++){
                possiblity[j] += (double)mincount_per_catagory[j]/(double)max;
            }
        }
        double max = possiblity[0];
        for (int n = 1; n < possiblity.length; n++)
            if (possiblity[n] > max)
                max = possiblity[n];
        for(int j=0;j<7;j++){
            possiblity[j] = 1-possiblity[j]/max;

        }
        System.out.println(Arrays.toString(possiblity));
        return possiblity;
    }

    public int compare_int_array(int[] a,int[] b){
        int total = 0;
        for(int i= 0;i<a.length;i++){
            total+= Math.abs(a[i] - b[i]);
        }
        return total;
    }

    //int[7][40][3]
    public void save_to_file(int[][][] input, String save_to_file_name){


        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 7; i++)//for each row
        {
            for(int j = 0; j < 40; j++)//for each column
            {
                for(int k=0;k<3;k++){
                    builder.append(input[i][j][k]);//append to the output string
                    builder.append(",");
                }
                builder.append("\n");
            }
        }
//        builder.append("\n");
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(save_to_file_name));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[][][] read_txt(String filename){

        int[][][] total_result = new int[7][40][3];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            for(int i = 0; i < 7; i++)//for each row
            {
                for(int j = 0; j < 40; j++)//for each column
                {
                    String line = reader.readLine();
                    String[] numbers = line.split(",");
                    for(int k=0;k<3;k++){
                        total_result[i][j][k] = Integer.parseInt(numbers[k]);
                    }
                }
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return total_result;
    }
}
