import java.util.Calendar;

/**
 * Modela el prestamo de un libro a un socio en una fecha determinada.
 * Asienta la fecha de retiro, la fecha de devolucion y permite verificar
 * si el prestamo se encuentra vencido.
 * 
 * @author Subgrupo 1 (Prestamo y Socio)
 * @version 1.0 - TP7 Integrador
 */
public class Prestamo {
    private Calendar fechaRetiro;
    private Calendar fechaDevolucion;
    private Socio socio;
    private Libro libro;

    /**
     * Constructor de la clase Prestamo.
     * Se crea con la fecha de retiro solamente; la fecha de devolucion queda inicialmente en null.
     * 
     * @param p_fechaRetiro Fecha en que se retira el libro
     * @param p_socio Socio que solicita el prestamo
     * @param p_libro Libro solicitado
     */
    public Prestamo(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        this.setFechaRetiro(p_fechaRetiro);
        this.setFechaDevolucion(null);
        this.setSocio(p_socio);
        this.setLibro(p_libro);
    }

    // Setters y Getters
    private void setFechaRetiro(Calendar p_fechaRetiro) {
        this.fechaRetiro = p_fechaRetiro;
    }

    public Calendar getFechaRetiro() {
        return this.fechaRetiro;
    }

    private void setFechaDevolucion(Calendar p_fechaDevolucion) {
        this.fechaDevolucion = p_fechaDevolucion;
    }

    public Calendar getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    private void setSocio(Socio p_socio) {
        this.socio = p_socio;
    }

    public Socio getSocio() {
        return this.socio;
    }

    private void setLibro(Libro p_libro) {
        this.libro = p_libro;
    }

    public Libro getLibro() {
        return this.libro;
    }

    /**
     * Registra la fecha en la cual el libro es devuelto a la biblioteca.
     * @param p_fecha Fecha efectiva de devolucion
     */
    public void registrarFechaDevolucion(Calendar p_fecha) {
        this.setFechaDevolucion(p_fecha);
    }

    /**
     * Devuelve true si la fecha pasada como parametro es mayor que la fecha
     * de vencimiento (fecha de retiro mas los dias de prestamos asignados al socio).
     * 
     * @param p_fecha Fecha a comparar (usualmente la fecha actual o la fecha de devolucion)
     * @return boolean true si esta vencido, false en caso contrario
     */
    public boolean vencido(Calendar p_fecha) {
        // Clonamos la fecha de retiro para calcular el vencimiento sin modificar el original
        Calendar fechaVencimiento = (Calendar) this.getFechaRetiro().clone();
        fechaVencimiento.add(Calendar.DAY_OF_YEAR, this.getSocio().getDiasPrestamo());
        
        // Comprueba si p_fecha es posterior a la fecha de vencimiento
        return p_fecha.after(fechaVencimiento);
    }

    /**
     * Devuelve una representacion en String del prestamo segun la consigna:
     * Retiro: <<fecha de retiro>> - Devolución: <<fecha de devolución>>
     * Libro: <<título del libro>>
     * Socio: <<nombre del socio>>
     */
    @Override
    public String toString() {
        String strRetiro = String.format("%04d/%02d/%02d",
            this.getFechaRetiro().get(Calendar.YEAR),
            this.getFechaRetiro().get(Calendar.MONTH) + 1,
            this.getFechaRetiro().get(Calendar.DAY_OF_MONTH));
            
        String strDevolucion;
        if (this.getFechaDevolucion() != null) {
            strDevolucion = String.format("%04d/%02d/%02d",
                this.getFechaDevolucion().get(Calendar.YEAR),
                this.getFechaDevolucion().get(Calendar.MONTH) + 1,
                this.getFechaDevolucion().get(Calendar.DAY_OF_MONTH));
        } else {
            strDevolucion = "No devuelto";
        }

        return "Retiro: " + strRetiro + " - Devolución: " + strDevolucion + "" +
               "Libro: " + this.getLibro().getTitulo() + " " +
               "Socio: " + this.getSocio().getNombre();
    }
}
