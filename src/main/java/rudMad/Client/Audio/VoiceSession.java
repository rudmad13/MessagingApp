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




    /**
     * Starts the capture of the microphone and the playback for the speakers.
     */
    public void start(){



        senderThread = new Thread(sender);
        receiverThread = new Thread(receiver, "Playback");

        senderThread.start();
        receiverThread.start();

    }


    /**
     * Interrupts the thread and stops the capture and playback.
     */
    public void stop(){
        senderThread.interrupt();
        receiverThread.interrupt();
        sender.stopCapture();
        receiver.stopPlayback();
    }



}
