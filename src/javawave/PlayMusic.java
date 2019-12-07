package javawave;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class PlayMusic {

    Clip clip;
    public PlayMusic(String filename){
        try{
            File muiscpath = new File(filename);
            if (muiscpath.exists()){

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(muiscpath);
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInput);
//                clip.start();
//                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }else {
                System.out.println("Cant find file");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void start(){
        this.clip.start();
    }

    public void stop(){
        this.clip.stop();
        this.clip.setMicrosecondPosition(0);
    }

    public void pause(){
        this.clip.stop();
    }

    public static void main(String[] args){

    }
}
