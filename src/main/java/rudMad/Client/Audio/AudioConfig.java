package rudMad.Client.Audio;

import javax.sound.sampled.AudioFormat;

public final class AudioConfig {

    public static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true , false);

    private AudioConfig(){};

}
