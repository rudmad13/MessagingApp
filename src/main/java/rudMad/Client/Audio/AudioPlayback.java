package rudMad.Client.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayback {

    private final SourceDataLine speakers;

    public AudioPlayback(AudioFormat format) throws LineUnavailableException{

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        speakers = (SourceDataLine) AudioSystem.getLine(info);

        speakers.open(format);
    }


    public void start() {
        speakers.start();
    }

    public void play(byte[] buffer, int length){

        int offset = 0;

        while(offset < length){
            int written = speakers.write(buffer, offset, length);

            if (written <= 0) {
                break; 
            }

            offset += written;


        }
    }

    public void close(){
        speakers.stop();
        speakers.close();
    }

}
