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
import modelo.ProductoTabla;

/**
 * FXML Controller class
 *
 * @author diego
 */
public class VistaProductosController implements Initializable, VistaControlada {
//Declaración de los botones

    @FXML
    private Button aniadirBT;
    @FXML
    private Button modificarBT;
    @FXML
    private Button eliminarBT;
    @FXML
    private Button nuevoBT;
    //Declaramos los campos de texto
    @FXML
    private TextField nombreTF;
    @FXML
    private TextField presentacionTF;
    @FXML
    private TextField cantidadTF;
    @FXML
    private TextField precioTF;
    //Declarar el boton y texto para buscar
    @FXML
    private TextField buscarTF;
    @FXML
    private Button buscarBT;
    //Declaramos las tablas y las columnas
    @FXML
    private TableView<ProductoTabla> productosTB;
    @FXML
    private TableColumn nombreCL;
    @FXML
    private TableColumn presentacionCL;
    @FXML
    private TableColumn cantidadCL;
    @FXML
    private TableColumn precioCL;
    ObservableList<ProductoTabla> productos;
    
    private int posicionProductoEnTabla;
    ControladorDeVistas miControlador;

    public VistaProductosController() {
    }

    @FXML
    private void nuevo(ActionEvent event) {
        nombreTF.setText("");
        presentacionTF.setText("");
        cantidadTF.setText("");
        precioTF.setText("");

        modificarBT.setDisable(true);
        eliminarBT.setDisable(true);
        aniadirBT.setDisable(false);
    }

    @FXML
    private void aniadir(ActionEvent event) {
        ProductoTabla producto = new ProductoTabla();
        producto.nombre.set(nombreTF.getText());
        producto.presentacion.set(presentacionTF.getText());
        producto.cantidad.set(Integer.parseInt(cantidadTF.getText()));
        producto.precio.set(Integer.parseInt(precioTF.getText()));
        producto.insertarDatos();
    }

    @FXML
    private void modificar(ActionEvent event) {
        ProductoTabla producto = new ProductoTabla();
        producto.nombre.set(nombreTF.getText());
        producto.presentacion.set(presentacionTF.getText());
        producto.cantidad.set(Integer.parseInt(cantidadTF.getText()));
        producto.precio.set(Integer.parseInt(precioTF.getText()));
        String nombreAnterior = productosTB.getSelectionModel().getSelectedItem().getNombre();
        String presentacionAnterior = productosTB.getSelectionModel().getSelectedItem().getPresentacion();

        producto.actualizarDatos(nombreAnterior, presentacionAnterior);

        productos.remove(posicionProductoEnTabla);
        productos.add(posicionProductoEnTabla, producto);


    }

    @FXML
    private void eliminar(ActionEvent event) {

        ProductoTabla producto = new ProductoTabla();
        String nombre = productosTB.getSelectionModel().getSelectedItem().getNombre();
        String presentacion = productosTB.getSelectionModel().getSelectedItem().getPresentacion();
        producto.eliminarDatos(nombre, presentacion);
        productos.remove(posicionProductoEnTabla);

    }

    @FXML
    private void buscar(ActionEvent event) {
        String datoBusqueda = buscarTF.getText();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getNombre().equals(datoBusqueda) || productos.get(i).getPresentacion().equals(datoBusqueda)) {
                productosTB.getSelectionModel().select(i);
                break;
            }
        }
    }
    //Para seleccionar una celda de la tabla
    public final ListChangeListener<ProductoTabla> selectorTablaProductos = new ListChangeListener<ProductoTabla>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends ProductoTabla> c) {
            ponerProductoSeleccionado();
        }
    };

    public ProductoTabla getProductoTablaSeleccionado() {
        if (productosTB != null) {
            List<ProductoTabla> tabla = productosTB.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                ProductoTabla competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void ponerProductoSeleccionado() {
        ProductoTabla producto = getProductoTablaSeleccionado();
        posicionProductoEnTabla = productos.indexOf(producto);

        if (producto != null) {
            //Pongo los datos en los campos de texto
            nombreTF.setText(producto.getNombre());
            presentacionTF.setText(producto.getPresentacion());
            cantidadTF.setText(producto.getCantidad().toString());
            precioTF.setText(producto.getPrecio().toString());

            //Pongo los botones en su estado correspondiente
            modificarBT.setDisable(false);
            eliminarBT.setDisable(false);
            aniadirBT.setDisable(true);

        }

    }

    private void inicializarTablaProductos() {
        nombreCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, String>("nombre"));
        presentacionCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, String>("presentacion"));
        cantidadCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, Integer>("cantidad"));
        precioCL.setCellValueFactory(new PropertyValueFactory<ProductoTabla, Integer>("precio"));

        productos = FXCollections.observableArrayList();
        productosTB.setItems(productos);

        ProductoTabla productoLlamador = new ProductoTabla();
        ArrayList<ProductoTabla> listaProductos = productoLlamador.adquirirProductos();

        for (ProductoTabla producto : listaProductos) {
            productos.add(producto);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.inicializarTablaProductos();

        modificarBT.setDisable(true);
        eliminarBT.setDisable(true);

        //Seleccionar las tuplas de la tabla de productos
        final ObservableList<ProductoTabla> tablaProductosSel = productosTB.getSelectionModel().getSelectedItems();
        tablaProductosSel.addListener(selectorTablaProductos);
    }

    @Override
    public void setVistaPadre(ControladorDeVistas screenParent) {
        miControlador = screenParent;
    }

    public void gotoClientes(ActionEvent event) {
        miControlador.setVista(SistemaFacturacion.vista3_ID);
    }
}
