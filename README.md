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
- Server certificate and hostname verification using a client PKCS12 store
- Mutual TLS authentication: both server and client present certificates trusted by the other side

The server decrypts chat messages to broadcast them; messages are not end-to-end encrypted. TLS protects the TCP connection; the UDP voice relay does not currently encrypt audio.

## Installation

### Requirements

- Java 21 or newer
- Download JAR file from Releases

## How to run

Complete the certificate exchange in [server TLS setup](#server-tls-setup) and
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
| Enter path for Keystore: | `C:\path\to\server.p12` | The full path to the PKCS12 keystore created in [server TLS setup](#server-tls-setup), without surrounding quotes. |
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
| Enter path for truststore: | `C:\path\to\client.p12` | The full path to the combined client PKCS12 store created in [client TLS setup](#client-tls-setup), without surrounding quotes. |
| Enter password for truststore: | `YOUR_PASSWORD` | The password you chose when creating the truststore. Input is visible as you type. |
| Enter ip address for server: | `localhost` | The server's hostname or IP address, which must match its certificate. |
| Enter the port the user is entering: | `5000` | The numeric port the server is listening on. |
| Enter a username: | `alice` | Your chat username. Choose a name that is not already in use on the server. |

The client loads its TLS settings from the truststore prompts. The server address,
port, and username are entered interactively. No TLS system properties or connection arguments are needed in the launch command.
If the truststore cannot be loaded, the client asks for its path and password again.

Once connected, type a message and press Enter to send it. Type `/quit` and press
Enter to disconnect from the server and exit the client.

## Developers

### Development requirements

- Maven 3.9+
- Java 21 or newer

### Build

```bash
mvn clean package
```

## Server TLS setup

The application uses TLS 1.3 with mutual authentication: the client verifies the
server certificate, and the server requires a trusted client certificate.
This workflow creates self-signed certificates for direct trust between peers.

Each side currently loads one PKCS12 file as both its keystore and truststore:

| File | Own identity (keystore role) | Trusted peers (truststore role) |
| --- | --- | --- |
| Server store, e.g. `server.p12` | Server private key and certificate | Imported client certificates |
| Client store, e.g. `client.p12` | Client private key and certificate | Imported server certificate |

Run the commands in PowerShell using the JDK's `keytool`. Keep store files outside
the repository. Passwords are requested interactively.

### Replace the placeholders

Replace every `<...>` value, including the angle brackets, before running a command.
Keep the surrounding quotes. Reuse the same chosen paths and aliases in later steps.

| Placeholder | Example | Meaning |
| --- | --- | --- |
| `<server-cert-alias>` | `chat-server` | Local alias for the server's private-key entry; reuse when exporting. |
| `<client-cert-alias>` | `chat-client` | Local alias for this client's private-key entry; reuse when exporting. |
| `<server-store-path>` | `C:\MessagingCerts\server.p12` | Server's combined PKCS12 store. |
| `<client-store-path>` | `C:\MessagingCerts\client.p12` | Client's combined PKCS12 store. |
| `<server-cert-file>` | `C:\MessagingCerts\server-cert.pem` | Public server certificate to export or import. |
| `<client-cert-file>` | `C:\MessagingCerts\client-alice-cert.pem` | Public client certificate to export or import. |
| `<server-display-name>` | `Chat Server` | Readable certificate subject (CN, Common Name). |
| `<client-display-name>` | `Client Alice` | Readable client certificate subject; separate from the chat username. |
| `<trusted-server-alias>` | `trusted-server` | New alias for the server certificate in the client store. |
| `<trusted-client-alias>` | `trusted-client-alice` | New alias for this client certificate in the server store. |

Create the parent folders first. Certificate file paths refer to local copies on
whichever computer runs the command. Trusted-certificate aliases must differ from
private-key aliases in the same store; use a distinct trusted-client alias for each client.

### 1. Generate the server identity

On the server computer:

```powershell
keytool -genkeypair -alias "<server-cert-alias>" -keyalg RSA -keysize 3072 -storetype PKCS12 -keystore "<server-store-path>" -validity 365 -dname "CN=<server-display-name>" -ext "SAN=dns:localhost,ip:127.0.0.1" -ext "EKU=serverAuth"
```

Choose a store password and retain it for the server startup prompt. This creates
the private key and its self-signed certificate. Use a new store path for a new
identity; these commands do not replace an existing entry with the same alias.

CN gives the certificate a readable subject name. SAN (Subject Alternative Name)
lists the hostnames and IP addresses clients may use to connect. The example SAN
supports local testing using `localhost` or `127.0.0.1`.

For a public IP, replace the SAN argument above with:

```text
-ext "SAN=dns:localhost,ip:127.0.0.1,ip:<server-public-ip>"
```

Replace `<server-public-ip>` with the server's actual public IP. Add
`dns:<server-hostname>` for a DNS name or `ip:<server-lan-ip>` for a LAN address
as additional comma-separated entries when needed. Clients must connect using
an address included in SAN. Router port forwarding and firewall access must also
allow the server's TCP listening port; adding an address to SAN does not provide
network connectivity.

EKU (Extended Key Usage) declares the certificate's intended role: `serverAuth`
for the server and `clientAuth` for the client. These commands explicitly set it.
SAN entries cannot be edited in an issued certificate; changing them requires a
replacement certificate and updating the certificate trusted by clients.

### 2. Export the server's public certificate

```powershell
keytool -exportcert -rfc -alias "<server-cert-alias>" -keystore "<server-store-path>" -file "<server-cert-file>"
```

Enter the server store password. Send only the exported public certificate to
clients through a trusted channel. Keep the server store and its password private.

Continue with client setup below. Before a client can connect, the server must
also import that client's public certificate.

## Client TLS setup

### 3. Generate the client identity

On each client computer, obtain the server's public certificate, then generate
a separate client identity:

```powershell
keytool -genkeypair -alias "<client-cert-alias>" -keyalg RSA -keysize 3072 -storetype PKCS12 -keystore "<client-store-path>" -validity 365 -dname "CN=<client-display-name>" -ext "EKU=clientAuth"
```

Choose a client store password. Each client should have its own private key and
certificate; do not share client stores between users.

### 4. Trust the server on the client

Import the server certificate into the same client store:

```powershell
keytool -importcert -alias "<trusted-server-alias>" -file "<server-cert-file>" -keystore "<client-store-path>" -storetype PKCS12
```

Enter the client store password. Before accepting the trust prompt, compare the
displayed SHA-256 fingerprint with the server operator through a trusted channel.
The operator can display their certificate fingerprint with:

```powershell
keytool -printcert -file "<server-cert-file>"
```

### 5. Export the client's public certificate

On the client:

```powershell
keytool -exportcert -rfc -alias "<client-cert-alias>" -keystore "<client-store-path>" -file "<client-cert-file>"
```

Enter the client store password. Send the exported certificate to the server
operator. Keep the client store and its password private.

### 6. Trust the client on the server

On the server, import the received client certificate into the existing server store:

```powershell
keytool -importcert -alias "<trusted-client-alias>" -file "<client-cert-file>" -keystore "<server-store-path>" -storetype PKCS12
```

Enter the server store password. Verify the displayed SHA-256 fingerprint with the
client owner before accepting. The client owner can display their fingerprint with:

```powershell
keytool -printcert -file "<client-cert-file>"
```

Repeat client identity creation and certificate exchange for each additional client,
using a distinct trusted-client alias in the server store.

### 7. Start the server and client

Start or restart the server after importing client certificates so it loads the
updated trust settings. Restart clients after changing their stores as well.

- At the server's keystore prompt, enter the absolute `<server-store-path>` and its password.
- At the client's truststore prompt, enter the absolute `<client-store-path>` and its password.
  Despite the prompt's name, the client loads this file as both its keystore and truststore.
- Enter paths without surrounding quotes at the application prompts.
- Connect using a hostname or IP in the server certificate's SAN and the server's
  TCP listening port.

See [Server](#server) and [Client](#client) for launch commands. Certificate trust
and hostname verification remain enabled; there is no plaintext fallback.

For the certificate command reference, see
[Oracle's Java 21 keytool documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/keytool.html).
