import java.util.ArrayList;
import java.util.Calendar;

/**
 * Clase abstracta que representa a un socio generico de la biblioteca.
 * Aplica los conceptos de Abstraccion, Encapsulamiento y Jerarquia de Clases
 * (Temas 2, 4 y 6 del programa de la asignatura).
 * 
 * @author Subgrupo 1 (Prestamo y Socio)
 * @version 1.0 - TP7 Integrador
 */
public abstract class Socio {
    private int dniSocio;
    private String nombre;
    private int diasPrestamo;
    private ArrayList<Prestamo> prestamos;

    /**
     * Constructor de la clase abstracta Socio.
     * 
     * @param p_dniSocio DNI unico del socio
     * @param p_nombre Nombre y apellido del socio
     * @param p_diasPrestamo Cantidad de dias asignados para prestar libros
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo) {
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.prestamos = new ArrayList<Prestamo>();
    }

    // Setters y Getters
    public void setDniSocio(int p_dniSocio) {
        this.dniSocio = p_dniSocio;
    }

    public int getDniSocio() {
        return this.dniSocio;
    }

    public void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setDiasPrestamo(int p_diasPrestamo) {
        this.diasPrestamo = p_diasPrestamo;
    }

    public int getDiasPrestamo() {
        return this.diasPrestamo;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    /**
     * Agrega un nuevo prestamo a la ficha/lista de prestamos del socio.
     * @param p_prestamo Prestamo realizado
     */
    public void agregarPrestamo(Prestamo p_prestamo) {
        this.prestamos.add(p_prestamo);
    }

    /**
     * Devuelve la cantidad de libros que actualmente estan en poder del socio
     * (es decir, aquellos prestamos cuya fecha de devolucion aun es null).
     * 
     * @return int cantidad de libros en su poder
     */
    public int cantLibrosPrestados() {
        int cant = 0;
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null) {
                cant++;
            }
        }
        return cant;
    }

    /**
     * Devuelve true si el socio no tiene ningun prestamo vencido a la fecha actual.
     * Se evaluan los libros que tiene actualmente en su poder (no devueltos).
     * 
     * @return boolean true si puede pedir, false si tiene libros vencidos
     */
    public boolean puedePedir() {
        Calendar hoy = Calendar.getInstance();
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null && p.vencido(hoy)) {
                return false; // Tiene al menos un prestamo vencido
            }
        }
        return true;
    }

    /**
     * Metodo abstracto polimorfico que identifica el tipo o subclase de socio.
     * Obliga a cada clase derivada (Docente, Estudiante) a indicar su identidad.
     * 
     * @return String "Docente" o "Estudiante"
     */
    public abstract String soyDeLaClase();

    /**
     * Devuelve la representacion en String del socio segun el formato:
     * D.N.I.: <<dni>> || <<nombre y apellido>> (<<soyDeLaClase()>>) || Libros Prestados: <<cantLibrosPrestados()>>
     */
    @Override
    public String toString() {
        return "D.N.I.: " + this.getDniSocio() + " || " + 
               this.getNombre() + " (" + this.soyDeLaClase() + ") || " + 
               "Libros Prestados: " + this.cantLibrosPrestados();
    }
}
