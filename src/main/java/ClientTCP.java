import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) throws IOException {
        if (args.length != 2){
            System.out.println("Usage: ClientTCP <ServerIP> <ServerPort>");
            return;
        }
        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

        Scanner keyboard = new Scanner(System.in);
        String message = keyboard.nextLine();
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

        SocketChannel sc = SocketChannel.open();

        sc.connect(new InetSocketAddress(serverIP, serverPort));

        sc.write(buffer);
        buffer.flip();
        sc.shutdownOutput();

        buffer = ByteBuffer.allocate(1024);
        sc.read(buffer);
        buffer.flip();
        byte[] bytes = buffer.array();
        System.out.println(new String(bytes));

        sc.close();

    }
}
