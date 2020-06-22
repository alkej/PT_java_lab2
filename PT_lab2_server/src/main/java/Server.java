import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.*;

public class Server {

    private int port;

    private ExecutorService executor;

    public Server(int port){
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
    }

    public void go(){
        try(ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server started");


            while (true){

                Socket client = serverSocket.accept();
                System.out.println("Accepted connection " + client);

                this.executor.submit(() ->
                        ClientHandler(client));

            }

        }catch (IOException e){
            System.out.println("Server starting error");
            e.printStackTrace();
        }

    }

    private void ClientHandler(Socket client){
        try (DataInputStream dis = new DataInputStream(client.getInputStream())){


            String fileName = dis.readUTF();

            File file = new File("./files", fileName);

            try {
                boolean creation_result;
                while (true){
                    creation_result = file.createNewFile();

                    if (creation_result){
                        break;
                    }else{

                        String[] splitted = fileName.split("[.]");
                        int last_index = splitted.length - 1;
                        String ext = splitted[last_index];
                        String name = Long.toString(Calendar.getInstance().getTimeInMillis());

                        String file2Name = name + "." + ext;
                        File file2 = new File("./files", file2Name);

                        file.renameTo(file2);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }


            try (OutputStream os = new FileOutputStream(file.getPath())) {

                byte[] buffer = new byte[4096]; //bufor 4KB

                int readSize;
                while ((readSize = dis.read(buffer)) != -1) {
                    os.write(buffer, 0, readSize);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }catch (IOException e){
            e.printStackTrace();

        }finally {
            try {
                client.close();
            }catch (IOException e ){
                e.printStackTrace();
            }
        }
    }


}
