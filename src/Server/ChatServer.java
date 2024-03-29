package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {
    public static void main(String args[]){


        Socket client = null;
        ServerSocket server=null;
        System.out.println("Server Listening......");
        try{
            server = new ServerSocket(4444);

        }
        catch(IOException e){
            e.printStackTrace();

        }
        ArrayList<Socket> clientList = new ArrayList<>();

        while(true){
            try{
                client = server.accept();
                System.out.println("connection Established");
                ServerThread ST=new ServerThread(client);
                ST.start();
                clientList.add(client);
            }

            catch(Exception e){
                e.printStackTrace();

            }
        }

    }

}
