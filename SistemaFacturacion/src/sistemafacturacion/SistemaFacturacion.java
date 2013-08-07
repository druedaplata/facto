/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemafacturacion;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author diego
 */
public class SistemaFacturacion extends Application {
    
    public static String vista1_ID = "facturas";
    public static String vista1_archivo = "VistaFacturas.fxml";
    public static String vista2_ID = "clientes";
    public static String vista2_archivo = "VistaClientes.fxml";
    public static String vista3_ID = "productos";
    public static String vista3_archivo = "VistaProductos.fxml";
    
    
    
    @Override
    public void start(Stage primaryStage) {
        
        ControladorDeVistas principalControlador = new ControladorDeVistas();
        //principalControlador.cargarVista(vista1_ID, vista1_archivo); //Vista Principal de Facturas
        principalControlador.cargarVista(SistemaFacturacion.vista3_ID, SistemaFacturacion.vista3_archivo); //Vista de Clientes
        principalControlador.cargarVista(SistemaFacturacion.vista2_ID, SistemaFacturacion.vista2_archivo); //Vista de Productos
        
        principalControlador.setVista(SistemaFacturacion.vista3_ID);
        
        Group root = new Group();
        root.getChildren().addAll(principalControlador);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
