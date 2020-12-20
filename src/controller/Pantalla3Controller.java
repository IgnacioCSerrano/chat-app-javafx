package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.User;
import model.Conexion;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Pantalla3Controller implements Initializable {
    private Conexion conexion;
    private User user;

    @FXML
    private AnchorPane apContenido;
    @FXML
    private PasswordField txtPassw;
    @FXML
    private PasswordField txtConfirmPassw;
    @FXML
    private Label labPassw;
    @FXML
    private Label labPasswConf;
    @FXML
    private Button btnChangePassw;
    @FXML
    public ImageView ivImagen;
    @FXML
    private Button btnVolver;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
    
    public void setDatos(User user) {
        this.user = user;
        ivImagen.setImage(new Image(new ByteArrayInputStream(user.getImagen())));
    }
    
    public Alert getAlertSuccess(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setContentText(content);
        return alert;
    }
    
    public Alert getAlertError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(content);
        return alert;
    }

    @FXML
    private void changePassw(ActionEvent event) {
        if ( txtPassw.getText().isEmpty() || txtConfirmPassw.getText().isEmpty()) {
            Alert alert = getAlertError("Debe rellenar ambos campos.");
            alert.showAndWait();
            txtPassw.requestFocus();
        } else {
            if ( !txtPassw.getText().equals( txtConfirmPassw.getText() ) ) {
                Alert alert = getAlertError("Las contraseñas no coinciden.");
                alert.showAndWait();
                txtPassw.requestFocus();
            } else {
                user.setPassw(txtPassw.getText());
                if ( conexion.cambiarClave(user) ) {
                    Alert alerta = getAlertSuccess("Contraseña modificada correctamente.");
                    alerta.showAndWait();
                    txtPassw.clear();
                    txtConfirmPassw.clear();
                    txtPassw.requestFocus();
                } else {
                    Alert alerta = getAlertSuccess("Error al modificar contraseña.");
                    alerta.showAndWait();
                }
            }
        }
    }

    @FXML
    private void cambiarImagen(MouseEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        
        if (selectedFile != null) {
            String filename = selectedFile.getAbsolutePath();
            System.out.println(filename);
            try {
                FileInputStream fis = new FileInputStream(filename);
                byte[] data = new byte[(int)selectedFile.length()];
                
//                int bytesRead;
//                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//                while ( (bytesRead = fis.read(data)) != -1) {
//                   buffer.write(data, 0, bytesRead);
//                }
//                user.setImagen(buffer.toByteArray());
                
                fis.read(data);
                user.setImagen(data);
                
                if (conexion.cambiarImagen(user)) {
                    ivImagen.setImage(new Image(new ByteArrayInputStream(user.getImagen())));
                } else {
                    Alert alerta = getAlertError("Error al modificar imagen.");
                    alerta.showAndWait();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.load(getClass().getResource("/view/Pantalla2.fxml").openStream());
            AnchorPane root = cargador.getRoot();
            apContenido.getChildren().clear();
            apContenido.getChildren().add(root);

            Pantalla2Controller controller = cargador.getController();
            controller.setConexion(conexion);
            controller.setDatos(user);
        } catch (IOException ex) {
            Logger.getLogger(Pantalla1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
