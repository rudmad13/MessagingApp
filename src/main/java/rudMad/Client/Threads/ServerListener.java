package rudMad.Client.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import rudMad.Client.ServerResponses.ServerResponseHandler;

public class ServerListener implements Runnable {
    private final BufferedReader in;
    private final ServerResponseHandler responseHandler;

    public ServerListener(BufferedReader in, ServerResponseHandler responseHandler) {
        this.in = in;
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    responseHandler.handle(message);
                } else {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection has been closed!");
        }
    }
}
