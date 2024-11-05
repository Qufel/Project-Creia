package engine.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioClip {

    Clip clip = null;
    private FloatControl gainControl;

    public AudioClip(String filename) throws RuntimeException {

        try {

            InputStream stream = AudioClip.class.getResourceAsStream(filename);
            InputStream bufferedStream = new BufferedInputStream(stream);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(bufferedStream);

            AudioFormat format = audioInput.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false
                    );

            AudioInputStream decodedAudioInput = AudioSystem.getAudioInputStream(decodeFormat, audioInput);
            clip = AudioSystem.getClip();
            clip.open(decodedAudioInput);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public void play() {
        if (clip == null)
            return;

        stop();
        clip.setFramePosition(0);

        while (!clip.isRunning()) {
            clip.start();
        }

    }

    public void stop() {

        if (clip.isRunning()) {
            clip.stop();
        }

    }

    public void close() {
        stop();
        clip.drain();
        clip.close();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }

    public void setVolume(float volume) {
        gainControl.setValue(volume);
    }

    public float getVolume() {
        return gainControl.getValue();
    }

    public boolean isPlaying() {
        return clip.isRunning();
    }
}
