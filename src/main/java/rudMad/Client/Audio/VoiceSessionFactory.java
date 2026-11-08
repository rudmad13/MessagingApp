package rudMad.Client.Audio;

import javax.sound.sampled.LineUnavailableException;

import rudMad.Client.UDP.UdpConnection;

public class VoiceSessionFactory {

    public static VoiceSession create(UdpConnection connection) throws LineUnavailableException{


        UdpAudioSender sender = null;
        AudioCapture microphone = null;

        AudioPlayback speakers= null;
        AudioReceiver receiver = null;

        //Create the UdpAudioSender
        try{
            microphone = new AudioCapture(AudioConfig.FORMAT);
            sender = new UdpAudioSender(microphone, connection);

            speakers = new AudioPlayback(AudioConfig.FORMAT);
            receiver = new AudioReceiver(speakers, connection);

            return new VoiceSession(sender, receiver);

        } catch (LineUnavailableException e){
            System.err.println("Error with microphone: " +  e.getMessage());
        
            if(microphone != null){
                microphone.close();
            }

            if(speakers != null){
                speakers.close();
            }

            throw e;
        }

    }

}
