import java.io.*;
import java.util.*;

public class Persistencia {

    // Guarda todos los libros en un archivo CSV
    public static void guardarLibros(Biblioteca biblioteca, String archivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (Libro libro : biblioteca.getLibros()) {
                
                String linea = libro.getTitulo() + ";" + libro.getEditorial() + ";" + libro.getAnio() + ";" + libro.getEdicion();
                escritor.write(linea);
                escritor.newLine();
            }
        } catch (IOException error) {
            System.out.println("Error guardando libros: " + error.getMessage());
        }
    }

    // Carga libros desde un archivo CSV
    public static void cargarLibros(Biblioteca biblioteca, String archivo) {
        File archivoLibros = new File(archivo);
        if (!archivoLibros.exists()) return;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoLibros))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                String titulo = datos[0];
                String editorial = datos[1];
                int anio = Integer.parseInt(datos[2]);
                int edicion = Integer.parseInt(datos[3]);
                biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
            }
        } catch (IOException error) {
            System.out.println("Error cargando libros: " + error.getMessage());
        }
    }

    // Guarda todos los socios en un archivo CSV
    public static void guardarSocios(Biblioteca biblioteca, String archivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (Socio socio : biblioteca.getSocios()) {
                String base = socio.getDniSocio() + ";" + socio.getNombre() + ";" + socio.getDiasPrestamo() + ";" + socio.soyDeLaClase();
                if (socio instanceof Estudiante) {
                    Estudiante estudiante = (Estudiante) socio;
                    escritor.write(base + ";" + estudiante.getCarrera());
                } else {
                    Docente docente = (Docente) socio;
                    escritor.write(base + ";" + docente.getArea());
                }
                escritor.newLine();
            }
        } catch (IOException error) {
            System.out.println("Error guardando socios: " + error.getMessage());
        }
    }

    // Carga todos los socios desde un archivo CSV
    public static void cargarSocios(Biblioteca biblioteca, String archivo) {
        File archivoSocios = new File(archivo);
        if (!archivoSocios.exists()) return;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoSocios))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                int dni = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                int diasPrestamo = Integer.parseInt(datos[2]);
                String tipoSocio = datos[3];
                String extra = datos.length > 4 ? datos[4] : "";

                if (tipoSocio.equalsIgnoreCase("Estudiante")) {
                    biblioteca.nuevoSocioEstudiante(dni, nombre, extra);
                } else {
                    biblioteca.nuevoSocioDocente(dni, nombre, extra);
                }
            }
        } catch (IOException error) {
            System.out.println("Error cargando socios: " + error.getMessage());
        }
    }
}
