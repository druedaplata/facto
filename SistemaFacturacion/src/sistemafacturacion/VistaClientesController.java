/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemafacturacion;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.ClienteTabla;
import modelo.ProductoTabla;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class VistaClientesController implements Initializable, VistaControlada {

    //Declaración de botones
    @FXML
    private Button aniadirBT;
    @FXML
    private Button modificarBT;
    @FXML
    private Button eliminarBT;
    @FXML
    private Button nuevoBT;
    @FXML
    private Button buscarBT;
    //Declaración de campos de texto
    @FXML
    private TextField identificacionTF;
    @FXML
    private TextField nombreTF;
    @FXML
    private TextField direccionTF;
    @FXML
    private TextField telefonoTF;
    @FXML
    private TextField correoTF;
    @FXML
    private TextField buscarTF;
    //Declaración de tabla y columnas
    @FXML
    private TableView<ClienteTabla> clientesTB;
    @FXML
    private TableColumn identificacionCL;
    @FXML
    private TableColumn nombreCL;
    @FXML
    private TableColumn direccionCL;
    @FXML
    private TableColumn telefonoCL;
    @FXML
    private TableColumn correoCL;
    ObservableList<ClienteTabla> clientes;
    
    private int posicionClienteEnTabla;
    ControladorDeVistas miControlador;
    
    @FXML
    private void nuevo(ActionEvent event) {
        identificacionTF.setText("");
        nombreTF.setText("");
        direccionTF.setText("");
        telefonoTF.setText("");
        correoTF.setText("");

        modificarBT.setDisable(true);
        eliminarBT.setDisable(true);
        aniadirBT.setDisable(false);

    }
    @FXML
    private void aniadir(ActionEvent event) {
        ClienteTabla cliente = new ClienteTabla();
        cliente.identificacion.set(Integer.parseInt(identificacionTF.getText()));
        cliente.nombre.set(nombreTF.getText());
        cliente.direccion.set(direccionTF.getText());
        cliente.telefono.set(Integer.parseInt(telefonoTF.getText()));
        cliente.correo.set(correoTF.getText());
        cliente.insertarDatos();
        clientes.add(cliente);
    }
    
    @FXML
    private void modificar() {
        ClienteTabla cliente = new ClienteTabla();
        cliente.identificacion.set(Integer.parseInt(identificacionTF.getText()));
        cliente.nombre.set(nombreTF.getText());
        cliente.direccion.set(direccionTF.getText());
        cliente.telefono.set(Integer.parseInt(telefonoTF.getText()));
        cliente.correo.set(correoTF.getText());

        int identificacionAnterior = clientesTB.getSelectionModel().getSelectedItem().getIdentificacion();
        cliente.actualizarDatos(identificacionAnterior);

        clientes.remove(posicionClienteEnTabla);
        clientes.add(posicionClienteEnTabla, cliente);
    }
    @FXML
    private void eliminar() {
        ClienteTabla cliente = new ClienteTabla();
        int identificacion = clientesTB.getSelectionModel().getSelectedItem().getIdentificacion();
        cliente.eliminarDatos(identificacion);
        clientes.remove(posicionClienteEnTabla);
    }

    @FXML
    private void buscar(ActionEvent event) {
        int datoBusqueda = Integer.parseInt(buscarTF.getText());
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdentificacion().intValue() == datoBusqueda) {
                clientesTB.getSelectionModel().select(i);
                break;
            } 
        }
    }
    //Para seleccionar una celda de la tabla
    public final ListChangeListener<ClienteTabla> selectorTablaClientes = new ListChangeListener<ClienteTabla>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends ClienteTabla> c) {
            ponerClienteSeleccionado();
        }
    };

    public ClienteTabla getClienteTablaSeleccionado() {
        if (clientesTB != null) {
            List<ClienteTabla> tabla = clientesTB.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                ClienteTabla competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void ponerClienteSeleccionado() {
        ClienteTabla cliente = getClienteTablaSeleccionado();
        posicionClienteEnTabla = clientes.indexOf(cliente);

        if (cliente != null) {
            //Pongo los datos en los campos de texto
            cliente.identificacion.set(Integer.parseInt(identificacionTF.getText()));
            cliente.nombre.set(nombreTF.getText());
            cliente.direccion.set(direccionTF.getText());
            cliente.telefono.set(Integer.parseInt(telefonoTF.getText()));
            cliente.correo.set(correoTF.getText());

            //Pongo los botones en su estado correspondiente
            modificarBT.setDisable(false);
            eliminarBT.setDisable(false);
            aniadirBT.setDisable(true);

        }

    }

    private void inicializarTablaClientes() {
        identificacionCL.setCellValueFactory(new PropertyValueFactory<ClienteTabla, Integer>("identificacion"));
        nombreCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, String>("nombre"));
        direccionCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, String>("direccion"));
        telefonoCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, Integer>("telefono"));
        correoCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, String>("correo"));

        clientes = FXCollections.observableArrayList();
        clientesTB.setItems(clientes);
        
        ClienteTabla clienteLlamador = new ClienteTabla();
        ArrayList<ClienteTabla> listaClientes = clienteLlamador.adquirirClientes();

        for (ClienteTabla cliente : listaClientes) {
            clientes.add(cliente);
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        this.inicializarTablaClientes();

        modificarBT.setDisable(true);
        eliminarBT.setDisable(true);

        //Seleccionar las tuplas de la tabla de productos
        ObservableList<ClienteTabla> tablaProductosSel = clientesTB.getSelectionModel().getSelectedItems();
        tablaProductosSel.addListener(selectorTablaClientes);
    }

    @Override
    public void setVistaPadre(ControladorDeVistas screenParent) {
        miControlador = screenParent;
    }

    public void gotoProductos(ActionEvent event) {
        miControlador.setVista(SistemaFacturacion.vista2_ID);
    }
}
