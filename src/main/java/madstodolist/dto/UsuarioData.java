package madstodolist.dto;

import java.util.Date;
import java.util.Objects;


// Data Transfer Object para la clase Usuario
public class UsuarioData {

    private Long id;
    private String email;
    private String nombre;
    private String password;
    private Date fechaNacimiento;
    private boolean administrador;
    private boolean bloqueado = false;
    private Date fechaCreacion;


    public void setBloqueado(boolean estado) {
        this.bloqueado = estado;
    }
    public boolean isBloqueado() {
        return bloqueado;
    }

    public boolean isAdministrador() { return administrador; }
    public void setAdministrador(boolean administrador) {
        this.administrador = administrador; }

    public UsuarioData(Long id, String email) {
        this.id = id;
        this.email = email;
    }
    public UsuarioData() {
    }


    // Getters y setters
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Sobreescribimos equals y hashCode para que dos usuarios sean iguales
    // si tienen el mismo ID (ignoramos el resto de atributos)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioData)) return false;
        UsuarioData that = (UsuarioData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
