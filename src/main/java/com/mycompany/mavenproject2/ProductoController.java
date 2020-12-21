/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductoController implements Initializable {

    private static final Logger LOG = Logger.getLogger(ProductoController.class.getName());

    @FXML
    private TableColumn<producto, Integer> idCod;
    @FXML
    private TableColumn<producto, String> idDescripcion;
    private ConexionBD conex;
    @FXML
    private TableView<producto> idTable;
    @FXML
    private TableColumn<producto, Integer> idCantidad;
    @FXML
    private TableColumn<producto, Float> idIva;
    @FXML
    private TableColumn<producto, Float> idPrecio;
    @FXML
    private TextField textcod;
    @FXML
    private TextField textdesc;
    @FXML
    private TextField textcantidad;
    @FXML
    private TextField textPrecio;
    @FXML
    private ComboBox<String> cmbIva;
    @FXML
    private ComboBox<Marca> cmbMarca;
    @FXML
    private TableColumn<producto, Integer> idMarca;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.idCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.idDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.idCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        this.idIva.setCellValueFactory(new PropertyValueFactory<>("iva"));
        this.idPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        this.idMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        this.cmbMarca.setCellFactory((ListView<Marca> l) -> {
            return new ListCell<Marca>() {
                @Override
                protected void updateItem(Marca m, boolean empty) {
                    if (!empty) {
                        this.setText("(" + m.getCodigo() + ")" + m.getDescripcion());
                    } else {
                        this.setText("");
                    }
                    super.updateItem(m, empty);
                }
            };
        });

        //Establecer la forma en que el combo va a mostrar la marca seleccioinada
        this.cmbMarca.setButtonCell(new ListCell<Marca>() {
            @Override
            protected void updateItem(Marca m, boolean empty) {
                if (!empty) {
                    this.setText("(" + m.getCodigo() + ") " + m.getDescripcion());
                } else {
                    this.setText("");
                }
                super.updateItem(m, empty);
            }
        }
        );

        //Cargar los posibles valores en el combo de IVA
        this.cmbIva.getItems().add("10%");
        this.cmbIva.getItems().add("5%");
        this.cmbIva.getItems().add("Excento");
        this.cmbIva.getSelectionModel().selectFirst();

        //Se crea la conexion a la base de datos con la clase creada para el efecto
        this.conex = new ConexionBD();
        //Invocamos al metodo que trae los registros de la tabla marca para cargar en el combo
        this.cargarMarcas();
        this.cargarDatos();
        
        this.idTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends producto> obs,producto valorAnterior, producto valorNuevo)->{
            if(valorNuevo!=null){
                this.textcod.setText(valorNuevo.getCodigo().toString());
                this.textdesc.setText(valorNuevo.getDescripcion());
                this.textcantidad.setText(valorNuevo.getCantidad().toString());
                this.textPrecio.setText(valorNuevo.getPrecio().toString());
                for(Marca mc : this.cmbMarca.getItems()){
                    if(mc.getCodigo().equals(this.idTable.getSelectionModel().getSelectedItem().getMarca())){
                        this.cmbMarca.getSelectionModel().select(mc);
                        break;
                    }
                    }
                }
            });
    }

    //Metodo que consulta a la base de datos y carga las marcas en el combo
    private void cargarMarcas() {
        try {
            String sql = "SELECT * FROM marca";
            Statement stm = this.conex.getConexion().createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                this.cmbMarca.getItems().add(new Marca(resultado.getInt("idmarca"), resultado.getString("descripcion")));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al cargar Marcas", ex);
        }
    }

    @FXML
    private void registrar(ActionEvent event) {
        String id = this.textcod.getText();
        String descripcion = this.textdesc.getText();
        String cantidad = this.textcantidad.getText();
        String iva = this.cmbIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
                break;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.textPrecio.getText();
        Integer marca = this.cmbMarca.getSelectionModel().getSelectedItem().getCodigo();

        if (id.isEmpty() || descripcion.isEmpty() || cantidad.isEmpty() || iva.isEmpty() || precio.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error!");
            a.setHeaderText("Complete todos los campos.");
            a.show();
        } else {
            try {
                String sql = "INSERT INTO producto(descripcion,cantidad,iva,precio,idmarca) VALUES(?,?,?,?,?)";
                PreparedStatement stm = conex.getConexion().prepareStatement(sql);
                stm.setString(1, descripcion);
                stm.setString(2, cantidad);
                stm.setInt(3, iv);
                stm.setString(4, precio);
                stm.setInt(5, marca);
                stm.execute();
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setTitle("Éxito");
                al.setHeaderText("Producto guardado correctamente");
                al.show();
                this.textdesc.clear();
                this.textcod.clear();
                this.textcantidad.clear();
                this.textPrecio.clear();
                this.cargarMarcas();
                this.cargarDatos();

            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error de conexión");
                al.setHeaderText("No se puede insertar a la base de datos");
                al.setContentText(ex.toString());
                al.showAndWait();

            }
        }
    }

    private void cargarDatos() {
        this.idTable.getItems().clear();
        try {
            String sql = "SELECT * FROM producto";
            Statement stm = this.conex.getConexion().createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                Integer cod = resultado.getInt("idproducto");
                String desc = resultado.getString("descripcion");
                Integer cant = resultado.getInt("cantidad");
                String iva = resultado.getString("iva");
                Float prec = resultado.getFloat("precio");
                Integer marc = resultado.getInt("idmarca");
                producto p = new producto(desc, cod, cant, iva, prec, marc);
                this.idTable.getItems().add(p);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        String id = this.textcod.getText();
        String descripcion = this.textdesc.getText();
        String cantidad = this.textcantidad.getText();
        String iva = this.cmbIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
                break;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.textPrecio.getText();
        Integer marca = this.cmbMarca.getSelectionModel().getSelectedItem().getCodigo();

        String sql = "UPDATE producto SET descripcion = ?, cantidad=?, iva=?, precio=?, idmarca=? WHERE idproducto = ?";
        try {
            PreparedStatement stm = conex.getConexion().prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.setString(2, cantidad);
            stm.setInt(3, iv);
            stm.setString(4, precio);
            stm.setInt(5, marca);
            stm.setInt(6, Integer.parseInt(id));
            stm.execute();
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Éxito");
            al.setHeaderText("Producto editado correctamente");
            al.show();
            this.textdesc.clear();
            this.textcod.clear();
            this.textcantidad.clear();
            this.textPrecio.clear();
            this.cargarDatos();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al editar", ex);
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error de conexión");
            al.setHeaderText("No se puede editar registro en la base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();

        }
        this.cargarDatos();
        this.cargarMarcas();
    }

    @FXML
    private void eliminar(ActionEvent event) {
        String id = this.textcod.getText();
        String descripcion = this.textdesc.getText();
        String cantidad = this.textcantidad.getText();
        String iva = this.cmbIva.getSelectionModel().getSelectedItem();
        String precio = this.textPrecio.getText();

        if (id.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error al eliminar");
            a.setHeaderText("Ingrese un codigo");
            a.show();
        } else {
            Alert alConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alConfirm.setTitle("Confirmar");
            alConfirm.setHeaderText("Desea eliminar el producto?");
            alConfirm.setContentText(id + " " + descripcion + " " + cantidad + " " + iva + " " + precio);
            Optional<ButtonType> accion = alConfirm.showAndWait();

            if (accion.get().equals(ButtonType.OK)) {
                try {
                    String sql = "DELETE FROM producto WHERE  idproducto = ?";
                    PreparedStatement stm = conex.getConexion().prepareStatement(sql);
                    Integer cod = Integer.parseInt(id);
                    stm.setInt(1, cod);
                    stm.execute();
                    int cant = stm.getUpdateCount();
                    if (cant == 0) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Error al eliminar");
                        a.setHeaderText("No existe el producto con codigo" + id);
                        a.show();
                    } else {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Eliminado");
                        a.setHeaderText("Producto aliminado correctamente");
                        a.show();
                        this.textdesc.clear();
                        this.textcod.clear();
                        this.textcantidad.clear();
                        this.textPrecio.clear();

                        this.cargarDatos();
                    }
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "Error al eliminar", ex);
                }
            }
        }
    }

}
