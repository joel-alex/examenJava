/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class MainController implements Initializable {
    private static final Logger LOG=Logger.getLogger(MainController.class.getName());

    @FXML
    private TabPane tabPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    private void marcaAction(ActionEvent event){
         this.cargarPestania("marca");
    }

    @FXML
    private void productoAction(ActionEvent event) {
          this.cargarPestania("producto");
      
    }
    private void cargarPestania(String nombre){
        try{
            FXMLLoader loader = new FXMLLoader();
            AnchorPane vistaMarca = loader.load(MainController.class.getResourceAsStream(nombre+".fxml"));
            Tab t=new Tab();
            t.setText(nombre);
            t.setContent(vistaMarca);
            this.tabPane.getTabs().add(t);
            this.tabPane.getSelectionModel().select(t);
        }catch(IOException e){
            LOG.log(Level.SEVERE, "Error al cargar FXML de "+nombre, e);
        }
        }
    
}
