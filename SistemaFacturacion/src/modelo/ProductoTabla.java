/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author diego
 */
public class ProductoTabla {

    public SimpleStringProperty nombre = new SimpleStringProperty();
    public SimpleStringProperty presentacion = new SimpleStringProperty();
    public SimpleIntegerProperty cantidad = new SimpleIntegerProperty();
    public SimpleIntegerProperty precio = new SimpleIntegerProperty();

    public String getNombre() {
        return nombre.get();
    }

    public String getPresentacion() {
        return presentacion.get();
    }

    public Integer getCantidad() {
        return cantidad.get();
    }

    public Integer getPrecio() {
        return precio.get();
    }

    private Connection getConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/facturacionbd", "root", "socorro");
    }

    public void insertarDatos() {
        try {
            try (Connection con = getConexion()) {
                String queryConsulta = "select * from productos where nombre = ? and presentacion = ?";
                PreparedStatement consulta = con.prepareStatement(queryConsulta);
                consulta.setString(1, this.getNombre());
                consulta.setString(2, this.getPresentacion());
                ResultSet resultado = consulta.executeQuery();

                if (resultado.first()) {
                    System.out.println("Ya hay un dato registrado así");
                } else {
                    String query = "INSERT INTO productos(nombre, presentacion, cantidad, precio) VALUES(?,?,?,?)";
                    PreparedStatement estado = con.prepareStatement(query);

                    estado.setString(1, this.getNombre());
                    estado.setString(2, this.getPresentacion());
                    estado.setInt(3, this.getCantidad());
                    estado.setInt(4, this.getPrecio());
                    estado.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public void actualizarDatos(String nombreAnterior, String presentacionAnterior) {
        try {

            Connection con = getConexion();
            String query = "update productos set nombre = ?, presentacion = ?, cantidad = ?, precio = ? where nombre = ? and presentacion = ?";
            PreparedStatement estado = con.prepareStatement(query);

            estado.setString(1, this.getNombre());
            estado.setString(2, this.getPresentacion());
            estado.setInt(3, this.getCantidad());
            estado.setInt(4, this.getPrecio());
            estado.setString(5, nombreAnterior);
            estado.setString(6, presentacionAnterior);
            estado.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public void eliminarDatos(String nombre, String presentacion) {
        try {            
            Connection con = getConexion();
            String query = "delete from productos where nombre = ? and presentacion = ?";
            PreparedStatement estado = con.prepareStatement(query);

            estado.setString(1, nombre);
            estado.setString(2, presentacion);
            estado.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public ArrayList<ProductoTabla> adquirirProductos() {
        try {
            try (Connection con = getConexion()) {
                Statement st = con.createStatement();
                ResultSet datosDeTabla = st.executeQuery("select nombre, presentacion, cantidad, precio from productos");
                ArrayList<ProductoTabla> listaProductos = new ArrayList<>();
                while (datosDeTabla.next()) {

                    ProductoTabla producto = new ProductoTabla();
                    producto.nombre.set(datosDeTabla.getString("nombre"));
                    producto.presentacion.set(datosDeTabla.getString("presentacion"));
                    producto.cantidad.set(datosDeTabla.getInt("cantidad"));
                    producto.precio.set(datosDeTabla.getInt("precio"));
                    listaProductos.add(producto);
                }
                return listaProductos;

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
