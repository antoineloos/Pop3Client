/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javax.swing.event.ChangeListener;


/**
 *
 * @author Epulapp
 */
public class View implements Observer  {

    private GridPane mainGrid;
    private FlowPane headerPane;
    private FlowPane loginPane;
    private FlowPane BodyPane;
    private BorderPane rootGrid;
    private JFXButton connectBtn;
    private ScrollPane scroolPane;
    private JFXButton loginBtn;
    private JFXButton disconnectBtn;
    private Mail selectedMail;
    private Label body;
    private Label from;
    private Label objet;
    private ThreadClient courantClt;
    private JFXListView<Mail> listView;
    private ObservableClient OThread;
    private JFXButton deleteBtn;
    public final String style =  "-fx-background-color: #00B4FF;" + "-fx-spacing: 50px;"+ "-fx-text-fill: white;"+"-fx-font: 16px \"Lato\";";
    public final String styleFieldH = "-fx-text-fill: white;"
            +"-fx-font: 16px \"Lato\";"
            
            +"-fx-focus-color: #00B4FF;"
            +"-fx-background-color: #515151" ;
    
    public final String styleField = "-fx-font: 16px \"Lato\";"
            
            +"-fx-focus-color: #00B4FF;"
            +"-fx-background-color: #FFFFFF" ;
    
     public View()
    {
        connectBtn= new JFXButton("Set");
        connectBtn.setStyle(style);
        
        
        loginBtn = new JFXButton("Login");
        loginBtn.setStyle(style);
       
        disconnectBtn = new JFXButton("Quit");
        disconnectBtn.setStyle(style);
        disconnectBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) 
            {
                try {
                    OThread.sendQuit();
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        headerPane = new FlowPane();
        headerPane.setOrientation(Orientation.HORIZONTAL);
        headerPane.getChildren().add(new WLabel("IP : "));
        JFXTextField tmp = new JFXTextField();
        tmp.setStyle(styleFieldH);
        
        JFXTextField tmp2 = new JFXTextField();
        tmp2.setStyle(styleFieldH);
        
        HBox spcBox = new HBox();
             spcBox.setPrefWidth(15);

        spcBox.setSpacing(15.0); //In your case
        connectBtn.setOnAction(new CustomEventHandler(this) {

            @Override
            public void handle(ActionEvent event) {
               
                try {
                    ThreadClient t = new ThreadClient(tmp.getText(),Integer.parseInt(tmp2.getText()));
                    courantClt = t;
                    OThread = t.getClient();
                    
                    OThread.addObserver(view);
                  
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        deleteBtn = new JFXButton("",new ImageView(new javafx.scene.image.Image("POP3Client/trash-can-icon-24.png")));
       
        deleteBtn.setStyle(style);
        deleteBtn.setPrefHeight(40);
        deleteBtn.setVisible(false);
        deleteBtn.setPrefWidth(40);
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               
            if(OThread!=null) try {
                OThread.sendDelete(selectedMail.getNumero());
                listView.getItems().remove(selectedMail);
            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        });
        
        headerPane.getChildren().add(tmp);
        headerPane.getChildren().add(new WLabel("Port : "));
        headerPane.getChildren().add(tmp2);
        headerPane.getChildren().add(connectBtn);
        headerPane.getChildren().add(spcBox);
        headerPane.getChildren().add(disconnectBtn);
        headerPane.setPrefWidth(800);
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setStyle("-fx-background-color: #515151;" );
        
        loginPane = new FlowPane();
        BodyPane = new FlowPane();
        JFXTextField tmp3 = new JFXTextField();
        tmp3.setStyle(styleField);
        tmp3.setPadding(new Insets(10,10,10,10));
        JFXTextField tmp4 = new JFXTextField();
        tmp4.setStyle(styleField);
        tmp4.setPadding(new Insets(10,10,10,10));
        
        listView = new JFXListView<Mail>();
        listView.setPrefHeight(0);
        listView.setPrefWidth(0);
        
        listView.setVisible(false);
        HBox hbox = new HBox(listView);
        listView.setCellFactory(param -> new ListCell<Mail>() {
        @Override
        protected void updateItem(Mail item, boolean empty)
        {
            super.updateItem(item, empty);
            Platform.runLater(new Runnable(){
                

                @Override
                public void run() {
                    
                if(item!=null)
                {
                    setText("From : "+item.getFrom()+"\n"+"Objet : "+item.getSubject());
                }
                else setText(""); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
    });
        
        
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMail = newSelection;
                if(selectedMail!=null) deleteBtn.setVisible(true);
                body.setText( newSelection.getBody());
                from.setText("From : "+newSelection.getFrom());
                objet.setText("Objet : "+newSelection.getSubject());
            }
        });
        body = new Label();
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.JUSTIFY);
        body.setPrefWidth(400);
        from = new Label();
        from.setWrapText(true);
        from.setTextAlignment(TextAlignment.JUSTIFY);
        from.setPrefWidth(400);
        objet = new Label();
        objet.setWrapText(true);
        objet.setTextAlignment(TextAlignment.JUSTIFY);
        objet.setPrefWidth(400);
        BodyPane.getChildren().add(from);
        BodyPane.getChildren().add(objet);
        BodyPane.getChildren().add(body);
        BodyPane.getChildren().add(deleteBtn);
        BodyPane.setOrientation(Orientation.VERTICAL);
        BodyPane.setAlignment(Pos.TOP_LEFT);
        BodyPane.setPrefHeight(400);
        BodyPane.setPrefWidth(500);
        BodyPane.setPadding(new Insets(10,10,10,10));
        BodyPane.setVgap(5);
        BodyPane.setHgap(5);
        
        
        
        loginPane.getChildren().add(new Label("Adresse : "));
        loginPane.getChildren().add(tmp3);
        loginPane.getChildren().add(new Label("Mot de passe : "));
        loginPane.getChildren().add(tmp4);
        loginBtn.setAlignment(Pos.CENTER);
         loginBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                loginPane.setVisible(false);
                listView.setVisible(true);
                OThread.setUserName(tmp3.getText());
                OThread.setPassword(tmp4.getText());
                listView.setPrefHeight(40);
                listView.setPrefWidth(200);
                BodyPane.setVisible(true);
                courantClt.launch();
                if(rootGrid!=null) rootGrid.setCenter(BodyPane);
            }
        });
        loginPane.getChildren().add(loginBtn);
        loginPane.setOrientation(Orientation.VERTICAL);
        loginPane.setAlignment(Pos.CENTER);
        
        
        
       
        rootGrid = new BorderPane();
        
        rootGrid.setTop(headerPane);
        rootGrid.setLeft(listView);
        rootGrid.setCenter(loginPane);
        
        rootGrid.setPrefHeight(600);
        rootGrid.setPrefWidth(800);
        
        mainGrid = new GridPane();
        mainGrid.getChildren().add(rootGrid);
        mainGrid.setStyle("-fx-background-color: #FFFFFF;");
    }
    
    @Override
    public void update(Observable o, Object arg) {
        listView.getItems().clear();
        ObservableClient obs = (ObservableClient)o;
        ArrayList<Mail> lst = obs.getMails();
        for(Mail elem: lst)
        {
            listView.getItems().add(elem);
        }
        
    }
    
    public GridPane getRootNode()
    {
        return this.mainGrid;
    }
    
}
