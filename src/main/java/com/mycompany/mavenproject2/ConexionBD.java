/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author alexis
 */
class ConexionBD {

    private static final Logger LOG = Logger.getLogger(ConexionBD.class.getName());

    private Connection con = null;

    public ConexionBD() {

        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyecto_investigacion", "admin", "Mysql.123456");
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede conectar a la base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();
            System.exit(1);
        }
    }

    public Connection getConexion() {
        return this.con;
    }

}
