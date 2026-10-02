package rudMad.Client;

import rudMad.Threads.ServerListener;
import rudMad.Threads.WriterThread;
import java.util.Scanner;





public class Client {

    private ClientConnection connection;
    private String username;
    private final Scanner input;
    


    public Client(ClientConnection connection, String username) {
        this(connection, username, new Scanner(System.in));
    }

    public Client(ClientConnection connection, String username, Scanner input) {
        this.input = input;
        this.connection = connection;
        this.username = username;
    }

    
    /**
     * This method starts the client by creating and starting two threads:
     * 1. ServerListener thread: Listens for messages from the server and prints them
     * 2. WriterThread: Reads user input from the console and sends it to the server
     */
    public void start() {

        ServerListener listener = new ServerListener(connection.getIn());
        WriterThread writer = new WriterThread(connection, input);

        Thread listenerThread = new Thread(listener);
        Thread writerThread = new Thread(writer);

        listenerThread.start();
        writerThread.start();
    }

}