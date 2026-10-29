package rudMad.Client.Audio;

import java.io.IOException;

import rudMad.Client.UDP.UdpConnection;

public class AudioReceiver implements  Runnable{

    private final UdpConnection connection;
    private final AudioPlayback speakers;

    public AudioReceiver(AudioPlayback speakers, UdpConnection connection){
        this.connection = connection;
        this.speakers = speakers;
    }

    @Override
    public void run() {
        try{
            playAudio();
        } catch (IOException e ){
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }


    public void playAudio() throws IOException{

        byte[] buffer  = new byte[640];

        speakers.start();

        while(!Thread.currentThread().isInterrupted()){

            int bytesReceived = connection.receive(buffer);

            if(bytesReceived > 0){
                speakers.play(buffer, bytesReceived);
            }
        }
    }

    

}
