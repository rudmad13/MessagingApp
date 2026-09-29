# TCP Server (Chat server)

## Description
This project was created to provide a simple way to communicate with friends without relying on large messaging platforms. It is a multithreaded TCP chat application written in Java that allows mulitple clients to connect to a server and exchange messages in real time.

## Features
 - Thread-per-client server model
 - Supports multiple concurrent client connections
 - Server broadcasts all messages to clients connected to server

 # Installation

 ## Requirements
  - Java 21 or newer
  - Download JAR file from Releases
 
## How to run

Complete the one-time [server TLS setup](#server-tls-setup) or
[client TLS setup](#client-tls-setup) below before running these commands.

### Server

Replace the keystore path and password with your server's values:

```powershell
java "-Djavax.net.ssl.keyStore=C:\path\to\server.p12" "-Djavax.net.ssl.keyStorePassword=YOUR_PASSWORD" "-Djavax.net.ssl.keyStoreType=PKCS12" -jar target/Tcpserver-1.0-SNAPSHOT.jar -s 5000
```

### Client

Replace the truststore path and password with your client's values:

```powershell
java "-Djavax.net.ssl.trustStore=C:\path\to\client-truststore.p12" "-Djavax.net.ssl.trustStorePassword=YOUR_PASSWORD" "-Djavax.net.ssl.trustStoreType=PKCS12" -jar target/Tcpserver-1.0-SNAPSHOT.jar -c localhost 5000 alice
```

In both commands, replace the JAR path if you downloaded or moved the JAR.
Change 5000 to the server's port. For the client, replace localhost with the
server's host or IP (which must match its certificate) and alice with your username.
The TLS settings must appear before -jar.

# Developers

## Requirements
- Maven 3.9+
- Java 21 or newer

## Build
```bash 
mvn clean package
```

#Future Features
- Username
- End-to-end encryption
- GUI
- User Authentication

## Server TLS setup

Both server and client require TLS 1.3. Client certificates are not required.

Generate a local development certificate with Java's keytool (it prompts for a password):

~~~powershell
keytool -genkeypair -alias chat-server -keyalg RSA -keysize 3072 -storetype PKCS12 -keystore server.p12 -validity 365 -dname "CN=localhost" -ext "SAN=dns:localhost,ip:127.0.0.1"
~~~

For remote use, replace the SAN addresses with the server's actual DNS name or IP.
Keep the keystore and its private key out of source control.

Start the server, replacing the path and password:

~~~powershell
java "-Djavax.net.ssl.keyStore=C:\path\to\server.p12" "-Djavax.net.ssl.keyStorePassword=YOUR_PASSWORD" "-Djavax.net.ssl.keyStoreType=PKCS12" -jar target/Tcpserver-1.0-SNAPSHOT.jar -s 5000
~~~

Export only the public certificate to share with clients through a trusted channel:

~~~powershell
keytool -exportcert -rfc -alias chat-server -keystore server.p12 -file server-cert.pem
~~~

## Client TLS setup

Obtain server-cert.pem through a trusted channel, then import it into a client truststore.
Never copy the server's private keystore to clients:

~~~powershell
keytool -importcert -alias chat-server -file server-cert.pem -keystore client-truststore.p12 -storetype PKCS12
~~~

Check the certificate fingerprint with the server operator before accepting the import.
Start the client using that truststore:

~~~powershell
java "-Djavax.net.ssl.trustStore=C:\path\to\client-truststore.p12" "-Djavax.net.ssl.trustStorePassword=YOUR_PASSWORD" "-Djavax.net.ssl.trustStoreType=PKCS12" -jar target/Tcpserver-1.0-SNAPSHOT.jar -c localhost 5000 alice
~~~

The host or IP must match the certificate's Subject Alternative Name. The development
certificate supports localhost and 127.0.0.1. Certificate trust and address verification
remain enabled; there is no plaintext fallback. Without an explicit truststore,
Java's default trusted certificates apply.

Command-line passwords may be visible in shell history and process arguments.
These commands are intended for local development.

TLS completes before the username handshake. Connection setup uses ten-second read
timeouts, cleared on acceptance. Server handshakes still run sequentially in the accept
loop. TLS protects traffic between clients and the server; the server reads messages
to broadcast them.
