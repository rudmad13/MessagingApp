package rudMad.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import rudMad.Server.Server;

public final class LoggingConfig {

    private static final Logger SERVER_LOGGER = Logger.getLogger(Server.class.getName());
    private static FileHandler fileHandler;

    private LoggingConfig() {
    }

    /** Configures one shared file handler for server events in this process. */
    public static synchronized void configureServerLogging() throws IOException {
        if (fileHandler != null) {
            return;
        }

        Files.createDirectories(Path.of("logs"));
        FileHandler handler = new FileHandler("logs/server-%g.log", 1_048_576, 3, true);
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.INFO);

        SERVER_LOGGER.setLevel(Level.INFO);
        SERVER_LOGGER.addHandler(handler);
        SERVER_LOGGER.setUseParentHandlers(false);
        fileHandler = handler;
    }
}
