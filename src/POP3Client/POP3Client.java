/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Epulapp
 */
public class POP3Client extends Application {
    
    @Override
    public void start(Stage primaryStage) {
       
        View vw = new View();
        StackPane root = new StackPane();
        root.getChildren().add(vw.getRootNode());
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Pop3 client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
