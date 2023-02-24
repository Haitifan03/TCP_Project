import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientTCPFile {
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
        File file = new File(System.getProperty("user.dir") + "/src/main/java/test");
        writeByteArrayToFile(bytes, file);
        System.out.println(new String(bytes));

        sc.close();

    }
    public static void writeByteArrayToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }
}
