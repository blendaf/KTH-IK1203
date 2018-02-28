package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        int counter = 0;

        if (ToServer == null) {
            return askServer(hostname, port);
        }

        String sentence = ToServer;
        StringBuilder modifiedSentence = new StringBuilder();

        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(3000);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(sentence + '\n');
        String serverString;

        try {
            while ((serverString = inFromServer.readLine()) != "\n" && serverString != null && counter < 1024) {
                modifiedSentence.append(serverString + '\n');
                counter++;
            }
        } catch (IOException e) {
            clientSocket.close();
            return modifiedSentence.toString();
        }

        counter = 0;
        clientSocket.close();
        System.out.println("still in tcpclient");
        return modifiedSentence.toString();

    }

    public static String askServer(String hostname, int port) throws IOException {

        int counter = 0;
        String sentence = "ping";
        StringBuilder modifiedSentence = new StringBuilder();

        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(3000);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(sentence + "\n");
        String serverString;

        try {
            while ((serverString = inFromServer.readLine()) != "\n" && serverString != null && counter < 1024) {
                modifiedSentence.append(serverString + "\n");
                counter++;
            }

        } catch (IOException e) {
            clientSocket.close();
            return modifiedSentence.toString();
        }

        counter = 0;
        clientSocket.close();
        return modifiedSentence.toString();
    }
}
