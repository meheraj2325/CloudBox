package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {

    public static void main(String args[]) throws IOException{


        InetAddress address=InetAddress.getLocalHost();
        Socket client=null;
        String line=null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;

        try {
            client=new Socket(address, 4444);
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(client.getInputStream()));
            os= new PrintWriter(client.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        System.out.println("Enter your name: ");
        String name = br.readLine();
        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response=null;
        try{
            do{
                line=br.readLine();
                os.println(line);
                os.flush();
                do{
                    response=is.readLine();
                    System.out.println("From Server : "+response);
                }while(!response.equalsIgnoreCase("-->END<--"));
            }while(line.compareTo("QUIT")!=0);



        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{

            is.close();
            os.close();
            br.close();
            client.close();
            System.out.println("Connection Closed");
        }

    }
}
