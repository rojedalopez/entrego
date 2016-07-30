/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author SISTEMAS
 */
public class Usuario {
    private String correo;
    private String nombre;
    private String apellido;
    private String codigo;
    private String mensaje;
    private String url_imagen;
    private String fecha;
    private String token;
    private int rol;
    private String telefono;
    private int proyecto;
    private String name_proyecto;
    
    
    public Usuario() {
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    
    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String hv_archivo) {
        this.url_imagen = hv_archivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
    }

    public String getName_proyecto() {
        return name_proyecto;
    }

    public void setName_proyecto(String name_proyecto) {
        this.name_proyecto = name_proyecto;
    }
    
    
    
}
