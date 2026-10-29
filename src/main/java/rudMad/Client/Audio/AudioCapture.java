package rudMad.Client.Audio;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioCapture {

    private final TargetDataLine microphone;

    public AudioCapture(AudioFormat format) throws LineUnavailableException {

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        microphone = (TargetDataLine) AudioSystem.getLine(info);

        microphone.open(format);//Grabs audio device
    }

    public void start(){
        microphone.start();
    }

    public int read(byte[] buffer){
        return microphone.read(buffer, 0, buffer.length);
    }


    public void close(){
        microphone.stop();
        microphone.close();
    }
}