package javawave;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.CheckedInputStream;

public class MediaPlayer {
    private void sleep(long latency) {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int v1_frame = 0;
    private int v2_frame = 0;
    private boolean v1_start = false;
    private boolean v2_start = false;

    PlayMusic m1,m2;
    BufferedImage[] v1, v2;
    JPanel pm2 = new JPanel();
    JFreeChart[] image_bar = new JFreeChart[3];
    ChartPanel cc;



    public MediaPlayer(String[] args){
        int width = 352;
        int height = 288;
        String[] path = new String[7];
        path[0] = "resources/data/flowers.txt";
        path[1] = "resources/data/interview.txt";
        path[2] = "resources/data/movie.txt";
        path[3] = "resources/data/musicvideo.txt";
        path[4] = "resources/data/sports.txt";
        path[5] = "resources/data/starcraft.txt";
        path[6] = "resources/data/traffic.txt";

        String[] vedio_path = new String[7];
        vedio_path[0] = "resources/flowers/flowers";
        vedio_path[1] = "resources/interview/interview";
        vedio_path[2] = "resources/movie/movie";
        vedio_path[3] = "resources/musicvideo/musicvideo";
        vedio_path[4] = "resources/sports/sports";
        vedio_path[5] = "resources/starcraft/starcraft";
        vedio_path[6] = "resources/traffic/traffic";

        String[] music_path = new String[7];
        music_path[0] = "resources/flowers/flowers.wav";
        music_path[1] = "resources/interview/interview.wav";
        music_path[2] = "resources/movie/movie.wav";
        music_path[3] = "resources/musicvideo/musicvideo.wav";
        music_path[4] = "resources/sports/sports.wav";
        music_path[5] = "resources/starcraft/starcraft.wav";
        music_path[6] = "resources/traffic/traffic.wav";

        //read the input file

        String query_file_path = "resources/query/second/second";
        String query_music_file_path = "resources/query/second/second.wav";
//                new StringBuilder(query_file_path).append(".wav").toString();
        String tempfile = "tempquerydata.txt";
        File file = new File(tempfile);
        try{
            boolean result = Files.deleteIfExists(file.toPath());
        }catch (IOException e){
            System.out.println("cannot delete file");
        }

        //start process data
        ImageProcess imp = new ImageProcess();
        imp.convert_video(10,150,query_file_path,tempfile);
        double[][] result = imp.test(tempfile, path);

        double[] possibility = result[7];
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for(int i=0;i<7;i++){
            map.put(possibility[i],i);
        }
        Arrays.sort(possibility);
        int[] video_rank = new int[3];
        video_rank[0] = map.get(possibility[6]);
        video_rank[1] = map.get(possibility[5]);
        video_rank[2] = map.get(possibility[4]);

        double[][] graph_chart = new double[3][40];
        graph_chart[0] = result[video_rank[0]];
        graph_chart[1] = result[video_rank[1]];
        graph_chart[2] = result[video_rank[2]];



        //configurate window setup
        ImageDisplay ren = new ImageDisplay();

        JFrame frame = new JFrame();
        JPanel[] panels = new JPanel[4];

        frame.setLayout(new GridLayout(2, 2));

        for (int i = 0; i < 4; i++) {
            panels[i] = new JPanel();
            panels[i].setPreferredSize(new Dimension(400,400));
            panels[i].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
            frame.add(panels[i]);
        }

        JTextArea query_name = new JTextArea("Qurey Video");
        panels[0].add(query_name);

        //video selection
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        String temp = vedio_path[video_rank[0]].split("/")[2];
        String labeltext1 = new StringBuilder(temp).append(".rgb       ").append(df.format(possibility[6] * 100)).append("%").toString();
        JButton match1 = new JButton(labeltext1);
        match1.setPreferredSize(new Dimension(200,30));

        temp = vedio_path[video_rank[1]].split("/")[2];
        String labeltext2 = new StringBuilder(temp).append(".rgb       ").append(df.format(possibility[5] * 100)).append("%").toString();
        JButton match2 = new JButton(labeltext2);
        match2.setPreferredSize(new Dimension(200,30));


        temp = vedio_path[video_rank[2]].split("/")[2];
        String labeltext3 = new StringBuilder(temp).append(".rgb       ").append(df.format(possibility[4] * 100)).append("%").toString();
        JButton match3 = new JButton(labeltext3);
        match3.setPreferredSize(new Dimension(200,30));

        match1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2 = ren.showIms(vedio_path[video_rank[0]], 600, width, height);
                MediaPlayer.this.m2 = new PlayMusic(music_path[video_rank[0]]);

                cc = new ChartPanel(image_bar[0]);
                cc.setPreferredSize(new Dimension(350,180));
                pm2.removeAll();
                pm2.add(cc);
                pm2.revalidate();
                pm2.repaint();

            }
        });

        match2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2 = ren.showIms(vedio_path[video_rank[1]], 600, width, height);
                MediaPlayer.this.m2 = new PlayMusic(music_path[video_rank[1]]);

                cc = new ChartPanel(image_bar[1]);
                cc.setPreferredSize(new Dimension(350,180));
                pm2.removeAll();
                pm2.add(cc);
                pm2.revalidate();
                pm2.repaint();


            }
        });

        match3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2 = ren.showIms(vedio_path[video_rank[2]], 600, width, height);
                MediaPlayer.this.m2 = new PlayMusic(music_path[video_rank[2]]);

                cc = new ChartPanel(image_bar[2]);
                cc.setPreferredSize(new Dimension(350,180));
                pm2.removeAll();
                pm2.add(cc);
                pm2.revalidate();
                pm2.repaint();


            }
        });




        image_bar[0] = Chart.Generate_chart(graph_chart[0]);
        image_bar[1] = Chart.Generate_chart(graph_chart[1]);
        image_bar[2] = Chart.Generate_chart(graph_chart[2]);

