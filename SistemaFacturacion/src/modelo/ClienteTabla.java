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
 * @author personal
 */
public class ClienteTabla {

    public SimpleIntegerProperty identificacion = new SimpleIntegerProperty();
    public SimpleStringProperty nombre = new SimpleStringProperty();
    public SimpleStringProperty direccion = new SimpleStringProperty();
    public SimpleIntegerProperty telefono = new SimpleIntegerProperty();
    public SimpleStringProperty correo = new SimpleStringProperty();

    public Integer getIdentificacion() {
        return identificacion.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getDireccion() {
        return direccion.get();
    }

    public Integer getTelefono() {
        return telefono.get();
    }

    public String getCorreo() {
        return correo.get();
    }

    private Connection getConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/facturacionbd", "root", "socorro");
    }

    public void insertarDatos() {
        try {
            try (Connection con = getConexion()) {
                String queryConsulta = "select * from clientes where identificacion = ?";
                PreparedStatement consulta = con.prepareStatement(queryConsulta);
                consulta.setInt(1, this.getIdentificacion());
                ResultSet resultado = consulta.executeQuery();

                if (resultado.first()) {
                    System.out.println("Ya hay un dato registrado así");
                } else {
                    String query = "INSERT INTO clientes(identificacion, nombre, direccion, telefono, correo) VALUES(?,?,?,?)";
                    PreparedStatement estado = con.prepareStatement(query);

                    estado.setInt(1, this.getIdentificacion());
                    estado.setString(2, this.getNombre());
                    estado.setString(3, this.getDireccion());
                    estado.setInt(4, this.getTelefono());
                    estado.setString(5, this.getCorreo());
                    estado.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public void actualizarDatos(int identificacionAnterior) {
        try {

            Connection con = getConexion();
            String query = "update clientes set identificacion = ?, nombre = ?, direccion = ?, telefono = ?, correo = ? where identificacion = ?";
            PreparedStatement estado = con.prepareStatement(query);

            estado.setInt(1, this.getIdentificacion());
            estado.setString(2, this.getNombre());
            estado.setString(3, this.getDireccion());
            estado.setInt(4, this.getTelefono());
            estado.setString(5, this.getCorreo());
            estado.setInt(6, identificacionAnterior);
            estado.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public void eliminarDatos(int identificacion) {
        try {
            Connection con = getConexion();
            String query = "delete from clientes where identificacion = ?";
            PreparedStatement estado = con.prepareStatement(query);

            estado.setInt(1, identificacion);
            estado.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }
    
    public ArrayList<ClienteTabla> adquirirClientes()
    {
        try {
            try (Connection con = getConexion()) {
                Statement st = con.createStatement();
                ResultSet datosDeTabla = st.executeQuery("select identificacion, nombre, direccion, telefono, correo from clientes");
                ArrayList<ClienteTabla> listaClientes = new ArrayList<>();
                while (datosDeTabla.next()) {

                    ClienteTabla cliente = new ClienteTabla();
                    cliente.identificacion.set(datosDeTabla.getInt("identificacion"));
                    cliente.nombre.set(datosDeTabla.getString("nombre"));
                    cliente.direccion.set(datosDeTabla.getString("direccion"));
                    cliente.telefono.set(datosDeTabla.getInt("telefono"));
                    cliente.correo.set(datosDeTabla.getString("correo"));
                    listaClientes.add(cliente);
                }
                return listaClientes;

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
}
