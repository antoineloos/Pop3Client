/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Epulapp
 */
public class Mail {
    private int numero;
    private String contenu;
    private boolean supprime;
    private int taille;
    private String replyTo = "";
    private String from = "";
    private String to = "";
    private String subject = "";
    private String mimeVersion = "";
    private String body = "";
    
    public Mail(int numero, int taille, String contenu) {
        this.numero = numero;
        this.contenu = contenu;
        this.taille = taille;
        this.supprime = false;
        parseMail();
    }
    
    public void supprimer()
    {
        this.supprime = true;
    }
    
    public void retablir()
    {
        this.supprime = false;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @return the contenu
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * @return the supprime
     */
    public boolean isSupprime() {
        return supprime;
    }

    /**
     * @return the taille
     */
    public int getTaille() {
        return taille;
    }
    
    private void parseMail()
    {
        Matcher m = Pattern.compile("Reply-To:\\s([a-zA-Z0-9]|\\.|@)+\\r?\\n").matcher(this.contenu);
        if (m.find()) {
            this.replyTo = m.group(1);
        }
        m = Pattern.compile("From:\\s([a-zA-Z0-9]|\\.|@)+\\r?\\n").matcher(this.contenu);
        if (m.find()) {
            this.from = m.group(1);
        }
        m = Pattern.compile("To:\\s([a-zA-Z0-9]|\\.|@)+\\r?\\n").matcher(this.contenu);
        if (m.find()) {
            this.to = m.group(1);
        }
        m = Pattern.compile("Subject:\\s(\\w|\\W)+\\r?\\n").matcher(this.contenu);
        if (m.find()) {
            this.subject = m.group(1);
        }
        m = Pattern.compile("MIME-Version:\\s([0-9]|\\.)+\\r?\\n").matcher(this.contenu);
        if (m.find()) {
            this.mimeVersion = m.group(1);
        }
        m = Pattern.compile("\\r?\\n\\r?\\n(\\w|\\W)$").matcher(this.contenu);
        if (m.find()) {
            this.body = m.group(1);
        }
    }

    /**
     * @return the replyTo
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return the mimeVersion
     */
    public String getMimeVersion() {
        return mimeVersion;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }
}
