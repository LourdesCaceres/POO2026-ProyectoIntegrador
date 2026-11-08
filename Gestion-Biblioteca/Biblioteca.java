import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
/**
 * Clase que representa las características y acciones propias de una biblioteca.
 * Administra colecciones de socios y libros, así como la operatoria de préstamos.
 * 
 * @author Lourdes G. Caceres
 * @author Gabriel Esquivel
 * @author Mirta Fernandez 
 * 
 * @version 1.0 02/11/2026
 */
public class Biblioteca {

    private String nombre;
    private ArrayList<Socio> socios;
    private ArrayList<Libro> libros;
    
    /**
     * 1er Constructor para crear una Biblioteca inicializando sus colecciones vacías.
     * 
     * @param p_nombre Nombre de la biblioteca.
     */
    public Biblioteca(String p_nombre){
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
    }
    /**
     * 2do Constructor para crear una Biblioteca con colecciones de socios y libros preexistentes.
     * 
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socios Colección de socios inicial.
     * @param p_libros Colección de libros inicial.
     */
    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios, ArrayList<Libro> p_libros){
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(p_libros);
    }
    
    //Mutadores (SETTERS)
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }
    private void setSocios(ArrayList<Socio> p_socios) {
        this.socios = p_socios;
    }
    private void setLibros(ArrayList<Libro> p_libros) {
        this.libros = p_libros;
    }
    //Observadores (GETTERS)
    public String getNombre() {
        return this.nombre;
    }
    public ArrayList<Socio> getSocios() {
        return this.socios;
    }
    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    /**
     * Instancia y agrega un nuevo libro a la colección de la biblioteca.
     * 
     * @param p_titulo Título del libro.
     * @param p_edicion Número de edición del libro.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación del libro.
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        this.getLibros().add(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    /**
     * Método que instancia y agrega un socio de tipo Estudiante a la lista.
     * 
     * @param p_dniSocio DNI del estudiante.
     * @param p_nombre Nombre completo del estudiante.
     * @param p_carrera Carrera que cursa el estudiante.
     */
    public void nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera){
        this.getSocios().add(new Estudiante(p_dniSocio, p_nombre, p_carrera));
    }
    /**
     * Método que instancia y agrega un socio tipo Docente a la lista.
     * 
     * @param p_dniSocio DNI del docente.
     * @param p_nombre Nombre completo del docente.
     * @param p_area Área de conocimiento o departamento del docente.
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area){
        this.getSocios().add(new Docente(p_dniSocio, p_nombre, p_area));
    }
    /**
     * Registra la devolución de un libro prestado asignándole la fecha actual.
     * 
     * @param p_libro El libro que se desea devolver.
     * @throws LibroNoPrestadoException Si el libro no se encuentra prestado actualmente.
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro '" + p_libro.getTitulo() + "' no se encuentra prestado actualmente.");
        }
        p_libro.ultimoPrestamo().registrarFechaDevolucion(Calendar.getInstance());
    }
    /**
     * Da de baja a un socio de tipo Estudiante si no posee libros en su poder.
     * 
     * @param p_estudiante Objeto Estudiante a desafiliar.
     * @return true si el estudiante fue removido con éxito, false si no existe o tiene libros pendientes.
     */
    public boolean desafiliarSocioEstudiante(Estudiante p_estudiante){
        if (this.getSocios().contains(p_estudiante) && p_estudiante.cantLibrosPrestados() == 0) {
            return this.getSocios().remove(p_estudiante);
        }
        return false;
    }
    /**
     * Da de baja a un socio de tipo Docente si no posee libros en su poder.
     * 
     * @param p_docente Objeto Docente a desafiliar.
     * @return true si el docente fue removido con éxito, false si no existe o tiene libros pendientes.
     */
    public boolean desafiliarSocioDocente(Docente p_docente){
        if(this.getSocios().contains(p_docente) && p_docente.cantLibrosPrestados() == 0){
            return this.getSocios().remove(p_docente);
        }
        return false;
    }
    /**
     * Filtra y devuelve todos los prestamos vencidos al dia de hoy
     * 
     * @return prestamosVencidos (ArrayList<Prestamo>).
     */
    public ArrayList<Prestamo> prestamosVencidos(){
        ArrayList<Prestamo> prestamosVencidos = new ArrayList<>();
        Calendar fechaActual = Calendar.getInstance();

        for (Socio unSocio : this.getSocios()) {
            for (Prestamo unPrestamo : unSocio.getPrestamos()) {
                if (unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(fechaActual)) {
                    prestamosVencidos.add(unPrestamo);
                }
            }
        }
        return prestamosVencidos;
    }
    /**
     * Identifica y devuelve a los docentes que nunca han adeudados libros.
     * 
     * @return responsables (ArrayList<Docente>)
     */
    public ArrayList<Docente> docentesResponsables(){
        ArrayList<Docente> responsables = new ArrayList<>();

        for(Socio unSocio : this.getSocios()){
            if(unSocio instanceof Docente){
                Docente unDocente = (Docente) unSocio;

                if(unDocente.esResponsable()){
                    responsables.add(unDocente);
                }
            }
        }
        return responsables;
    }
    /**
     * Busca en la biblioteca un libro específico que se encuentre actualmente prestado.
     * 
     * @param p_titulo Título exacto del libro a buscar.
     * @return El objeto Libro si existe y está prestado, null en caso contrario.
     */
    public Libro buscarLibroPrestado(String p_titulo) {
        for (Libro libro : libros) {
            if (libro.prestado() && libro.getTitulo().equals(p_titulo)) {
                return libro;
            }
        }
        return null;
    }
    /**
     * Realiza la operación de préstamo de un libro a un socio determinado.
     * 
     * @param p_fechaRetiro Fecha en la que se retira el libro de la biblioteca.
     * @param p_socio Socio que solicita el préstamo.
     * @param p_libro Libro que se desea llevar.
     * @return true si el préstamo se realizó con éxito, false si el socio no puede pedir más o el libro ya está prestado.
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        if (p_socio != null) {
            if (p_socio.puedePedir() && !p_libro.prestado()) {
                p_socio.agregarPrestamo(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Cuenta la cantidad de socios registrados de un tipo específico (Docente o Estudiante).
     * 
     * @param p_objeto Cadena de texto con el tipo de socio a buscar ("Estudiante" o "Docente").
     * @return Cantidad de socios encontrados de ese tipo.
     */
    public int cantidadSociosPorTipo(String p_objeto) {
        int cont = 0;
        for(Socio socios : this.getSocios()) {
            if (socios.soyDeLaClase().equalsIgnoreCase(p_objeto)) {
                ++cont;
            }
        }
        return cont;
    }
    /**
     * Averigua el nombre del socio que tiene en su poder un libro específico.
     * 
     * @param p_libro El libro a consultar.
     * @return El nombre del socio que lo tiene prestado.
     * @throws LibroNoPrestadoException Si el libro se encuentra disponible en la biblioteca.
     */
    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (p_libro.prestado()) {
            return p_libro.ultimoPrestamo().getSocio().getNombre();
        } else {
            throw new LibroNoPrestadoException("El libro '" + p_libro.getTitulo() +  "' se encuentra en la biblioteca\n");
        }
    }
    /**
     * Busca y retorna un socio por su número de documento.
     * 
     * @param p_dni DNI del socio a buscar.
     * @return socio (objeto Socio) encontrado, o null si no está registrado.
     */
    public Socio buscarSocio(int p_dni) {
        Socio socio = null;
        for(Socio socios : this.getSocios()) {
            if (socios.getDniSocio() == p_dni) {
                socio = socios;
            }
        }
        return socio;
    }
    /**
     * Genera un listado numerado con la información de todos los socios y un conteo por tipo.
     * 
     * @return listaSocio Cadena de texto formateada con la lista de socios.
     */
    public String listaDeSocios() {
        int i = 0;
        int docentes = 0;
        int estudiantes = 0;
        String listaSocio = "\tLista de Socios:\n\n";

        for(Socio socios : this.getSocios()) {
            ++i;
            if (socios.soyDeLaClase().equalsIgnoreCase("Docente")) {
                ++docentes;
            } else if (socios.soyDeLaClase().equalsIgnoreCase("Estudiante")) {
                ++estudiantes;
            }

            listaSocio = listaSocio + i + ")" + socios.toString() + "\n";
        }

        return listaSocio + "\n******************************************\nCant. Socios tipo Estudiante: " + estudiantes + "\nCant. Socios tipo Docente: " + docentes + "\n******************************************\n";
    }
    /**
     * Genera un listado de todos los títulos distintos de libros en la biblioteca, sin repetir.
     * 
     * @return Cadena de texto formateada con la lista de títulos únicos.
     */
    public String listaDeTitulos() {
            int i = 0;
            HashSet<String> titulos = new HashSet<>();

            for(Libro unLibro : this.getLibros()) {
                titulos.add(unLibro.getTitulo());
            }

            String listaTitulos = "\tLista de Títulos:\n\n";

            for(String unLibro : titulos) {
                ++i;
                listaTitulos = listaTitulos + i + ") " + unLibro + "\n";
            }
            return listaTitulos;
    }
    /**
     * Genera un listado de todos los libros con su información y estado actual de préstamo.
     * 
     * @return Cadena de texto formateada con la lista de libros y disponibilidad.
     */
    public String listaDeLibros() {
        int i = 0;
        String listaLibros = "\tLista de Libros:\n\n";

        for(Libro libros : this.getLibros()) {
            ++i;
            if (!libros.prestado()) {
                listaLibros = listaLibros + i + ") " + libros.toString() + "||Prestado:(NO)\n";
            } else {
                listaLibros = listaLibros + i + ") " + libros.toString() + "||Prestado:(SI)\n";
            }
        }

        return listaLibros;
    }
    /**
     * Genera un listado con la información de aquellos docentes que cumplieron el criterio de responsabilidad.
     * 
     * @return Cadena de texto formateada con la lista de docentes responsables.
     */
    public String listaDeDocentesResponsables() {
        String listaResponsables = "\tLista de Docentes Responsables:\n\n";

        for(Docente docentes : this.docentesResponsables()) {
            listaResponsables = listaResponsables + "*" + docentes.toString() + "\n";
        }

        return listaResponsables;
    }

}

    
