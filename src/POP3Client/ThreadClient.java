/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Epulapp
 */
public class ThreadClient extends Thread {

    private ObservableClient client;
    private String ip_serv;
    private int port;
    private Socket socket;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private boolean actif = false;

    public ThreadClient(String ip_serv, int port) throws IOException {
        this.ip_serv = ip_serv;
        this.port = port;
        this.socket = new Socket(ip_serv, port);
        this.client = new ObservableClient(socket);
        try {
            in = new BufferedInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void launch() {
        this.actif = true;
        this.start();
    }
    
    public void halt(){
        this.actif = false;
    }

    @Override
    public void run() {
        try {
            this.InitListener();
        } catch (Exception ex) {
            client.setException(ex);
        }
    }

    private void InitListener() throws IOException, Exception {
        while (actif == true) {
            String request = "";
            int buffersize = in.available();
            while (buffersize > 0) {
                if (buffersize > 512) {
                    buffersize = 512;
                }
                byte[] buffer = new byte[buffersize];
                in.read(buffer);
                request += Arrays.toString(buffer);
            }

            Matcher m = Pattern.compile("^(\\+OK|\\-ERR)\\s((.|\\s)+)$").matcher(request);
            if (m.find()) {
                String code = m.group(1);
                String msg = m.group(2);
                client.traiteEvt(code, msg);
            }
        }
    }
    
    

}
