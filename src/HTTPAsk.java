import tcpclient.TCPClient;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;


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
            System.out.println("accepted");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String temp = "blenda";
            while (temp.compareTo("") != 0) {

                temp = inFromClient.readLine();
                request.append(temp + "\r\n");
            }


            String getRequest = request.toString();
            System.out.println("Check: " + getRequest);

            if(!getRequest.contains("/ask?")){
                httpResponse.append("HTTP/1.1 401 Not Found" + "\r\n");
                outToClient.writeBytes(httpResponse.toString());
                connectionSocket.close();

            }else {

                //remove Get and split at //ask?
                String[] splitQuery = toQuery(getRequest);

                Boolean validRequest = false;
                //Boolean validRequest = true;
                String errorMessage = "no error";

                //check if URI starts with "/ask?"
                if (!splitQuery[0].startsWith("/ask?")) {
                    errorMessage = "404";
                    validRequest = false;
                    httpResponse.append("HTTP/1.1 404 Not Found" + "\r\n");
                    outToClient.writeBytes(httpResponse.toString());
                    connectionSocket.close();
                }
                //check if there are more than one "?"
                if (splitQuery[1].contains("?")) {
                    errorMessage = "4xx";
                    validRequest = false;
                    httpResponse.append("HTTP/1.1 402 Payment Required" + "\r\n");
                    outToClient.writeBytes(httpResponse.toString());
                    connectionSocket.close();
                } else {
                    validRequest = true;
                }

                if (!validRequest) {
                    httpResponse.append("404");
                    outToClient.writeBytes(httpResponse.toString());
                    connectionSocket.close();


                } else {

                    String finalPath = splitQuery[1];

                    System.out.println("Check final path: " + finalPath);

                    String[] pairs = finalPath.split("&");

                    boolean validPairs = true;
                    for (int i = 0; i < pairs.length; i++) {
                        System.out.println("Check pairs: " + pairs[i]);
                        if (!pairs[i].contains("=")) {
                            validPairs = false;
                            break;
                        }
                    }


                    /**
                     if(!validPairs){
                     validRequest = false;
                     errorMessage = "404";
                     }

                     **/

                    HashMap<String, String> parsedPairs = parsePairs(pairs);

                    boolean portAndHost = false;
                    if (parsedPairs.containsKey("hostname") && parsedPairs.containsKey("port")) {
                        portAndHost = true;
                    } else {
                        portAndHost = false;
                    }

                    boolean stringexists = false;
                    if (pairs.length > 2) {
                        stringexists = true;
                    }

                    System.out.println("Check portandHost: " + portAndHost);
                    System.out.println("Check string: " + stringexists);

                    port = Integer.parseInt(parsedPairs.get("port"));
                    hostname = parsedPairs.get("hostname");

                    System.out.println("Check port: " + port);
                    System.out.println("Chesk hostname: " + hostname);

                    String tcpResponse;
                    if (validRequest && stringexists) {
                        tcpResponse = TCPClient.askServer(hostname, port, pairs[3]);
                    }
                    if (validRequest && !stringexists) {
                        tcpResponse = TCPClient.askServer(hostname, port, "");

                    } else {
                        tcpResponse = errorMessage;
                    }

                    System.out.println("Check TCP response: " + tcpResponse);

                    httpResponse.append(tcpResponse);

                    System.out.println(httpResponse.toString());

                    httpResponse.append(httpMessage);
                    httpResponse.append("\r\n");

                    outToClient.writeBytes(httpResponse.toString());

                    connectionSocket.close();

                    httpResponse.setLength(0);

                }

            }
        }

    }



    public static HashMap<String, String>parsePairs(String[] pairs){
        HashMap<String, String> parsedPairs = new HashMap<>();
        for(int i = 0; i < pairs.length; i++)
        {
            String[] split = pairs[i].split("=");
            //TODO:fixa att host korresponderar till hostnamnet och port till portnamnet.
            parsedPairs.put(split[0], split[1]);
        }
        return parsedPairs;
    }

    public static String[] toQuery(String getRequest){
        //Remove "GET"
        String[] removeGet = getRequest.split(("\\s+"));
        //divide path into "/ask?" and rest
        String[] splitQuery = removeGet[1].split("(?<=/ask\\?)");
        return splitQuery;
    }


}

