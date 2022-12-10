import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMultiTheadedServer implements Runnable {
 //ㅠㅠㅠ하...
    private static final ConcurrentHashMap<String, Float> map;
    private final Socket clientSocket;

    static {
        map = new ConcurrentHashMap<>();
        map.put("Axle", 238.50f);
        map.put("Gear", 45.55f);
        map.put("Wheel", 86.30f);
        map.put("Rotor", 8.50f);
    }

    SimpleMultiTheadedServer(Socket socket) {
        this.clientSocket = socket;
    }

    public static void main(String args[]) {
        System.out.println("Multi-Threaded Server Started");
        try {
            new Thread(
                    () -> {
                        try(DatagramSocket socket = new DatagramSocket()) {
                            socket.setBroadcast(true);
                            String message = "노트북 친구들 하이";
                            byte[] buffer = message.getBytes();
                            while(true) {
                                try {
                                    InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
                                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, 5000);
                                    socket.send(packet);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();

            ServerSocket serverSocket = new ServerSocket(4000);
            while (true) {
                System.out.println("Listening for a client connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected to a Client");
                new Thread(new SimpleMultiTheadedServer(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Multi-Threaded Server Terminated");
    }

    public void run() {
        System.out.println("Client Thread Started");
        try (BufferedReader bis = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
                PrintStream out = new PrintStream(
                        clientSocket.getOutputStream())) {

            String partName = bis.readLine();
            float price = map.get(partName);
            out.println(price);
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            System.out.println("Request for " + partName
                    + " and returned a price of "
                    + nf.format(price));
            
//            while(true) {
//                String partName = bis.readLine();
//                if("quit".equalsIgnoreCase(partName)) {
//                    break;
//                }
//                float price = map.get(partName);
//                out.println(price);
//                NumberFormat nf = NumberFormat.getCurrencyInstance();
//                System.out.println("Request for " + partName
//                        + " and returned a price of "
//                        + nf.format(price));
//            } 
            
            clientSocket.close();
            System.out.println("Client Connection Terminated");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Client Thread Terminated");
    }
}
