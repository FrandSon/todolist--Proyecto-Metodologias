package madstodolist.dto;

// Clase de datos para el formulario de login
public class LoginData {
    private String eMail;
    private String password;
    private boolean bloqueado;

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
