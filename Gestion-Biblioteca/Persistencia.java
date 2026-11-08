import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class Persistencia {

    // Guarda todos los libros en un archivo CSV
    public static void guardarLibros(Biblioteca biblioteca, String archivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (Libro libro : biblioteca.getLibros()) {
                
                String linea = libro.getTitulo() + ";" + libro.getEditorial() + ";" + libro.getEdicion() + ";" + libro.getAnio();
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
                int edicion = Integer.parseInt(datos[2]);
                int anio = Integer.parseInt(datos[3]);
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
                
                // ORDEN DE GUARDADO: [0]DNI ; [1]Nombre ; [2]Dias ; [3]Tipo
                String base = socio.getDniSocio() + ";" + socio.getNombre() + ";" + socio.getDiasPrestamo() + ";" + socio.soyDeLaClase();
                
                if (socio instanceof Estudiante) {
                    Estudiante estudiante = (Estudiante) socio;
                    escritor.write(base + ";" + estudiante.getCarrera()); // [4] Extra
                } else {
                    Docente docente = (Docente) socio;
                    escritor.write(base + ";" + docente.getArea());       // [4] Extra
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
                
                // ORDEN DE CARGA: (Estrictamente igual al de guardado)
                int dni = Integer.parseInt(datos[0]);          // Índice 0
                String nombre = datos[1];                      // Índice 1
                int diasPrestamo = Integer.parseInt(datos[2]); // Índice 2 (Descomentado)
                String tipoSocio = datos[3];                   // Índice 3
                String extra = datos.length > 4 ? datos[4] : ""; // Índice 4

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

    // Guarda los préstamos iterando sobre la colección de socios
    public static void guardarPrestamos(Biblioteca biblioteca, String archivo) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            
            // 1. Recorremos los socios de la biblioteca
            for (Socio socio : biblioteca.getSocios()) {
                
                // 2. Por cada socio, recorremos su lista de préstamos
                // (Asumiendo que Socio tiene el método getPrestamos() según su relación 0..* en el UML)
                for (Prestamo prestamo : socio.getPrestamos()) {
                    
                    String strRetiro = formatoFecha.format(prestamo.getFechaRetiro().getTime());
                    String strDev = (prestamo.getFechaDevolucion() != null) ? 
                                    formatoFecha.format(prestamo.getFechaDevolucion().getTime()) : "null";
                    
                    // Formato CSV: dniSocio;tituloLibro;fechaRetiro;fechaDevolucion
                    String linea = socio.getDniSocio() + ";" + 
                                   prestamo.getLibro().getTitulo() + ";" + 
                                   strRetiro + ";" + 
                                   strDev;
                                   
                    escritor.write(linea);
                    escritor.newLine();
                }
            }
        } catch (IOException error) {
            System.out.println("Error guardando préstamos: " + error.getMessage());
        }
    }

    // Carga los préstamos y los vincula al Socio y al Libro correspondiente
    public static void cargarPrestamos(Biblioteca biblioteca, String archivo) {
        File archivoPrestamos = new File(archivo);
        if (!archivoPrestamos.exists()) return;

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoPrestamos))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                String[] datos = linea.split(";");
                int dni = Integer.parseInt(datos[0]);
                String titulo = datos[1];
                String strRetiro = datos[2];
                String strDev = datos[3];
                
                // 1. Buscar el Socio en memoria
                Socio socioEncontrado = biblioteca.buscarSocio(dni);
                
                // 2. Buscar el Libro en memoria
                Libro libroEncontrado = null;
                for (Libro l : biblioteca.getLibros()) {
                    if (l.getTitulo().equalsIgnoreCase(titulo)) {
                        libroEncontrado = l;
                        break;
                    }
                }
                
                // 3. Si ambos existen, instanciamos el préstamo
                if (socioEncontrado != null && libroEncontrado != null) {
                    try {
                        Calendar fechaRetiro = Calendar.getInstance();
                        fechaRetiro.setTime(formatoFecha.parse(strRetiro));
                        
                        Prestamo nuevoPrestamo = new Prestamo(fechaRetiro, socioEncontrado, libroEncontrado);
                        
                        if (!strDev.equals("null")) {
                            Calendar fechaDev = Calendar.getInstance();
                            fechaDev.setTime(formatoFecha.parse(strDev));
                            nuevoPrestamo.registrarFechaDevolucion(fechaDev);
                        }
                        
                        // 4. Vinculamos el préstamo a las colecciones del Socio y del Libro
                        socioEncontrado.agregarPrestamo(nuevoPrestamo); 
                        libroEncontrado.getPrestamos().add(nuevoPrestamo); 
                        
                    } catch (ParseException e) {
                        System.out.println("Error de formato en las fechas del préstamo: " + e.getMessage());
                    }
                }
            }
        } catch (IOException error) {
            System.out.println("Error cargando préstamos: " + error.getMessage());
        }
    }

}
