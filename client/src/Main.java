import java.io.IOException;
import java.net.*;

public class Main {
    public static void main(String[] args) {

        Thread receiveBroadCastSocket = new Thread(
                () -> {
                    /**dd
                     * TODO 전체 코인 받아오는 스레드
                     * 서버에서 코인 정보 갱신 brocastsocket.read를 통해 읽어오고
                     * 읽어온 정보를 업데이트 시켜줌
                     */
                    try (MulticastSocket socket = new MulticastSocket(5000)) {
                        InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
                        socket.joinGroup(groupAddress);
                        byte[] buffer = new byte[1024 * 1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // 연결 지향이 아니기때문에 받기만 하는 입장인 Client는 주소와 포트는 필요가 없다.
                        while (true) {
                            socket.receive(packet); // receive는 packet으로 받아야함
                            byte[] receiveData = packet.getData();
                            String data = new String(receiveData, 0, packet.getLength());
                            System.out.println(data);
                        }
                    }
                    catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        receiveBroadCastSocket.start();

        Thread receiveTCPSocket = new Thread(
                () -> {
                    /**
                     * TODO 서버로부터 매수/매도 요청 받아오기
                     * 받아온 정보 gui에 갱신
                     */
                    try {
                        Socket clientSocket = new Socket(Inet4Address.getByName("127.0.0.1"), 4000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        receiveTCPSocket.start();
    }
}
