/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

/**
 *
 * @author Epulapp
 */
public class Mail {
    private int numero;
    private String contenu;
    private boolean supprime;
    private int taille;
    
    public Mail(int numero, int taille, String contenu) {
        this.numero = numero;
        this.contenu = contenu;
        this.taille = taille;
        this.supprime = false;
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
}
