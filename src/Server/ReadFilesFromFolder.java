package Server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ReadFilesFromFolder{

    public void listOfFiles(String path,final File folder,Socket client) throws IOException {
        PrintWriter printWriter =new PrintWriter(client.getOutputStream());

        ArrayList<String> folders = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                folders.add(fileEntry.getName());
            } else {
                if (fileEntry.isFile()) {
                    files.add(fileEntry.getName());
                }

            }
        }
        printWriter.println(path);
        printWriter.flush();
        for(String s : folders){
            printWriter.println("Folder=" + s);
            printWriter.flush();
            ///System.out.println("To Client : "+"Folder = " +  s);
        }
        for(String s : files){
            printWriter.println("File=" + s);
            printWriter.flush();
            ///System.out.println("To Client : "+"File = " +  s);
        }
        printWriter.println("-->END<--");
        printWriter.flush();
    }
}