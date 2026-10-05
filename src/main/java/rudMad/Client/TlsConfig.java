package rudMad.Client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class TlsConfig {




    /**
     * Validates a PKCS12 truststore and then sets the JVM truststore properties.
     * Call before the default SSL context is initialized; changing properties
     * does not reconfigure an already initialized context.
     *
     * @param path path to the truststore file
     * @param password password used to verify the truststore's integrity
     * @throws IOException if the file cannot be read, its contents are invalid,
     *         or the password is incorrect
     * @throws KeyStoreException if no provider supports the PKCS12 keystore type
     * @throws GeneralSecurityException if an integrity-check algorithm is
     *         unavailable or a certificate cannot be loaded
     */
    public static void configureTrustStore(String path, String password) throws IOException, GeneralSecurityException, KeyStoreException{

        validateTrustStore(path, password.toCharArray());

        //Set the system properties for the SSL configs
        System.setProperty("javax.net.ssl.trustStore", path);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");


    }


    /**
     * Checks that the file can be loaded as a PKCS12 truststore with the supplied
     * password. The input stream is closed whether loading succeeds or fails.
     * This method does not configure SSL or verify that the store trusts a
     * particular server.
     *
     * @param path path to the truststore file
     * @param password password used to verify integrity, or {@code null} to skip
     *        the integrity check
     * @throws IOException if the file cannot be read, its contents are invalid,
     *         or the password is incorrect
     * @throws NoSuchAlgorithmException if the integrity-check algorithm is unavailable
     * @throws CertificateException if a certificate in the store cannot be loaded
     * @throws KeyStoreException if no provider supports the PKCS12 keystore type
     */
    private static void  validateTrustStore(String path, char[] password)throws IOException, NoSuchAlgorithmException, CertificateException,KeyStoreException{

        KeyStore trustStore = KeyStore.getInstance("PKCS12");

        try (InputStream input = Files.newInputStream(Path.of(path))){
            trustStore.load(input, password);
        }



    }

}
