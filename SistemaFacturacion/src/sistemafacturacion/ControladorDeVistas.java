/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemafacturacion;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author diego
 */
public class ControladorDeVistas extends StackPane {

    //Contiene las vistas que seran mostradas
    private HashMap<String, Node> vistas = new HashMap<>();

    public ControladorDeVistas() {
        super();
    }

    //Agrega la vista a la colección
    public void agregarVista(String nombre, Node vista) {
        vistas.put(nombre, vista);
    }

    //Retorne el Node con el nombre apropiado
    public Node getVista(String nombre) {
        return vistas.get(nombre);
    }

    //Carga el archivo FXML, añade la vista a la colección de vista y
    //finalmente inyecta la vista a el controlador
    public boolean cargarVista(String nombre, String recurso) {
        try {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource(recurso));            
            Parent cargaVista = (Parent) miCargador.load();
            VistaControlada miControladorDeVista = ((VistaControlada) miCargador.getController());
            miControladorDeVista.setVistaPadre(this);
            agregarVista(nombre, cargaVista);
            return true;
        } catch (Exception e) {
                e.getMessage();
            return false;
        }
    }

    /*Este metodo intenta mostrar la vista con un nombre predefinido.
     * Primero se asegura que la vista haya sido cargada. Entonces si hay mas
     * de una vista, la nueva vista es añadida despues, y la vista actual es removida.
     * Si no hay ninguna vista siendo mostrada, la nueva vista es añadida a el root.
     */
    public boolean setVista(final String name) {
        if (vistas.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, vistas.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(vistas.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded in here!!! \n");
            return false;
        }
    }

    //Este metodo removerá la vista con el nombre de la coleccion de vistas
    public boolean retirarVista(String nombre) {
        if (vistas.remove(nombre) == null) {
            System.out.println("La vista no existe");
            return false;
        } else {
            return true;
        }
    }
}
