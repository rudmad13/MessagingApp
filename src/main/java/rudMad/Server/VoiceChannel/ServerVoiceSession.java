package rudMad.Server.VoiceChannel;

import java.net.InetSocketAddress;

public class ServerVoiceSession {

    private VoiceRelay voiceRelay;
    private Thread packetForwarder;

    public ServerVoiceSession(VoiceRelay voiceRelay){
        this.voiceRelay = voiceRelay;
    }


    /**
     * @return int - Returns the port number the voice session is listening on
     */
    public int getPort(){
        return voiceRelay.getPort();
    }


    public void addParticipant(String username, InetSocketAddress address){
        voiceRelay.addParticipant(username, address);
    }

    /**
     * Removes the user from the voice session
     * @param username - Representing the username that wants to join the voice session
     */
    public void removeParticipant(String username){
        voiceRelay.removeParticipant(username);
    }
    

    /**
     * Init the thread and do the start method
     */
    public void start(){

        packetForwarder = new Thread(voiceRelay);

        packetForwarder.start();

    }


    /**
     * This is will interupt the thread and then call voicerelay to close its socket
     * Unblocking the recieve method for the socket.
     */
    public void stop(){
        if(packetForwarder != null){
            packetForwarder.interrupt();
            voiceRelay.close();
        }

    }
}
