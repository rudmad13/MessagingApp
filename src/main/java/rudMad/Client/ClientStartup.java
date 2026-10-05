package rudMad.Client;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class ClientStartup {

    private final Scanner kb;
    private final TlsConfig config;
    private String username;

    public ClientStartup() {
        this(new Scanner(System.in));
    }

    public ClientStartup(Scanner input) {
        this.kb = input;
        this.config = new TlsConfig();
    }

    /** Returns the username from the most recent successful connection. */
    public String getUsername() {
        return username;
    }

    public TlsConnection start() throws IOException {
        String path = readConnectionInput("Enter absolute path of the truststore file: ");

        String password = readConnectionInput("Enter the truststore password: ");

        configureSsl(path, password);
        return connect();
    }

    /**
     * Prompts for connection details and retries after a failed attempt.
     * Each retry asks for the server address, port, and username again.
     * Configure SSL before calling this method.
     *
     * @return the connection after the username handshake succeeds
     * @throws IOException if console input ends before a connection is established
     */
    public TlsConnection connect() throws IOException {
        TlsConnection connection = null;

        while (connection == null) {

            String ip = readConnectionInput("Enter the IP address of the server: ").trim();
            String portInput = readConnectionInput("Enter the port of the server: ").trim();
            String username = readConnectionInput("Enter a username to chat on the server: ");

            try {
                int port = Integer.parseInt(portInput);
                if (ip.isBlank() || username.isBlank()) {
                    throw new IllegalArgumentException("Server address and username cannot be blank.");
                }
                if (port < 1 || port > 65535) {
                    throw new IllegalArgumentException("Port must be between 1 and 65535.");
                }
                connection = ClientConnectionFactory.connect(ip, username, port);
                this.username = username;
            } catch (NumberFormatException e) {
                System.err.println("Port must be a whole number. Enter the connection details again.");
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Unable to connect: " + e.getMessage());
                System.out.println("Enter the connection details again.");
            }
        }

        return connection;
    }

    private String readConnectionInput(String prompt) throws IOException {
        System.out.println(prompt);
        if (!kb.hasNextLine()) {
            throw new IOException("Input ended before a connection was established.");
        }
        return kb.nextLine();
    }

    /**
     * Configures the truststore, prompting for a new path and password after
     * each failed attempt. Call before creating an SSL connection.
     *
     * @param path initial truststore file path
     * @param password initial truststore password
     * @throws IOException if console input ends before configuration succeeds
     */
    public void configureSsl(String path, String password) throws IOException {
        boolean configured = false;

        while (!configured) {
            try {
                if (path.isBlank()) {
                    throw new IOException("The truststore path cannot be blank.");
                }
                config.configureTrustStore(path, password);
                configured = true;
            } catch (IOException | GeneralSecurityException | InvalidPathException e) {
                System.err.println("Unable to configure the truststore: " + e.getMessage());
                System.out.println("Enter a new absolute path of the truststore file: ");
                if (!kb.hasNextLine()) {
                    throw new IOException("Input ended before the truststore was configured.", e);
                }
                path = kb.nextLine();

                System.out.println("Enter the truststore password: ");
                if (!kb.hasNextLine()) {
                    throw new IOException("Input ended before the truststore was configured.", e);
                }
                password = kb.nextLine();
            }
        }
    }
}
