package javawave;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public MediaPlayer(){
        int width = 352;
        int height = 288;

        ImageDisplay ren = new ImageDisplay();


        JFrame frame = new JFrame();
        JPanel[] panels = new JPanel[4];

        frame.setLayout(new GridLayout(2, 2));

        for (int i = 0; i < 4; i++) {
            panels[i] = new JPanel();
            panels[i].setSize(400, 400);
            panels[i].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
            frame.add(panels[i]);
        }

        JTextArea query_name = new JTextArea("Qurey Vedio");
        panels[0].add(query_name);

        JTextArea matched_videos = new JTextArea("Matched Videos");
        panels[1].add(matched_videos);

        //query video
        JLabel vedio_label1 = new JLabel();
        panels[2].add(vedio_label1);
        BufferedImage[] v1 = ren.showIms("resources/query/first/first", 150, width, height);
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
        BufferedImage[] v2 = ren.showIms("resources/flowers/flowers", 600, width, height);
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

        //        frame.pack();
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        m1 = new PlayMusic("resources/query/first/first.wav");
        m2 = new PlayMusic("resources/flowers/flowers.wav");


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
        MediaPlayer m = new MediaPlayer();
    }
}