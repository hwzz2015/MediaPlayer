package javawave;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.zip.CheckedInputStream;

public class MediaPlayer {
    private static void playmusic() {
        try {
            File muiscpath = new File("resources/flowers/flowers.wav");
            if (muiscpath.exists()) {

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(muiscpath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
//				clip.loop(Clip.LOOP_CONTINUOUSLY);

            } else {
                System.out.println("Cant find music file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
    static String filename_v1 = "resources/query_videos_2/SeenInexactMatch/HQ1/HQ1_"; //150
    static String filename_v2 = "resources/traffic/traffic"; //600
    static String filename_m1 = "resources/query/first/first.wav";
    static String filename_m2 = "resources/flowers/flowers.wav";

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

        //to be use
//        ImageProcess imp = new ImageProcess();
//        imp.convert_video(10,150,args[0],"tempquerydata.txt");
//        double[][] result = imp.test("tempquerydata.txt", path);


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
        JButton match1 = new JButton("match1");
        match1.setPreferredSize(new Dimension(80,50));
        JLabel matched_text1 = new JLabel("matched_text");
        matched_text1.setOpaque(true);
        JButton match2 = new JButton("match2");
        match2.setPreferredSize(new Dimension(80,50));
        JLabel matched_text2 = new JLabel("matched_text");
        matched_text2.setOpaque(true);
        JButton match3 = new JButton("match3");
        match3.setPreferredSize(new Dimension(80,50));
        JLabel matched_text3 = new JLabel("matched_text");
        matched_text3.setOpaque(true);

        match1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        panels[1].setLayout(new GridLayout(3,1));
        JPanel pm1 = new JPanel();
        pm1.add(match1);
        pm1.add(matched_text1);
        JPanel pm2 = new JPanel();
        pm2.add(match2);
        pm2.add(matched_text2);
        JPanel pm3 = new JPanel();
        pm3.add(match3);
        pm3.add(matched_text3);

        panels[1].add(pm1);
        panels[1].add(pm2);
        panels[1].add(pm3);


        //query video
        JLabel vedio_label1 = new JLabel();
        panels[2].add(vedio_label1);
        BufferedImage[] v1 = ren.showIms(filename_v1, 150, width, height);
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
        BufferedImage[] v2 = ren.showIms(filename_v2, 600, width, height);
        vedio_label2.setIcon(new ImageIcon(v2[0]));
        JButton v2_button_start = new JButton("start");
        JButton v2_button_pause = new JButton("pause");
        JButton v2_button_stop = new JButton("stop");
        panels[3].add(v2_button_start);
        v2_button_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_start = true;
                MediaPlayer.this.m2.start();
            }
        });
        panels[3].add(v2_button_pause);
        v2_button_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_start = false;
                MediaPlayer.this.m2.pause();
            }
        });
        panels[3].add(v2_button_stop);
        v2_button_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.this.v2_frame = 0;
                MediaPlayer.this.v2_start = false;
                MediaPlayer.this.m2.stop();
                vedio_label2.setIcon(new ImageIcon(v2[0]));
            }
        });
//        Points points = new Points();
//        panels[3].add(points);


        //        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        m1 = new PlayMusic(filename_m1);
        m2 = new PlayMusic(filename_m2);


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
                vedio_label2.setIcon(new ImageIcon(v2[v2_frame]));
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