//        ImageIcon image_match1 = new ImageIcon("ggg.jpg");
//        JLabel label_match1 = new JLabel();
//        label_match1.setIcon(image_match1);
//        JPanel panel_match1 = new JPanel();
//        panel_match1.add(label_match1);



        panels[1].setLayout(new GridLayout(2,1));
        JPanel pm1 = new JPanel();
        pm1.add(match1);
        pm1.add(match2);
        pm1.add(match3);



        panels[1].add(pm1);
        panels[1].add(pm2);


//        cc = new ChartPanel(image_bar[0]);
//        cc.setPreferredSize(new Dimension(300,100));
//        pm2.add(cc);



        //query video
        JLabel vedio_label1 = new JLabel();
        panels[2].add(vedio_label1);
        v1 = ren.showIms(query_file_path, 150, width, height);
        vedio_label1.setIcon(new ImageIcon(v1[0]));
        JButton v1_button_start = new JButton("start");
        JButton v1_button_pause = new JButton("pause");
        JButton v1_button_stop = new JButton("stop");
        panels[2].add(v1_button_start);
        v1_button_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v1_start = true;
                MediaPlayer.this.m1.start();
            }
        });
        panels[2].add(v1_button_pause);
        v1_button_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v1_start = false;
                MediaPlayer.this.m1.pause();
            }
        });
        panels[2].add(v1_button_stop);
        v1_button_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v1_frame = 0;
                MediaPlayer.this.v1_start = false;
                MediaPlayer.this.m1.stop();
                vedio_label1.setIcon(new ImageIcon(v1[0]));
            }
        });


        //reference video
        JLabel vedio_label2 = new JLabel();
        panels[3].add(vedio_label2);

        vedio_label2.setIcon(new ImageIcon(v1[0]));
        JButton v2_button_start = new JButton("start");
        JButton v2_button_pause = new JButton("pause");
        JButton v2_button_stop = new JButton("stop");
        panels[3].add(v2_button_start);
        v2_button_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_start = true;
                if(MediaPlayer.this.m2 != null) MediaPlayer.this.m2.start();
            }
        });
        panels[3].add(v2_button_pause);
        v2_button_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_start = false;
                if(MediaPlayer.this.m2 != null) MediaPlayer.this.m2.pause();
            }
        });
        panels[3].add(v2_button_stop);
        v2_button_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_frame = 0;
                MediaPlayer.this.v2_start = false;
                if(MediaPlayer.this.m2 != null) MediaPlayer.this.m2.stop();
                if(MediaPlayer.this.v2 != null) vedio_label2.setIcon(new ImageIcon(MediaPlayer.this.v2[0]));
            }
        });
//        Points points = new Points();
//        panels[3].add(points);


        //        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        m1 = new PlayMusic(query_music_file_path);


        while (true) {

            if(v1_start){
                vedio_label1.setIcon(new ImageIcon(v1[v1_frame]));
                if(v1_frame == 149){
                    v1_start = false;
                    v1_frame = 0;
                    MediaPlayer.this.m1.stop();
                }else v1_frame +=1;

            }

            if(v2_start){
                if(MediaPlayer.this.v2 != null) vedio_label2.setIcon(new ImageIcon(MediaPlayer.this.v2[v2_frame]));
                if(v2_frame == 599){
                    v2_start = false;
                    v2_frame = 0;
                    MediaPlayer.this.m2.stop();
                }else v2_frame +=1;

            }

            sleep((long) 33.33);
        }
    }

    public static void main(String[] args) {
        MediaPlayer m = new MediaPlayer(args);
    }
}

class Points extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.red);

        for (int i = 0; i <= 100000; i++) {
            Dimension size = getSize();
            int w = size.width;
            int h = size.height;

            Random r = new Random();
            int x = Math.abs(r.nextInt()) % w;
            int y = Math.abs(r.nextInt()) % h;
            g2d.drawLine(x, y, x, y);
        }
    }
}