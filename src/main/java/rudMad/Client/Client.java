package rudMad.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import rudMad.Client.Commands.CommandHandler;
import rudMad.Client.Threads.ServerListener;
import rudMad.Client.Threads.WriterThread;
import rudMad.Client.UDP.UdpConnection;





public class Client {

    private TlsConnection connection;
    private String username;
    private UdpConnection udpConnection;
    private final Scanner input;

    private final CommandHandler commandHandler;
    


    public Client(TlsConnection connection, String username) {
        
        this.connection = connection;
        this.username = username;
        this.input = new Scanner(System.in);
        this.commandHandler = new CommandHandler(this);
    }


    public void setTlsConnection(TlsConnection connection){
        this.connection = connection;

    }

    public void setUdpConnection(UdpConnection connection){
        this.udpConnection = connection;
    
    }

    public String getUsername(){
        return this.username;

    }

    public UdpConnection getUdpConnection(){
        return this.udpConnection;
    }

    public void closeUdpConnection(){

        if(udpConnection == null){
            return;
        }

        udpConnection.close();
    }

    public void closeTlsConnection(){

        try{
        connection.close();
        } catch(IOException e){
            System.err.println("Error closing connection: " + e.getMessage());

        }
    }


    public void sendMessage(String message) throws IOException{
        BufferedWriter out = connection.getOut();

        out.write(message);
        out.newLine();
        out.flush();

    }
    
    /**
     * This method starts the client by creating and starting two threads:
     * 1. ServerListener thread: Listens for messages from the server and prints them
     * 2. WriterThread: Reads user input from the console and sends it to the server
     */
    public void start() {

        ServerListener listener = new ServerListener(connection.getIn());
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