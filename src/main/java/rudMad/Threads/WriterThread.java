package rudMad.Threads;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Scanner;

import rudMad.Client.ClientConnection;


public class WriterThread implements Runnable {

    private final BufferedWriter out;
    private final Scanner scanner;
    private final Socket socket;

    public WriterThread(ClientConnection connection) {
        this.out = connection.getOut();
        this.scanner = new Scanner(System.in);
        this.socket = connection.getSocket();
    }

    @Override
    public void run() {
        try{
            String line;
            while (!socket.isClosed() && (line = scanner.nextLine()) != null) {
                out.write(line);
                out.newLine();
                out.flush();

                if (line.equalsIgnoreCase("quit()")) {
                    System.out.println("Disconnecting from the server...");
                    socket.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Connection has been closed!");
        }
    }


}
