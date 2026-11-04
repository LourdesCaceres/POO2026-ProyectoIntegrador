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
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
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
        this.getLibros().add(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        if (p_socio != null) {
            if (p_socio.puedePedir() && !p_libro.prestado()) {
                p_socio.agregarPrestamo(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                //p_socio.add(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                //p_libro.add(new Prestamo(p_fechaRetiro, p_socio, p_libro));
                //p_libro.agregarPrestamo(new Prestamo(p_fechaRetiro, p_socio, p_libro));
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

    public String listaDeDocentesResponsables() {
        String listaResponsables = "\tLista de Docentes Responsables:\n\n";

        for(Docente docentes : this.getSocios().docenteResponsables()) {
            listaResponsables = listaResponsables + "*" + docentes.toString() + "\n";
        }

        return listaResponsables;
    }

}

    
