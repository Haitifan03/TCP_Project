import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerTCPFile {
    public static void main(String @NotNull [] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: ServerTCP <port>");
            return;
        }
        ArrayList<String> fileList = getList();
        rename("test2", "test", fileList);
        System.out.println(fileList);
        updateList(fileList);

        int port = Integer.parseInt(args[0]);

        ServerSocketChannel listenChannel = ServerSocketChannel.open();

        listenChannel.bind(new InetSocketAddress(port));

        while (true) {
            SocketChannel serveChannel = listenChannel.accept();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            serveChannel.read(buffer);
            buffer.flip();
            byte[] bytes = buffer.array();
            System.out.println(new String(bytes));
            buffer.rewind();

            serveChannel.write(buffer);
            serveChannel.close();
        }
    }

    public static void delete(String fileName, ArrayList<String> fileList) {
        File file = new File(System.getProperty("user.dir") + "/src/main/java/" + fileName);
        // check if the file exists
        if (file.exists()) {
            // delete the file
            boolean deleted = file.delete();

            // check if the file was successfully deleted
            if (deleted) {
                System.out.println("File deleted successfully.");
                fileList.remove(fileName);
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public static void rename(String fileName, String newName, ArrayList<String> fileList) {
        File file = new File(System.getProperty("user.dir") + "/src/main/java/" + fileName);

        // check if the file exists
        if (file.exists()) {
            file.renameTo(new File(System.getProperty("user.dir") + "/src/main/java/" + newName));
            System.out.println("The file has been renamed.");
            fileList.remove(fileName);
            fileList.add(newName);
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public static ArrayList<String> getList() {
        ArrayList<String> tempArray = new ArrayList<>();
        File fileList = new File(System.getProperty("user.dir") + "/src/main/java/FileList");
        try (FileReader file = new FileReader(fileList)) {
                Scanner reader = new Scanner(file);
                String line;
                while (((line = reader.nextLine()) != null)) {
                    tempArray.add("" + (line));
                }
        }
        catch (Exception e) {}
        return tempArray;
    }
    public static void addFileToList(String filename, ArrayList<String> tempArray){
        tempArray.add(filename);
    }
    public static void updateList(ArrayList<String> tempArray){
        File fileList = new File(System.getProperty("user.dir") + "/src/main/java/FileList");
        try (PrintWriter author = new PrintWriter(fileList)){
            for (int i=0; i<tempArray.size();i++){
                author.println(tempArray.get(i));
            }
        } catch (Exception e){

        }
    }
}