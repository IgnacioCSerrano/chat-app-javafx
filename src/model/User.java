/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Usuario
 */
public class User {
    
    private String nombre;
    private String passw;
    private byte[] imagen;
    
    public User() {
        this.nombre = "";
        this.passw = "";
        this.imagen = null;
    }

    public User(String nombre, String passw, byte[] imagen) {
        this.nombre = nombre;
        this.passw = passw;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    
}
