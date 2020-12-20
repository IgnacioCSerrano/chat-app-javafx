package model;

/**
 *
 * @author Usuario
 */
public class Chat {
    
    private int id;
    private String usuario;
    private String mensaje;
    private String fecha;
    
    public Chat() {
        this.id = 0;
        this.usuario = "";
        this.mensaje = "";
        this.fecha = "";
    }

    public Chat(int id, String usuario, String mensaje, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFecha() {
        return fecha;
    }

}
