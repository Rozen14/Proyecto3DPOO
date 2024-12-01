package usuario;

public abstract class Usuario {

    protected String nombre;
    protected String contrasenia;
    protected String tipo;
    protected String correo;

    public Usuario(String nombre, String contrasenia, String tipo, String correo){

        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.tipo = tipo;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setId(String tipo) {
        this.tipo = tipo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    } 

    public boolean validarCredenciales(String correo, String contrasenia) {
        return this.correo.equals(correo) && this.contrasenia.equals(contrasenia);
    }
    
    public boolean actualizarContrasenia(String contraseniaActual, String nuevaContrasenia) {
        if (this.contrasenia.equals(contraseniaActual)) {
            this.contrasenia = nuevaContrasenia;
            return true;
        }
        return false;
    }
        
    

}
