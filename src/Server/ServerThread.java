package Server;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread{

    String line=null;
    BufferedReader  bufferedReaderClientInputStream = null;
    PrintWriter printWriterClientOutputStream = null;
    Socket client=null;
    ReadFilesFromFolder readFilesFromFolder =  new ReadFilesFromFolder();
    String path = "C:/Users/meher/Documents";

    public ServerThread(Socket s){
        client=s;
    }

    public void run() {
        try{
            bufferedReaderClientInputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printWriterClientOutputStream=new PrintWriter(client.getOutputStream());

        }catch(IOException e){
            e.printStackTrace();
        }

        try {
            do{
                line=bufferedReaderClientInputStream.readLine();
                if(line==null) continue;
                if(line.equals("**Upload**")){
                    String uploadFilePath = bufferedReaderClientInputStream.readLine();
                    long fileLength = Long.parseLong(bufferedReaderClientInputStream.readLine());
                    System.out.println("File size " + fileLength);
                    byte[] contents = new byte[10000];

                    FileOutputStream fileOutputStream = new FileOutputStream(uploadFilePath);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    InputStream inputStream = client.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    int bytesRead = 0;
                    long recieved = 0;

                    while((bytesRead = bufferedInputStream.read(contents))!=-1) {
                        bufferedOutputStream.write(contents, 0, bytesRead);
                        recieved+=bytesRead;
                        if(recieved>=fileLength) break;
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                    System.out.println("***File saved successfully!");
                }
                else if(line.equals("**Download**")){
                    String downloadFilePath = bufferedReaderClientInputStream.readLine();
                    File file = new File(downloadFilePath);
                    printWriterClientOutputStream.println(file.length());
                    System.out.println("FIle length " + file.length());
                    printWriterClientOutputStream.flush();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                    OutputStream outputStream = client.getOutputStream();

                    byte[] contents;
                    long fileLength = file.length();
                    long current = 0;

                    while(current!=fileLength){
                        int size = 100000;
                        if(fileLength - current >= size)
                            current += size;
                        else{
                            size = (int)(fileLength - current);
                            current = fileLength;
                        }
                        contents = new byte[size];
                        bufferedInputStream.read(contents, 0, size);
                        outputStream.write(contents);
                        ///System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
                    }

                    bufferedInputStream.close();
                    fileInputStream.close();
                    System.out.println("File sent succesfully!");
                }
                else if(line.equals("**CreateFolder**")){
                    String createFolderPath = bufferedReaderClientInputStream.readLine();
                    File newFolder = new File(createFolderPath);
                    if(!newFolder.exists()){
                        if(newFolder.mkdir()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }
                    else System.out.println("Directory already exists.");
                }
                else if(line.equals("**Login**")){
                    File folder = new File(path);
                    readFilesFromFolder.listOfFiles(path,folder,client);
                }
                else if(line.equals("**Back**")){
                    int n = path.lastIndexOf('/');
                    path = path.substring(0,n);
                    File folder = new File(path);
                    readFilesFromFolder.listOfFiles(path,folder,client);
                }
                else{
                    if(!line.equals("**Refresh**")) {
                        path +="/";
                        path+=line;
                    }
                    File folder = new File(path);
                    readFilesFromFolder.listOfFiles(path,folder,client);
                }
            }while(true);
        } catch(NullPointerException e){
            System.out.println("Null pointer");
            e.printStackTrace();
        }catch (IOException e) {

            e.printStackTrace();
        }


        finally{
            try{
                System.out.println("Connection Closing..");
                bufferedReaderClientInputStream.close();
                printWriterClientOutputStream.close();
                client.close();
            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
}