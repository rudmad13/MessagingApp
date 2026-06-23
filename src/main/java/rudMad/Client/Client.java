package rudMad.Client;

import rudMad.Threads.ServerListener;
import rudMad.Threads.WriterThread;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.net.Socket;



public class Client {

    private Socket client;
    private BufferedWriter out;// To Server
    private BufferedReader in;// From Server
    private ServerListener listener;
    private WriterThread writer;
    private String username;


    public Client(ClientConnection connection, String username, Scanner scanner) {
        this.client = connection.getSocket();
        this.out = connection.getOut();
        this.in = connection.getIn();
        this.username = username;
        this.listener = new ServerListener(in);
        this.writer = new WriterThread(out, scanner, client);
        
    }

    
    /**
     * This method starts the client by creating and starting two threads:
     * 1. ServerListener thread: Listens for messages from the server and prints them
     * 2. WriterThread: Reads user input from the console and sends it to the server
     */
    public void start() {

        Thread listenerThread = new Thread(listener);
        Thread writerThread = new Thread(writer);

        listenerThread.start();
        writerThread.start();
    }

}