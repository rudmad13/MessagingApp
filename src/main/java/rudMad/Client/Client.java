package rudMad.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import rudMad.Client.Commands.CommandHandler;
import rudMad.Client.ServerResponses.ServerResponseHandler;
import rudMad.Client.Threads.ServerListener;
import rudMad.Client.UDP.UdpConnection;





public class Client {

    private TlsConnection connection;
    private String username;
    private UdpConnection udpConnection;
    private final Scanner input;

    private final CommandHandler commandHandler;
    private final ServerResponseHandler responseHandler;
    


    public Client(TlsConnection connection, String username) {
        
        this.connection = connection;
        this.username = username;
        this.input = new Scanner(System.in);
        this.commandHandler = new CommandHandler(this);
        this.responseHandler = new ServerResponseHandler(this);
    }


    public void setTlsConnection(TlsConnection connection){
        this.connection = connection;

    }

    public synchronized void setUdpConnection(UdpConnection connection){
        this.udpConnection = connection;
    
    }

    public String getUsername(){
        return this.username;

    }

    public synchronized UdpConnection getUdpConnection(){
        return this.udpConnection;
    }

    public synchronized void closeUdpConnection(){

        if(udpConnection == null){
            return;
        }

        udpConnection.close();
        udpConnection = null;
    }

    public void closeTlsConnection(){

        try{
        connection.close();
        } catch(IOException e){
            System.err.println("Error closing connection: " + e.getMessage());

        }
    }


    public synchronized void sendMessage(String message) throws IOException{
        BufferedWriter out = connection.getOut();

        out.write(message);
        out.newLine();
        out.flush();

    }
    
    public synchronized boolean openUDPPort(int localPort) {
        if (udpConnection != null) {
            System.err.println("A UDP connection is already open.");
            return false;
        }

        try {
            udpConnection = new UdpConnection(localPort);
            return true;
        } catch (IOException e) {
            System.err.println("Error opening UDP port: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean ConnectToServer(int remotePort) {
        if (udpConnection == null) {
            System.err.println("Open a local UDP port before connecting to the server.");
            return false;
        }

        try {
            udpConnection.connect(connection.getHost(), remotePort);
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting UDP socket: " + e.getMessage());
            return false;
        }
    }
    /**
     * This method starts the client by creating and starting two threads:
     * 1. ServerListener thread: Listens for messages from the server and prints them
     * 2. WriterThread: Reads user input from the console and sends it to the server
     */
    public void start() {

        ServerListener listener = new ServerListener(connection.getIn(), responseHandler);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        while(listenerThread.isAlive() && input.hasNextLine()){
            String message = input.nextLine();

            if (message.startsWith("/")){
                commandHandler.handle(message);
            }else{
                try{
                    sendMessage(message);

                } catch (IOException e){
                    System.err.println("Error: " + e.getMessage());
                }
            }

        }
    }

}
