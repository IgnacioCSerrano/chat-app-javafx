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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Conexion;
import model.User;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Pantalla1Controller implements Initializable {
    private Conexion conexion;
    
    @FXML
    private AnchorPane apContenido;
    @FXML
    private PasswordField txtPwd;
    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnRegistrar;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
    
    private void cargarVentana() {
        User user = conexion.getUsuario(txtUsuario.getText(), txtPwd.getText());
        if (user != null) {
            try {
                FXMLLoader cargador = new FXMLLoader();
                
//                cargador.load(getClass().getResource("/view/Pantalla2.fxml").openStream());
                
                cargador.setLocation(getClass().getResource("/view/Pantalla2.fxml"));
//                Scene escena = new Scene(cargador.load());
                cargador.load();
                
                AnchorPane root = cargador.getRoot();
                apContenido.getChildren().clear();
                apContenido.getChildren().add(root);
                
                Pantalla2Controller controller = (Pantalla2Controller) cargador.getController();
                controller.setConexion(conexion);
                controller.setDatos(user);
            } catch (IOException ex) {
                Logger.getLogger(Pantalla1Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Usuario o contraseña incorrectos.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void inicio(ActionEvent event) {
        cargarVentana();
    }

    @FXML
    private void registro(ActionEvent event) {
        if (conexion.registrarUsuario(txtUsuario.getText(), txtPwd.getText())) {
            cargarVentana();
        }
    }

}
