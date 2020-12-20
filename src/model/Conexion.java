package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

/**
 *
 * @author Usuario
 */
public class Conexion {

    private Connection conexion;

    private static long count = 0;

    public Conexion() {
        this.conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conexion = DriverManager.getConnection("jdbc:mysql://freedb.tech/freedbtech_antonio2020", "freedbtech_javier2020", "javier2020");
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Éxito");
            alerta.setContentText("Conectado a la Base de Datos.");
            alerta.showAndWait();
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error");
            alerta.setContentText("No es posible conectarse a la Base de Datos:\n\n" + ex.getMessage());
            alerta.showAndWait();
        }
    }

    public Connection getConexion() {
        return conexion;
    }
    
    private String encriptarClave(String passw) {
        StringBuilder sb = new StringBuilder();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5"); // igual para SHA1 y SHA2
            byte[] bytes = md.digest(passw.getBytes());
            for (byte b : bytes) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (java.security.NoSuchAlgorithmException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
    
    public User getUsuario(String usuario, String clave) {
        String query = "SELECT * FROM user WHERE usuario = ? AND clave = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, encriptarClave(clave));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setNombre( rs.getString("usuario") );
                user.setImagen(rs.getBytes("imagen") );
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    private FileInputStream chooseImage() throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            String filename = selectedFile.getAbsolutePath();
            System.out.println(filename);
            return new FileInputStream(filename);
        }
        return null;
    }
    
    public boolean registrarUsuario(String user, String pwd) {
        String consulta = "INSERT INTO user VALUES (?,?,?)";
        try {
//            FileInputStream fis = chooseImage();
            FileInputStream fis = new FileInputStream("src\\images\\avatar.png");
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setString(1, user);
            ps.setString(2, encriptarClave(pwd));
            ps.setBinaryStream(3, fis);
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (FileNotFoundException | SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void getChat(ArrayList<Chat> lista) {
        lista.clear();
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String query = "SELECT * FROM chat ORDER BY id DESC";
        try {
            Statement s = conexion.createStatement();
            ResultSet rs = s.executeQuery(query);
            while (rs.next()) {
                Chat tupla = new Chat();
                tupla.setUsuario(rs.getString("usuario"));
                tupla.setMensaje(rs.getString("mensaje"));
                tupla.setFecha((form.format(rs.getTimestamp("fecha"))));
                lista.add(tupla);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getUsuarios(ArrayList<User> lista) {
        lista.clear();
        String query = "SELECT * FROM user";
        try {
            Statement s = conexion.createStatement();
            ResultSet rs = s.executeQuery(query);
            while (rs.next()) {
                User user = new User();
                user.setNombre(rs.getString("usuario"));
                user.setImagen(rs.getBytes("imagen"));
                lista.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean hayCambios() {
        boolean change = false;
        try {
            String query = "SELECT COUNT(*) FROM chat";
            Statement s = conexion.createStatement();
            ResultSet rs = s.executeQuery(query);
            if (rs.next()) {
                long c = rs.getLong(1);
                if (count != 0 && c != count) {
                    change = true;
                }
                count = c;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return change;
    }

    public boolean insertar(String texto, String usuario) {
        try {
            String query = "INSERT INTO chat VALUES (null,?,?,?)";
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
            ps.setString(3, texto);
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean cambiarImagen(User user) {
        String consulta = "UPDATE user SET imagen = ? WHERE usuario = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setBytes(1, user.getImagen());
            ps.setString(2, user.getNombre());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean cambiarClave(User user) {
        String cadenaSql = "UPDATE user set clave = ? where usuario = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(cadenaSql);
            ps.setString(1, encriptarClave(user.getPassw()));
            ps.setString(2, user.getNombre());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
