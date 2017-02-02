package POP3Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
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
    private String password = "lordgaben";
    private Socket socket;
    private int nb_mails = 0;
    private int tailleBoite = 0;
    private ArrayList<Mail> mails;
    private int messageDemande = 0;
    private boolean recuperationBoite = false;
    private String exception;
    
    
    public ObservableClient(Socket socket)
    {
        mails = new ArrayList<Mail>();
        this.socket = socket;
    }
    
    
    
    public void traiteEvt(String code, String msg) throws IOException, Exception
    {
        try{
        Matcher m;
        System.out.println(msg);
        switch(code) {
            case "+OK" :
                    switch(getEtat()) {
                        case "Connexion open" :
                            m = Pattern.compile("^POP3 server ready\\s?(<.*>)$").matcher(msg);
                            if (m.find()) {
                                this.setEtat("Authorization");
                                this.sendAPOP(m.group(1));
                                System.out.println("APOP sended");
                            }
                            break;
                        case "Authorization" :
                            m = Pattern.compile("^maildrop has ([0-9]+) messages \\(([0-9]+) octets\\)$").matcher(msg);
                            if (m.find()) {
                                this.nb_mails = Integer.parseInt(m.group(1));
                                this.tailleBoite = Integer.parseInt(m.group(2));
                                this.setEtat("Transaction");
                                this.recupererBoite();
                            }
                            
                            break;
                        case "Transaction" :

                            this.traiteTransaction(msg);
                            this.setChanged();
                            this.notifyObservers();
                            break;
                        case "Update" :
                            socket.close();
                            this.setEtat("Connexion closed");
                        default :
                            break;
                    }
                break;
            case "-ERR" :
                    switch(getEtat()) {
                        case "Connexion open" :
                            this.cptError ++;
                            if (cptError >= 5) {
                                this.setEtat("Connexion closed");
                                throw new Exception(msg);
                            }
                            break;
                        case "Authorization" :
                            this.setEtat("Connexion open");
                            throw new Exception(msg);

                        case "Transaction" :
                            this.erreurTransaction(msg);
                            break;
                        case "Update" :
                            socket.close();
                            this.setEtat("Connexion closed");
                            throw new Exception(msg);
                        default :
                            break;
                    }
                break;
            default :
                break;
        }
         
        }
        catch(Exception ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void sendAPOP(String timestamp) throws IOException
    {
        
        String MD5;
        MD5 = this.getMD5(timestamp);
        String request = "APOP " + this.getUserName() + " " +MD5;
        sendRequest(request);
        
        
    }
    
    private void sendRetrieve(int numero) throws IOException
    {
        if (numero >= 0) {
            String request = "RETR "+numero;
            sendRequest(request);
        } 
    }
    
    private void traiteTransaction(String msg) throws IOException, Exception
    {
        try{
        Matcher m = Pattern.compile("^([0-9]+) octets\\s?\\r?\\n((.|\\W)+)$").matcher(msg);
        if (m.find()) {
            int taille = Integer.parseInt(m.group(1));
            String szContenu = m.group(2);
            Mail mail = new Mail(messageDemande, taille, szContenu);
            getMails().add(mail);
            if (recuperationBoite == true) {
                if (this.messageDemande < getNb_mails() - 1) {
                    this.messageDemande++;
                    recupereMessage(this.messageDemande);

                    
                } else {
                    this.messageDemande = 0;
                    this.recuperationBoite = false;
                }
            } else {
                this.messageDemande = 0;
            }
        } else {
            m = Pattern.compile("^([0-9]+)\\s([0-9]+)$").matcher(msg);
            if (m.find()) {
                this.nb_mails = Integer.parseInt(m.group(1));
                this.tailleBoite = Integer.parseInt(m.group(1));
            } else {
                m = Pattern.compile("^message\\s([0-9]+)\\sdeleted$").matcher(msg);
                if (m.find()) {
                    final int mailToDel =  Integer.parseInt(m.group(1));
                    Optional<Mail> mail = getMails().stream().filter(x -> mailToDel == x.getNumero()).findFirst();
                    if (mail.isPresent()) {
                        Mail buffer = mail.get();
                        buffer.supprimer();  
                    } else {
                        throw new Exception("Mail not found locally.");
                    }
                } else {
                    m = Pattern.compile("^maildrop\\shas\\s([0-9]+)\\smessages\\s\\(([0-9]+)\\soctets\\)$").matcher(msg);
                    if (m.find()) {
                        nb_mails = Integer.parseInt(m.group(1));
                        tailleBoite = Integer.parseInt(m.group(2));
                        for (Mail mail : getMails()) {
                            mail.retablir();
                        }
                    }
                }
            }
        }
        }
        catch(Exception ex){System.err.println(ex.getMessage());}
    }
    
    private void erreurTransaction(String msg) throws Exception
    {
        Matcher m = Pattern.compile("^message\\s([0-9]+)\\salready\\sdeleted$").matcher(msg);
        if (m.find()) {
            throw new Exception(msg);
        }
    }
    
    private String getMD5(String timestamp) {
        String md5 = getSomme(this.getPassword(), timestamp);
        return md5;
    }
    
    private void sendRequest(String requesttmp) throws IOException
    {
        String request = requesttmp + "\n";
        Thread t= new Thread(new Runnable() {
           
            @Override
            public void run() {
                BufferedOutputStream out;
                
                try {
                    
                    out = new BufferedOutputStream(socket.getOutputStream());
                    out.write(request.getBytes());
                    System.out.println(request);
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ObservableClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        t.start();
    }
    
    public void recupereMessage(int nNumMessage) throws IOException
    {

        this.sendRetrieve(nNumMessage);
        
    }
    
    public void recupererBoite() throws IOException
    {
        if (this.getNb_mails() > 0) {
            this.recuperationBoite = true;
            this.messageDemande = 0;
            this.recupereMessage(messageDemande);
        }
    }
    
    public void sendStat() throws IOException
    {
        String request = "STAT";
        sendRequest(request);
    }
    
    public void sendDelete(int nNumMessage) throws IOException
    {
        String request = "DELE "+nNumMessage;
        sendRequest(request);
    }
    
    public void sendReset() throws IOException
    {
        String request = "RSET";
        sendRequest(request);
        
    }
    
    public void sendQuit() throws IOException
    {
        String request = "QUIT";
        sendRequest(request);
        if ("Transaction".equals(this.getEtat())) {
            setEtat("Update");
        }
        this.notifyObservers();
    }

    /**
     * @return the etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the nb_mails
     */
    public int getNb_mails() {
        return nb_mails;
    }

    /**
     * @return the tailleBoite
     */
    public int getTailleBoite() {
        return tailleBoite;
    }

    /**
     * @return the mails
     */
    public ArrayList<Mail> getMails() {
        return mails;
    }

    /**
     * @return the exception
     */
    public String getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception.getMessage();
        System.out.println(exception.getMessage());
        setChanged();
        notifyObservers();
    }

    /**
     * @param etat the etat to set
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    public static String getSomme(String mdp, String timbre) {
        String somme = null;
        MessageDigest md;
        try {
            String message = timbre + mdp;
            md = MessageDigest.getInstance("md5");
            md.update(message.getBytes());

            byte[] digest = null;
            digest = md.digest();
           // System.err.println(bytesToHex(digest));

            somme = bytesToHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Erreur lors de la génération de la somme de controle");
        }
        return somme;
    }

    public static String bytesToHex(byte[] b) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buffer.append(hexDigits[(b[j] >> 4) & 0x0f]);
            buffer.append(hexDigits[b[j] & 0x0f]);
        }
        return buffer.toString();
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
