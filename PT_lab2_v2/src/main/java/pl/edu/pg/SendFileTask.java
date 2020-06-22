package pl.edu.pg;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;

public class SendFileTask extends Task<Void> {
    private File file;
    private String host;
    private int port;

    public SendFileTask(File file, String host, int port) {
        this.file = file;

        this.host = host;
        this.port = port;
    }


    @Override
    protected Void call() throws Exception {
        updateMessage("Initiating...");

        try (Socket socket = new Socket(this.host, this.port)){
            updateMessage("Connected to the server");
            try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                dos.writeUTF(file.getName());

                try (InputStream fis = new FileInputStream(file.getPath())) {

                    int progressBarStatus = 0;

                    byte[] buffer = new byte[4096]; //bufor 4KB

                    int readSize;
                    while ((readSize = fis.read(buffer)) != -1) {

                        dos.write(buffer, 0, readSize);

                        progressBarStatus += readSize;
                        updateProgress(progressBarStatus, file.length());
                        updateMessage("Sending file");

                    }

                    updateMessage("File has been sent");

                }catch (IOException e){
                    updateMessage("Error with file transferring");
                    updateProgress(0, 100);
                    e.printStackTrace();
                }

            }catch (IOException e){
                updateMessage("Error with data stream"); //?
                updateProgress(0, 100);
                e.printStackTrace();
            }

        }catch (IOException e){
            updateMessage("Server connection error");
            updateProgress(0, 100);
            e.printStackTrace();
        }


        return null;
    }
}
