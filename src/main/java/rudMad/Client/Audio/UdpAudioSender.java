package rudMad.Client.Audio;


import java.io.IOException;

import rudMad.Client.UDP.UdpConnection;

public class UdpAudioSender implements Runnable{


    private final AudioCapture audioCapture;
    private final UdpConnection connection;

    public UdpAudioSender(AudioCapture capture, UdpConnection connection){
        this.audioCapture = capture;
        this.connection = connection; 
    }

    public void sendAudio()  throws IOException{

        byte[] buffer = new byte[640];

        audioCapture.start();


        while (!Thread.currentThread().isInterrupted()){
            int bytesRead = audioCapture.read(buffer);

            if (bytesRead > 0){
                connection.send(buffer, bytesRead);
            }
        }


    }


    @Override
    public void run() {
        try{
            sendAudio();
        } catch (IOException e){
            System.err.println("Unable to send audio: " + e.getMessage());
        }
    }

}
