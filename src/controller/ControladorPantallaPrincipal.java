package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import model.Conexion;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ControladorPantallaPrincipal implements Initializable {
    private Conexion conexion;
    
    @FXML
    private AnchorPane apContenedor;
    @FXML
    private AnchorPane apContenido;
    @FXML
    private AnchorPane apCabacera;
    @FXML
    private Button btnAccess;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        btnAccess.setDisable(conexion.getConexion() == null);
    }

    @FXML
    private void acceder(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.load(getClass().getResource("/view/Pantalla1.fxml").openStream());
            Pantalla1Controller controller = cargador.getController();
            controller.setConexion(conexion);
            // Debemos guardar contenido en AnchorPane porque elemento raíz de la vista es un AnchorPane
            AnchorPane root = cargador.getRoot();
            apContenido.getChildren().clear();
            apContenido.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(ControladorPantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
