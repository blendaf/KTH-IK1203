import java.net.*;
import java.io.*;


public class HTTPEcho {
    public static void main( String[] args) throws Exception{
        String clientSentence;
        String capitalizedSentence;
        int port;
        StringBuilder modifiedSentence = new StringBuilder();

        port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);

        while(true){

            Socket connectionSocket = serverSocket.accept();

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());

            String s;
            while((s = inFromServer.readLine()) != "\n" && s != null){
                modifiedSentence.append(s + "\r\n");
                //outToServer.println(s);
            }

        }


       /** try
        {
            ServerSocket serverSocket = new ServerSocket(0);
        } catch (IOException e)
        {
            // handle exception
        }

**//

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

