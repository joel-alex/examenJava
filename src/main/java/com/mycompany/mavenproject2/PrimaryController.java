package com.mycompany.mavenproject2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {

    private static final Logger LOG = Logger.getLogger(PrimaryController.class.getName());

    @FXML
    private TextField codText;
    @FXML
    private TextField descripText;

    private Connection con = null;
    @FXML
    private TableView<Marca> idTabla;
    @FXML
    private TableColumn<Marca, Integer> idCodigo;
    @FXML
    private TableColumn<Marca, String> idDescripcion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.idDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyecto_investigacion", "admin", "Mysql.123456");

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Error de conexión");
            al.setHeaderText("No se puede conectar a la base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();
            System.exit(1);
        }
        this.cargarDatos();
        
        this.idTabla.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Marca> obs,Marca valorAnterior, Marca valorNuevo)->{
               if(valorNuevo!=null){
                   this.codText.setText(valorNuevo.getCodigo().toString());
                   this.descripText.setText(valorNuevo.getDescripcion());
               }
    });
    }

    @FXML
    private void registrar(ActionEvent event) throws SQLException {

        String id = this.codText.getText();
        String descripcion = this.descripText.getText();
         if (id.isEmpty() || descripcion.isEmpty()){
           Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error!");
            a.setHeaderText("Complete todos los campos.");
            a.show();     
        }else{
             try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyecto_investigacion", "admin", "Mysql.123456");
            String sql = "INSERT INTO marca(descripcion) VALUES(?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.execute();
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Éxito");
            al.setHeaderText("Marca guardada correctamente");
            al.show();
            this.descripText.clear();
            this.codText.clear();
            this.cargarDatos();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Error de conexión");
            al.setHeaderText("No se puede insertar a la base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();

        }
         }
        
    }
    private void cargarDatos(){
        this.idTabla.getItems().clear();
        try {
            String sql = "SELECT * FROM marca";
            Statement stm = this.con.createStatement();
            ResultSet resultado=stm.executeQuery(sql);
            while(resultado.next()){
                Integer cod=resultado.getInt("idmarca");
                String desc=resultado.getString("descripcion");
                Marca m = new Marca(cod, desc);
                this.idTabla.getItems().add(m);
            }    
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        String id = this.codText.getText();
        String descripcion = this.descripText.getText();
        String sql = "UPDATE marca SET descripcion = ? WHERE idmarca = ?";
        if (descripcion.isEmpty()){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error al Editar");
            a.setHeaderText("Ingrese una descripción");
            a.show();
        }else{
             try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.setInt(2, Integer.parseInt(id));
            stm.execute();
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Éxito");
            al.setHeaderText("Marca editada correctamente");
            al.show();
            this.descripText.clear();
            this.codText.clear();
            this.cargarDatos();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al editar", ex);
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Error de conexión");
            al.setHeaderText("No se puede editar registro en la base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();

        }
        }
    }


    @FXML
    private void eliminar(ActionEvent event) {
         String strCodigo = this.codText.getText();
        String strDescripcion = this.descripText.getText();
        
        if (strCodigo.isEmpty()){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error al eliminar");
            a.setHeaderText("Ingrese un codigo");
            a.show();
        }else{
            Alert alConfirm = new Alert(AlertType.CONFIRMATION);
            alConfirm.setTitle("Confirmar");
            alConfirm.setHeaderText("Desea eliminar la marca?");
            alConfirm.setContentText(strCodigo+" "+strDescripcion);
            Optional<ButtonType> accion = alConfirm.showAndWait();
            
            if(accion.get().equals(ButtonType.OK)){
               try{
                String sql = "DELETE FROM marca WHERE  idmarca = ?";
                PreparedStatement stm = con.prepareStatement(sql);
                Integer cod= Integer.parseInt(strCodigo);
                stm.setInt(1, cod);
                stm.execute();
                int cantidad = stm.getUpdateCount();
                if (cantidad ==0){
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Error al eliminar");
                    a.setHeaderText("No existe la marca con codigo"+strCodigo);
                    a.show();
                }else{
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Eliminado");
                    a.setHeaderText("Marca aliminada correctamente");
                    a.show();
                    this.descripText.clear();
                    this.codText.clear();
                    this.cargarDatos();
                }
            }catch(SQLException ex){
                LOG.log(Level.SEVERE, "Error al eliminar", ex);
            } 
            }
        }
    }
}
