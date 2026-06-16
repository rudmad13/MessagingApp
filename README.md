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
 There are to modes to run this program
 - server
 - client
 
To run as a server you do

```bash 
java -jar <jar> -s <portNumber>
```
- portNumber - The port the server will listen on

To run as client

```bash
java -jar <jar> -c <serverIP> <port> <username>
```

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
- Encryption 
- GUI
- User Authentication