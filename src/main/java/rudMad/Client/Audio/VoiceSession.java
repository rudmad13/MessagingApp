package rudMad.Client.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import rudMad.Client.UDP.UdpConnection;

public class VoiceSession {

    private final UdpAudioSender sender;
    private final AudioReceiver receiver;

    private Thread senderThread;
    private Thread receiverThread;

    public VoiceSession(AudioFormat audioFormat,UdpConnection connection) throws LineUnavailableException{
        this.sender = new UdpAudioSender(audioFormat, connection);
        this.receiver = new AudioReceiver(audioFormat, connection);
    }





    public void start(){

        senderThread = new Thread(sender);
        receiverThread = new Thread(receiver, "Playback");


    }




}
