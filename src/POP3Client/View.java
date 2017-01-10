/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.util.Observable;
import java.util.Observer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Epulapp
 */
public class View implements Observer  {

    private GridPane mainGrid;
    private FlowPane headerPane;
    private FlowPane loginPane;
    private BorderPane rootGrid;
    private JFXButton connectBtn;
    private ScrollPane scroolPane;
    private JFXButton loginBtn;
    
    public final String style =  "-fx-background-color: #00B4FF;" + "-fx-spacing: 50px;"+ "-fx-text-fill: white;"+"-fx-font: 16px \"Lato\";";
    public final String styleFieldH = "-fx-text-fill: white;"
            +"-fx-font: 16px \"Lato\";"
            +"-fx-skin: \"com.jfoenix.android.skins.JFXTextFieldSkinAndroid\";"
            +"-fx-focus-color: #00B4FF;"
            +"-fx-background-color: #515151" ;
    
    public final String styleField = "-fx-font: 16px \"Lato\";"
            +"-fx-skin: \"com.jfoenix.android.skins.JFXTextFieldSkinAndroid\";"
            +"-fx-focus-color: #00B4FF;"
            +"-fx-background-color: #FFFFFF" ;
    
     public View()
    {
        connectBtn= new JFXButton("Connect");
        connectBtn.setStyle(style);
        
        loginBtn = new JFXButton("Connexion");
        loginBtn.setStyle(style);
        
        headerPane = new FlowPane();
        headerPane.setOrientation(Orientation.HORIZONTAL);
        headerPane.getChildren().add(new WLabel("IP : "));
        JFXTextField tmp = new JFXTextField();
        tmp.setStyle(styleFieldH);
        
        JFXTextField tmp2 = new JFXTextField();
        tmp2.setStyle(styleFieldH);
        headerPane.getChildren().add(tmp);
        headerPane.getChildren().add(new WLabel("Port : "));
        headerPane.getChildren().add(tmp2);
        headerPane.getChildren().add(connectBtn);
        headerPane.setPrefWidth(800);
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setStyle("-fx-background-color: #515151;" );
        
        loginPane = new FlowPane();
        JFXTextField tmp3 = new JFXTextField();
        tmp3.setStyle(styleField);
        tmp3.setPadding(new Insets(10,10,10,10));
        JFXTextField tmp4 = new JFXTextField();
        tmp4.setStyle(styleField);
        tmp4.setPadding(new Insets(10,10,10,10));
        
        loginPane.getChildren().add(new Label("Adresse : "));
        loginPane.getChildren().add(tmp3);
        loginPane.getChildren().add(new Label("Mot de passe : "));
        loginPane.getChildren().add(tmp4);
        loginBtn.setAlignment(Pos.CENTER);
        loginPane.getChildren().add(loginBtn);
        loginPane.setOrientation(Orientation.VERTICAL);
        loginPane.setAlignment(Pos.CENTER);
        
        
        
        rootGrid = new BorderPane();
        
        rootGrid.setTop(headerPane);
        rootGrid.setCenter(loginPane);
        rootGrid.setPrefHeight(600);
        rootGrid.setPrefWidth(800);
        
        mainGrid = new GridPane();
        mainGrid.getChildren().add(rootGrid);
        mainGrid.setStyle("-fx-background-color: #FFFFFF;");
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
    }
    
    public GridPane getRootNode()
    {
        return this.mainGrid;
    }
    
}
