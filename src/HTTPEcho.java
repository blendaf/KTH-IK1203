import java.net.*;
import java.io.*;


public class HTTPEcho {
    public static void main( String[] args) {

        ServerSocket serverSocket = new ServerSocket(0);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


        while(true){

        }










        String clientSentence;
        String capitalizedSentence;



        try
        {
            ServerSocket server = new ServerSocket(0);
        } catch (IOException e)
        {
            // handle exception
        }

        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);

        }




    }
}

