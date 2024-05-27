package b;

import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Settings extends JFrame {
    private static Settings instance;
    private static Clip clip;
    private static FloatControl volumeControl;
    private static float volume = 0.5f;

    private Settings() {
    	
    	ImageIcon icon = new ImageIcon("teduIcon.png");
		setIconImage(icon.getImage());
        setTitle("Settings");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.addChangeListener(e -> {
            volume = volumeSlider.getValue() / 100.0f;
            setVolume(volume);
        });
        panel.add(new JLabel("Main:"));
        volumeSlider.setBackground(new Color(125, 240, 255));
        panel.add(volumeSlider);
        panel.setBackground(new Color(125, 240, 255));
        add(panel);
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public static void loadMusic(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (clip != null && clip.isRunning()) {
            return;
        }

        if (clip != null) {
            clip.stop();
            clip.close();
        }

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(volume);
        playMusic();
    }

    private static void playMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private static void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = (range * volume) + min;
            volumeControl.setValue(gain);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
