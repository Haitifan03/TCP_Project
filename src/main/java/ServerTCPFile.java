import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerTCPFile {
    static ArrayList<String> fileList;

    public static void main(String @NotNull [] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: ServerTCP <port>");
            return;
        }
        fileList = getList();

        int port = Integer.parseInt(args[0]);

        ServerSocketChannel listenChannel = ServerSocketChannel.open();

        listenChannel.bind(new InetSocketAddress(port));

        // lst
        // del fileName
        // rnm fileName newName
        // upl fileName
        // dld fileName

        while (true) {
            SocketChannel serveChannel = listenChannel.accept();
            System.out.println("Waiting for a request:");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            serveChannel.read(buffer);
            buffer.flip();
            byte[] bytes = buffer.array();
            String request = new String(bytes);
            System.out.println(request);
            buffer.rewind();

            String[] requestArray = request.split(" ");
            String command = requestArray[0];
            System.out.println("The command is " + command);
            String response = "";

            if (command.contains("lst")){
                for (int x =0; x < getList().size(); x++){
                    response += getList().get(x) + ", ";
                }
            }
            else{
                String fileName = requestArray[1];
                System.out.println("The fileName is " + fileName);

                if (command.contains("del")){
                    delete(fileName);
                    updateList();
                    response = fileName + " deleted";
                }
                else if (command.contains("rnm")){
                    String newName = requestArray[2];
                    System.out.println("The new name is " + newName);
                    rename(fileName, newName);
                    updateList();
                    response = fileName + " renamed to " + newName;
                }
                else if (command.contains("uld")){
                    response = "file is being received.";
                    File file = new File(System.getProperty("user.dir") + "/src/main/java/" + fileName);
                    addFileToList(fileName);
                    updateList();
                    buffer = ByteBuffer.allocate(1024);
                    serveChannel.read(buffer);
                    buffer.flip();
                    byte[] byts = buffer.array();
                    writeByteArrayToFile(byts, file);
                }
                else if (command.contains("dld")){
                    response = "Sending file back now";
                    File file = new File(System.getProperty("user.dir") + "/src/main/java/" + fileName);
                    serveChannel.write(ByteBuffer.wrap(fileToByteArray(file)));
                }
            }

            System.out.println("The response is " + response);

            serveChannel.write(ByteBuffer.wrap(response.getBytes()));
            serveChannel.close();
        }
    }

    public static void delete(String fileName) {
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

    public static void rename(String fileName, String newName) {
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
    public static void addFileToList(String filename){
        fileList.add(filename);
    }
    public static void updateList(){
        File fileListFile = new File(System.getProperty("user.dir") + "/src/main/java/FileList");
        try (PrintWriter author = new PrintWriter(fileListFile)){
            for (String s : fileList) {
                author.println(s);
            }
        } catch (Exception e){

        }
    }

    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }
    public static void writeByteArrayToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }
}
