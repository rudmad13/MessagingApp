# TCP Server (Chat server)

## Table of contents

- [Description](#description)
- [Features](#features)
- [Installation](#installation)
- [How to run](#how-to-run)
  - [Server](#server)
  - [Client](#client)
- [Developers](#developers)
  - [Build](#build)
- [Server TLS setup](#server-tls-setup)
- [Client TLS setup](#client-tls-setup)

## Description

This project was created to provide a simple way to communicate with friends without relying on large messaging platforms. It is a multithreaded TCP chat application written in Java that allows multiple clients to connect to a server and exchange messages in real time.

## Features

- Thread-per-client server model
- Supports multiple concurrent client connections
- Server broadcasts messages to connected clients
- Interactive server setup for the keystore and listening port
- Interactive client setup for the truststore, server address, port, and username
- TLS 1.3 encryption protects messages in transit between each client and the server
- Server certificate and hostname verification using a client PKCS12 truststore

The server decrypts messages to broadcast them; messages are not end-to-end encrypted.

## Installation

### Requirements

- Java 21 or newer
- Download JAR file from Releases

## How to run

Complete the one-time [server TLS setup](#server-tls-setup) or
[client TLS setup](#client-tls-setup) below before running these commands.
In both commands, replace the JAR path if you downloaded or moved the JAR.

### Server

Start the server with `-s` to select server mode:

```powershell
java -jar target/Tcpserver-1.0-SNAPSHOT.jar -s
```

The server asks for the following information in order. Enter each value when prompted:

| Prompt | Example value | What to enter |
| --- | --- | --- |
| Enter absolute path for Keystore: | `C:\path\to\server.p12` | The full path to the PKCS12 keystore created in [server TLS setup](#server-tls-setup), without surrounding quotes. |
| Enter password for Keystore | `YOUR_PASSWORD` | The password you chose when creating the keystore. Input is visible as you type. |
| Enter port number between 0 and 65535 | `5000` | The port the server should listen on. Use the same port when connecting clients. Port `0` lets the system choose an available port; use the port printed by the server. |

The server loads its TLS settings from the keystore prompts and asks for the path
and password again if the keystore cannot be loaded. Enter the port interactively;
no TLS system properties or port argument are needed in the launch command.

### Client

Start the client with `-c` to select client mode:

```powershell
java -jar target/Tcpserver-1.0-SNAPSHOT.jar -c
```

The client asks for the following information in order. Enter each value when prompted:

| Prompt | Example value | What to enter |
| --- | --- | --- |
| Enter absolute path for truststore: | `C:\path\to\client-truststore.p12` | The full path to the PKCS12 truststore created in [client TLS setup](#client-tls-setup), without surrounding quotes. |
| Enter password for truststore: | `YOUR_PASSWORD` | The password you chose when creating the truststore. Input is visible as you type. |
| Enter ip address for server: | `localhost` | The server's hostname or IP address, which must match its certificate. |
| Enter the port the user is entering: | `5000` | The numeric port the server is listening on. |
| Enter a username: | `alice` | Your chat username. Choose a name that is not already in use on the server. |

The client loads its TLS settings from the truststore prompts. The server address,
port, and username are entered interactively. No TLS system properties or connection arguments are needed in the launch command.
If the truststore cannot be loaded, the client asks for its path and password again.

Once connected, type a message and press Enter to send it. Type `quit()` and press
Enter to disconnect.

## Developers

### Development requirements

- Maven 3.9+
- Java 21 or newer

### Build

```bash
mvn clean package
```

## Server TLS setup

Both server and client require TLS 1.3. Client certificates are not required.

Generate a local development certificate with Java's keytool (it prompts for a password):

~~~powershell
keytool -genkeypair -alias chat-server -keyalg RSA -keysize 3072 -storetype PKCS12 -keystore server.p12 -validity 365 -dname "CN=localhost" -ext "SAN=dns:localhost,ip:127.0.0.1"
~~~

For remote use, replace the SAN addresses with the server's actual DNS name or IP.
Keep the keystore and its private key out of source control.

Keep the keystore's absolute path and the password you chose for the server startup
prompts. Start the server:

~~~powershell
java -jar target/Tcpserver-1.0-SNAPSHOT.jar -s
~~~

Enter the keystore path and password, then the listening port as described in
[Server](#server). The server requires a PKCS12 keystore at startup.

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
Keep the truststore's absolute path and the password you chose for the client startup
prompts. Start the client:

~~~powershell
java -jar target/Tcpserver-1.0-SNAPSHOT.jar -c
~~~

Enter the truststore path and password, then the server address, port, and username
as described in [Client](#client). The client requires a PKCS12 truststore at startup.

The host or IP must match the certificate's Subject Alternative Name. The development
certificate supports localhost and 127.0.0.1. Certificate trust and address verification
remain enabled; there is no plaintext fallback.
