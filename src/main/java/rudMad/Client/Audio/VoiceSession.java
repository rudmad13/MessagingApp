package rudMad.Client.Audio;

import javax.sound.sampled.LineUnavailableException;


public class VoiceSession {

    private final UdpAudioSender sender;
    private final AudioReceiver receiver;

    private Thread senderThread;
    private Thread receiverThread;

    public VoiceSession(UdpAudioSender sender, AudioReceiver receiver) throws LineUnavailableException{
        this.sender = sender;
        this.receiver = receiver;
    }




    
    public void start(){



        senderThread = new Thread(sender);
        receiverThread = new Thread(receiver, "Playback");

    }


    public void stop(){
        senderThread.interrupt();
        receiverThread.interrupt();
    }



}
