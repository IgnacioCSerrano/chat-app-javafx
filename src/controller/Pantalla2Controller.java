package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;
import model.Chat;
import model.Conexion;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Pantalla2Controller implements Initializable {
    private Conexion conexion;
    private User user;
    private ArrayList<Chat> listaMensajes;
    private ArrayList<User> listaUsuarios;
    private Timer timer;
    
    @FXML
    private AnchorPane apContenido;
    @FXML
    private Label lbUsuario;
    @FXML
    private ScrollPane sp;
    @FXML
    private GridPane gp;
    @FXML
    private TextField txtMensaje;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnEditar;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaMensajes = new ArrayList<>();
        listaUsuarios = new ArrayList<>();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timer = new Timer();
    }
    
    private void refrescarTabla() {
        if (conexion.hayCambios()) {
            conexion.getChat(listaMensajes);
            cargarTabla();
        }
    }
    
    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
    
    private void cargarTabla() {
        // Resetear GridPane para no sobreescribir mensajes
        gp.getChildren().clear();
        // Construir tupla de etiquetas para cada columna
        for (int i = 0; i < listaMensajes.size(); i++) {
            // Imagen
            
//            for (int j = 0; j < listaUsuarios.size(); j++) {
//                if (listaUsuarios.get(j).getNombre().equals(listaMensajes.get(i).getUsuario())) {
//                    ImageView img = new ImageView();
//                    img.setFitHeight(40);
//                    img.setFitWidth(40);
//                    img.setPreserveRatio(false);
//                    InputStream is = new ByteArrayInputStream(listaUsuarios.get(j).getImagen());
//                    img.setImage(new Image(is));
//                    gp.add(img, 0, i);
//                }
//            }
            
            String username = listaMensajes.get(i).getUsuario();
            User usuario = listaUsuarios.stream().filter(u -> username.equals(u.getNombre())).findFirst().orElse(null);
            if (usuario != null) {
                ImageView img = new ImageView();
                img.setFitHeight(40);
                img.setFitWidth(40);
                img.setPreserveRatio(false);
                InputStream is = new ByteArrayInputStream(usuario.getImagen());
                img.setImage(new Image(is));
                gp.add(img, 0, i);
            } 
            // Nombre
            Label lbNombre = new Label(listaMensajes.get(i).getUsuario());
            gp.add(lbNombre, 1, i);
            // Mensaje
            Label lbMensaje = new Label(listaMensajes.get(i).getMensaje());
            gp.add(lbMensaje, 2, i);
            // Fecha
            Label lbFecha = new Label(listaMensajes.get(i).getFecha());
            gp.add(lbFecha, 3, i);
        }
    }
    
    public void setTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        refrescar();
//                    }
//                });
                Platform.runLater(() -> {
                    refrescarTabla();
                });
            }
        }, 0, 1000);
    }
    
    public void setDatos(User user) {
        this.user = user;
        lbUsuario.setText(user.getNombre());
        
        conexion.getUsuarios(listaUsuarios);
        conexion.getChat(listaMensajes);
        cargarTabla();
        
        setTimer();
        Stage stage = (Stage) apContenido.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            timer.cancel();
        });
    }
    
    @FXML
    private void enviarMensaje(ActionEvent event) {
        if ( conexion.insertar(txtMensaje.getText(), user.getNombre()) ) {
            
//            conexion.getChat(listaMensajes);
//            cargarTabla();

            txtMensaje.clear();
            txtMensaje.requestFocus();
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("Error al enviar mensaje.");
            alerta.showAndWait();
        }
    }
    
    @FXML
    private void refresh(ActionEvent event) {
        refrescarTabla();
    }
    
    @FXML
    private void editarPerfil(ActionEvent event) {
        timer.cancel();
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.setLocation(getClass().getResource("/view/Pantalla3.fxml"));
            cargador.load();
            
            AnchorPane root = cargador.getRoot();
            apContenido.getChildren().clear();
            apContenido.getChildren().add(root);
            
            Pantalla3Controller controller = cargador.getController();
            controller.setConexion(conexion);
            controller.setDatos(user);
        } catch (IOException ex) {
            Logger.getLogger(Pantalla1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        timer.cancel();
        try {
            FXMLLoader cargador = new FXMLLoader();
            cargador.load(getClass().getResource("/view/Pantalla1.fxml").openStream());
            
            AnchorPane root = cargador.getRoot();
            apContenido.getChildren().clear();
            apContenido.getChildren().add(root);
            
            Pantalla1Controller controller = cargador.getController();
            controller.setConexion(conexion);
        } catch (IOException ex) {
            Logger.getLogger(ControladorPantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
