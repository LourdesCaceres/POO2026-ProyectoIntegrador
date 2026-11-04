import java.util.ArrayList;
/**
 * Clase que abstrae la idea de un libro en una biblioteca que puede ser prestado a un socio de la misma.
 * Incluye métodos para comprobar el último préstamo realizado y si actualmente lo tiene alguien.
 * 
 * @author Chavez, Tobías Acdel 
 * @version 1.0 - 03/11/2026
 */
public class Libro
{
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;

    /**
     * PRIMER CONSTRUCTOR DE LA CLASE Libro: para la cardinalidad muchos.
     * 
     * @param p_titulo: String - titulo del libro.
     * @param p_edicion: int - edición específica del libro.
     * @param p_editorial: String - editorial que publicó el libro.
     * @param p_anio: int - año de publicación del libro.
     * @param p_prestamos: ArrayList<Prestamo> - colección de las veces que el libro fue prestado.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }
    
    /**
     * SEGUNDO CONSTRUCTOR DE LA CLASE Libro: para la cardinalidad ninguno.
     * 
     * @param p_titulo: String - titulo del libro.
     * @param p_edicion: int - edición específica del libro.
     * @param p_editorial: String - editorial que publicó el libro.
     * @param p_anio: int - año de publicación del libro.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
    }
    
    //SETTERS
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }
    
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }
    
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }
    
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }
    
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }

    //GETTERS
    public String getTitulo(){
        return this.titulo;
    }
    
    public int getEdicion(){
        return this.edicion;
    }
    
    public String getEditorial(){
        return this.editorial;
    }
    
    public int getAnio(){
        return this.anio;
    }
    
    public ArrayList<Prestamo> getPrestamos(){
        return this.prestamos;
    }
    
    
    /**
     * Concatena una cadena informativa con el título del libro.
     */
    public String toString(){
        return "Titulo: " + this.getTitulo();
    }
    
    /**
     * Recorre el array de préstamos del libro y obtiene la última vez
     * que fue prestado, devolviendolo.
     * 
     * @return último prestamo registrado del libro.
     */
    private Prestamo ultimoPrestamo(){
        Prestamo ultPrestamo = new Prestamo();
        for(Prestamo unPrestamo : this.getPrestamos()){
            ultPrestamo = unPrestamo;
        }
        
        return ultPrestamo;
    }
    
    /**
     * Verifica si el libro fue prestado y aún no se devolvió. Para ello
     * utiliza el método ultimoPrestado() para obtener el último préstamo registrado
     * del libro y verificar su fecha de devolución, si tiene fecha significa que el libro fue
     * devuelto y está en la biblioteca, caso contrario está siendo prestado.
     * 
     * @return false/true según si el libro fue prestado a algún socio.
     */
    private boolean prestado(){
        Prestamo ultimo = this.ultimoPrestamo();
        
        if(ultimo != null && ultimo.getFechaDevolucion() == null){
            return true;
        }else{
            return false;
        }
    }
}