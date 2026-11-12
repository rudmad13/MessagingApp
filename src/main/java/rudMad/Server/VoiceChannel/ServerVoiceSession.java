package rudMad.Server.VoiceChannel;

public class ServerVoiceSession {

    private VoiceRelay voiceRelay;
    private Thread packetForwarder;

    public ServerVoiceSession(VoiceRelay voiceRelay){
        this.voiceRelay = voiceRelay;
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
