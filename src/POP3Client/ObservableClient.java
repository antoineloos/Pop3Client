package POP3Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Epulapp
 */
public class ObservableClient extends Observable{
    
    private String etat = "Connexion closed";
    private int cptError = 0;
    private String userName = "kdebbiche";
    private Socket socket;
    private int nb_mails = 0;
    private int tailleBoite = 0;
    
    
    public ObservableClient(Socket socket)
    {
        this.socket = socket;
    }
    
    
    public void traiteEvt(String code, String msg) throws IOException
    {
        Matcher m;
        switch(code) {
            case "+OK" :
                    switch(etat) {
                        case "Connexion open" :
                            m = Pattern.compile("^POP3 server ready\\s?(.+)$").matcher(msg);
                            if (m.find()) {
                                this.etat = "Authorization";
                                this.sendAPOP(m.group(1));
                            }
                            break;
                        case "Authorization" :
                            m = Pattern.compile("^maildrop has ([0-9]+) message \\(([0-9]+) octets\\)$").matcher(msg);
                            if (m.find()) {
                                this.nb_mails = Integer.parseInt(m.group(1));
                                this.tailleBoite = Integer.parseInt(m.group(2));
                                this.etat = "Transaction";
                                this.recupereBoite();
                            }
                            
                            break;
                        case "Transaction" :
                            this.traiteTransaction(msg);
                        default :
                            break;
                    }
                break;
            case "-ERR" :
                    switch(etat) {
                        case "Connexion open" :
                            this.cptError ++;
                            if (cptError >= 5) {
                                this.etat = "Connexion closed";
                            }
                            break;
                        case "Authorization" :
                            this.etat = "Connexion open";
                            break;
                        default :
                            break;
                    }
                break;
            default :
                break;
        }
        this.notifyObservers();
    }
    
    private void sendAPOP(String timestamp) throws IOException
    {
        
        String MD5;
        MD5 = this.getMD5(timestamp);
        String request = "APOP " + this.userName + " " +MD5;
        sendRequest(request);
        
        
    }
    
    private void sendRetrieve(int numero) throws IOException
    {
        if (numero > 0) {
            String request = "RETR "+numero;
            sendRequest(request);
        }
       
    }
    
    private void traiteTransaction(String msg)
    {
        Matcher m = Pattern.compile("^([0-9]+) octets\\s?\\r?\\n(.|\\s|\\n|\\r)+$").matcher(msg);
        if (m.find()) {
            
        }
    }
    
    private String getMD5(String timestamp) {
        return "";
    }
    
    private void sendRequest(String request) throws IOException
    {
        Thread t= new Thread(new Runnable() {

            @Override
            public void run() {
                BufferedOutputStream out;
                try {
                    out = new BufferedOutputStream(socket.getOutputStream());
                    out.write(request.getBytes());
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        t.start();
    }
    
    public void recupereBoite() throws IOException
    {
        for (int i = 0; i < this.nb_mails; i++) {
            this.sendRetrieve(i);
        }
    }
    
}
