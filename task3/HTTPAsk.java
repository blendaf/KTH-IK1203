import tcpclient.TCPClient;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class HTTPAsk {
    public static void main(String[] args) throws Exception {

        StringBuilder httpResponse = new StringBuilder();
        StringBuilder request = new StringBuilder();
        int hostPort;

        try {
            hostPort = Integer.parseInt(args[0]);
        } catch (Exception e) {
            hostPort = 8888;
        }

        ServerSocket webServerSocket = new ServerSocket(hostPort);

        while (true) {
            System.out.println("request");
            Socket connectionSocket = webServerSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String temp = inFromClient.readLine();

            if (temp == null) {
                System.out.println("temp is null");
            }

            while (!temp.equals("")) {
                request.append(temp + "\r\n");
                temp = inFromClient.readLine();
            }

            String getRequest = request.toString();

            response(getRequest, outToClient);

            connectionSocket.close();
            request.setLength(0);
        }

    }

    public static void response(String getRequest, DataOutputStream outToClient) throws Exception {

        StringBuilder httpResponse = new StringBuilder();
        int port;
        String hostname;
        String stringInput = "";
        Date date = new Date();

        String[] splitComp = getRequest.split(" ");

        if (splitComp.length < 3) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        String fullPath = splitComp[1];
        String[] sectionSplit = fullPath.split("\\?");

        if (sectionSplit.length > 2) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        String path = sectionSplit[0];
        if (!path.equals("/ask")) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        if (sectionSplit.length == 1) {
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
        for (String[] pair : pairs) {
            if (pair.length != 2) {
                validPairs = false;
                break;
            }
        }

        if (!validPairs) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        HashMap<String, String> parsedPairs = new HashMap<>();
        for (String[] pair : pairs) {
            parsedPairs.put(pair[0], pair[1]);
        }

        if (!parsedPairs.containsKey("hostname") || !parsedPairs.containsKey("port")) {
            httpResponse.append("HTTP/1.1 400 Bad Request" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
            return;
        }

        Boolean stringExists = false;

        port = Integer.parseInt(parsedPairs.get("port"));
        hostname = parsedPairs.get("hostname");

        System.out.println("Check ports: " + port);
        System.out.println("Check hostname: " + hostname);

        if (parsedPairs.containsKey("string")) {
            stringExists = true;
            stringInput = parsedPairs.get("string");
        }

        String tcpResponse;
        try {
            if (stringExists) {
                tcpResponse = TCPClient.askServer(hostname, port, stringInput);
            } else {
                tcpResponse = TCPClient.askServer(hostname, port);
            }

            httpResponse.append("HTTP/1.1 200 OK" + "/r" + "Date: " + date + "\r\n");
            httpResponse.append("\r\n");
            httpResponse.append(tcpResponse);
            outToClient.writeBytes(httpResponse.toString());

        } catch (Exception e) {
            httpResponse.append("HTTP/1.1 404 Not found" + "\r\n");
            outToClient.writeBytes(httpResponse.toString());
        }
    }
}
