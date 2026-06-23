package rudMad.Threads;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Scanner;


public class WriterThread implements Runnable {

    private final BufferedWriter out;
    private final Scanner scanner;
    private final Socket socket;

    public WriterThread(BufferedWriter out, Scanner scanner, Socket socket) {
        this.out = out;
        this.scanner = scanner;
        this.socket = socket;
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
