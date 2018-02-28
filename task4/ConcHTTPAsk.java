import java.net.ServerSocket;
import java.net.Socket;

public class ConcHTTPAsk {

    public static void main(String[] args) throws Exception {

        try {
            int clientPort = Integer.parseInt(args[0]);
            ServerSocket webServerSocket = new ServerSocket(clientPort);

            while (true) {
                System.out.println("request");
                Socket connectionSocket = webServerSocket.accept();
                new Thread(new MyRunnable(connectionSocket)).start();
            }
        } catch (Exception e) {
            System.out.print("errorerror!");
        }
    }
}
