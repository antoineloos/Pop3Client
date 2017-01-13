/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POP3Client;

import com.jfoenix.controls.JFXListCell;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Epulapp
 */
public class ListItem extends JFXListCell {  
    private static final String CACHE_LIST_FOUND_CLASS = "cache-list-found";
    private static final String CACHE_LIST_NOT_FOUND_CLASS = "cache-list-not-found";
    private static final String CACHE_LIST_NAME_CLASS = "cache-list-name";
    private static final String CACHE_LIST_DT_CLASS = "cache-list-dt";
    private static final String CACHE_LIST_ICON_CLASS = "cache-list-icon";
    private static final String FONT_AWESOME = "FontAwesome";

    private GridPane grid = new GridPane();
    private Label icon = new Label();
    private Label name = new Label();
    private Label dt = new Label();

    public ListItem() {
        configureGrid();        
        configureIcon();
        configureName();
        configureDifficultyTerrain();
        addControlsToGrid(); 
        addContent();
        this.setPrefWidth(300);
        this.setPrefHeight(50);
    }

    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 10, 0, 10));
    }

    private void configureIcon() {
        icon.setFont(Font.font(FONT_AWESOME, FontWeight.BOLD, 24));
        icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
    }

    private void configureName() {
        name.getStyleClass().add(CACHE_LIST_NAME_CLASS);
    }

    private void configureDifficultyTerrain() {
        dt.getStyleClass().add(CACHE_LIST_DT_CLASS);
    }

    private void addControlsToGrid() {
        grid.add(icon, 0, 0, 1, 2);                    
        grid.add(name, 1, 0);        
        grid.add(dt, 1, 1);
    }

    public void updateItem(Mail mail, boolean empty) {
        super.updateItem(mail, empty);
        if (empty) {
            clearContent();
        } else {
            addContent();
        }
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void addContent() {
        setText(null);
        
        name.setText("blibli");
        dt.setText("blublu"+" / "+ "bloblo");
               
        setGraphic(grid);
    }

  
}
