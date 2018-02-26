import tcpclient.TCPClient;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class HTTPAsk {
    public static void main( String[] args) throws Exception{

        StringBuilder httpResponse = new StringBuilder();
        StringBuilder request = new StringBuilder();
        String hostname;
        int port;
        int clientPort;

        clientPort = Integer.parseInt(args[0]);
        ServerSocket webServerSocket = new ServerSocket(clientPort);

        Date date = new Date();
        String httpMessage = "HTTP/1.1 200 OK" + "/r" + "Date: " + date + "\r\n";
        System.out.println(date);


        while (true) {

            System.out.println("request");
            Socket connectionSocket = webServerSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String temp = inFromClient.readLine();
            System.out.println("temp is: " + temp);
            while (!temp.equals("")) {
                request.append(temp + "\r\n");
                temp = inFromClient.readLine();
            }


            String getRequest = request.toString();
            System.out.println("Check: " + getRequest);

            System.out.println("before response");
            response(getRequest, outToClient);

            connectionSocket.close();

            httpResponse.setLength(0);

        }

    }


    public static void response(String getRequest, DataOutputStream outToClient)throws Exception {

        StringBuilder httpResponse = new StringBuilder();
        int port;
        String hostname;
        String string;
        Date date = new Date();


        System.out.println("in response");
        String[] splitComp = getRequest.split(" ");

        if(splitComp.length < 3 ){
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        String fullPath = splitComp[1];
        String[] sectionSplit = fullPath.split("\\?");

        if(sectionSplit.length > 2){
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        String path = sectionSplit[0];
        System.out.println(path);
        if(!path.equals("/ask")){
            httpResponse.append("HTTP/1.1 404 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        if(sectionSplit.length == 1){
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        String query = sectionSplit[1];
        String[] queries = query.split("&");


        LinkedList<String[]> pairs = new LinkedList<>();
        for (String pair : queries) {
            pairs.add(pair.split("="));
        }

        boolean validPairs = true;
        for(String[] pair: pairs){
            if(pair.length != 2 ){ validPairs = false; }
        }


        if(!validPairs){
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }
        // PERF


        HashMap<String, String> parsedPairs = new HashMap<>();
        for(String[]pair : pairs){
            parsedPairs.put(pair[0], pair[1]);
        }

        if (!parsedPairs.containsKey("hostname") || !parsedPairs.containsKey("port")) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        //StringExists
        Boolean stringExists = false;
        String stringInput = "";

        port = Integer.parseInt(parsedPairs.get("port"));
        hostname = parsedPairs.get("hostname");

        System.out.println("Check port: " + port);
        System.out.println("Check hostname: " + hostname);

        if(parsedPairs.containsKey("string")){
            stringExists = true;
            stringInput = parsedPairs.get("string");
        }

        String tcpResponse;

        if(stringExists){ tcpResponse = TCPClient.askServer(hostname, port);}
        else{ tcpResponse = TCPClient.askServer(hostname, port, stringInput);}

        if(tcpResponse.contains("HTTP/1.1 404")){
            httpResponse.append("HTTP/1.1 404 Not Found" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        System.out.println("Check TCP response: " + tcpResponse);
        System.out.println(httpResponse.toString());

        httpResponse.append("HTTP/1.1 200 OK" + "/r" + "Date: " + date + "\r\n");
        httpResponse.append("\r\n");
        httpResponse.append(tcpResponse);

        outToClient.writeBytes(httpResponse.toString());

    }


}

