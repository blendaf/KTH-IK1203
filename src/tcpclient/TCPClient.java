package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws  IOException {

        if(ToServer == null) {
            return askServer(hostname, port);
        }


        String sentence = ToServer;
        StringBuilder modifiedSentence = new StringBuilder();



        //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(3000);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(sentence + '\n');

        String serverString;

        try {

            while((serverString = inFromServer.readLine()) != "\n" && serverString != null){
                modifiedSentence.append(serverString + '\n');
            }

        } catch (IOException e) {
            clientSocket.close();
            return modifiedSentence.toString();
        }


        clientSocket.close();
        return modifiedSentence.toString();
    }

    public static String askServer(String hostname, int port) throws  IOException {


        String sentence = "ping";
        StringBuilder modifiedSentence = new StringBuilder();



        //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(3000);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(sentence + '\n');

        String serverString;

        try {
            while((serverString = inFromServer.readLine()) != "\n" && serverString != null){
                modifiedSentence.append(serverString + '\n');
            }
        } catch (IOException e) {
            clientSocket.close();
            return modifiedSentence.toString();
        }


        clientSocket.close();
        return modifiedSentence.toString();


    }
}



