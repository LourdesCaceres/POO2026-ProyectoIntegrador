import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class Biblioteca {

    private String nombre;
    private ArrayList<Socio> socios;
    private ArrayList<Libro> libros;
    
    public Biblioteca(String p_nombre){
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<>());
        this.setLibros(new ArrayList<>());
    }
    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios){
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(new ArrayList<>());
    }
    public Biblioteca(String p_nombre, ArrayList<Libro> p_libros){
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<>());
        this.setLibros(p_libros);
    }
    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios, ArrayList<Libro> p_libros){
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(p_libros);
    }
    
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    private void setSocios(ArrayList<Socio> p_socios) {
        this.socios = p_socios;
    }

    private void setLibros(ArrayList<Libro> p_libros) {
        this.libros = p_libros;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<Socio> getSocios() {
        return this.socios;
    }

    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        this.addLibro(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        if (p_socio != null) {
            if (p_socio.puedePedir() && !p_libro.prestado()) {
                p_socio.addPrestamo(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                p_libro.addPrestamo(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int cantidadSociosPorTipo(String p_objeto) {
        int cont = 0;

        for(Socio socios : this.getSocios()) {
            if (socios.soyDeLaClase().equalsIgnoreCase(p_objeto)) {
                ++cont;
            }
        }

        return cont;
    }

    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (p_libro.prestado()) {
            return p_libro.getPrestamo().getSocio().getNombre();
        } else {
            throw new LibroNoPrestadoException("El libro '" + p_libro.getTitulo() +  "' se encuentra en la biblioteca\n");
        }
    }

    public Socio buscarSocio(int p_dni) {
        Socio socio = null;

        for(Socio socios : this.getSocios()) {
            if (socios.getDniSocio() == p_dni) {
                socio = socios;
            }
        }

        return socio;
    }
}

    
