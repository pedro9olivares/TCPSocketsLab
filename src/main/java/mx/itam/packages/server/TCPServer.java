package mx.itam.packages.server;

import java.net.*;
import java.io.*;

public class TCPServer {
    public static void main(String args[]) {
        try {
            int serverPort = 49152;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            AddressBook book = new AddressBook();

            while (true) {
                System.out.println("Waiting for messages...");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                Connection c = new Connection(clientSocket, book);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }

}

class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private AddressBook book;

    public Connection(Socket aClientSocket, AddressBook book) {
        this.book = book;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // an echo server
            int id = in.readInt();         // recibo solicitud

            Person person = book.getRecord(id); // obtengo la persona

            System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress());

            out.writeUTF(person.getName());                // envio respuesta

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
