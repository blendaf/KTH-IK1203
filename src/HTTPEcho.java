import java.net.*;
import java.io.*;
import java.util.*;

public class HTTPEcho {
    public static void main(String[] args) throws Exception {

        String clientSentence;
        int port;
        StringBuilder sentenceOut = new StringBuilder();

        port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);


        Date date = new Date();
        String httpMessage = "HTTP/1.1 200 OK" + "/r" + "Date: " + date + "\r\n";
        System.out.println(date);


        while (true) {


            System.out.println("request");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("accepted");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String temp = "blenda";

                while(temp.compareTo("")!= 0) {

                    System.out.println(temp);
                    temp = inFromClient.readLine();
                    sentenceOut.append(temp + "\r\n");
                    System.out.println("in while-loop");

                }



            System.out.println("after while-loop");

            System.out.print(sentenceOut);


            sentenceOut = sentenceOut.insert(0, "\r\n");
            sentenceOut = sentenceOut.insert(0, httpMessage);


            outToClient.writeBytes(sentenceOut.toString());
            connectionSocket.close();

            sentenceOut.setLength(0);


        }

        //outToClient.close();
        //inFromClient.close();



    }

}



