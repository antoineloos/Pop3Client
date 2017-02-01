/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Epulapp
 */
public class CustomEventHandler implements EventHandler<ActionEvent> {
    public View view;
    public CustomEventHandler(View vw)
    {
        
        super();
        this.view = vw;
    }

    @Override
    public void handle(ActionEvent event) {
        
    }
    
}
