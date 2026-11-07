import java.util.*;
import java.util.Calendar;

public class GestionBiblioteca {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // Crear la biblioteca
        Biblioteca biblioteca = new Biblioteca("Mi Biblioteca", new ArrayList<>(), new ArrayList<>());

        // Cargar datos guardados si existen
        Persistencia.cargarLibros(biblioteca, "libros.csv");
        Persistencia.cargarSocios(biblioteca, "socios.csv");

        System.out.println("Biblioteca cargada con " + biblioteca.getLibros().size() + " libros y " +
                biblioteca.getSocios().size() + " socios.\n");

        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU BIBLIOTECA =====");
            System.out.println("1. Agregar libro");
            System.out.println("2. Agregar socio");
            System.out.println("3. Prestar libro");
            System.out.println("4. Devolver libro");
            System.out.println("5. Mostrar libros");
            System.out.println("6. Mostrar socios");
            System.out.println("0. Salir");
            System.out.print("Elegir una opcion: ");

            opcion = teclado.nextInt();
            teclado.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1: // Agregar libro
                    System.out.print("Titulo: ");
                    String titulo = teclado.nextLine();
                    System.out.print("Edicion: ");
                    int edicion = teclado.nextInt();
                    teclado.nextLine();
                    System.out.print("Editorial: ");
                    String editorial = teclado.nextLine();
                    System.out.print("Año: ");
                    int anio = teclado.nextInt();
                    teclado.nextLine();
                    biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
                    System.out.println(" Libro agregado.");
                    break;

                case 2: // Agregar socio
                    System.out.print("DNI del socio: ");
                    int dni = teclado.nextInt();
                    teclado.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = teclado.nextLine();
                    System.out.print("Tipo (E = Estudiante / D = Docente): ");
                    String tipo = teclado.nextLine();
                    if (tipo.equalsIgnoreCase("E")) {
                        System.out.print("Carrera: ");
                        String carrera = teclado.nextLine();
                        biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
                    } else {
                        System.out.print("area: ");
                        String area = teclado.nextLine();
                        biblioteca.nuevoSocioDocente(dni, nombre, area);
                    }
                    System.out.println("Socio agregado.");
                    break;

                case 3: // Prestar libro
                    System.out.println(biblioteca.listaDeSocios());
                    System.out.print("DNI del socio que pide el libro: ");
                    int dniSocio = teclado.nextInt();
                    teclado.nextLine();
                    Socio socio = biblioteca.buscarSocio(dniSocio);
                    if (socio == null) {
                        System.out.println("Socio no encontrado.");
                        break;
                    }

                    System.out.println(biblioteca.listaDeLibros());
                    System.out.print("Numero del libro a prestar: ");
                    int nroLibro = teclado.nextInt();
                    teclado.nextLine();
                    if (nroLibro < 1 || nroLibro > biblioteca.getLibros().size()) {
                        System.out.println("Libro no valido.");
                        break;
                    }
                    Libro libro = biblioteca.getLibros().get(nroLibro - 1);

                    boolean ok = biblioteca.prestarLibro(Calendar.getInstance(), socio, libro);
                    if (ok) System.out.println("Prestamo realizado.");
                    else System.out.println("No se pudo prestar el libro. Quiza ya esta prestado o el socio no puede pedir mas.");
                    break;

                case 4: // Devolver libro
                    System.out.println(biblioteca.listaDeLibros());
                    System.out.print("Numero del libro a devolver: ");
                    int nroDev = teclado.nextInt();
                    teclado.nextLine();
                    if (nroDev < 1 || nroDev > biblioteca.getLibros().size()) {
                        System.out.println("Libro no valido.");
                        break;
                    }
                    Libro libroDev = biblioteca.getLibros().get(nroDev - 1);
                    try {
                        biblioteca.devolverLibro(libroDev);
                        System.out.println("Libro devuelto.");
                    } catch (LibroNoPrestadoException e) {
                        System.out.println("Libro invalido " + e.getMessage());
                    }
                    break;

                case 5: // Mostrar libros
                    System.out.println(biblioteca.listaDeLibros());
                    break;

                case 6: // Mostrar socios
                    System.out.println(biblioteca.listaDeSocios());
                    break;

                case 0: // Salir
                    System.out.println("Guardando datos antes de salir...");
                    Persistencia.guardarLibros(biblioteca, "libros.csv");
                    Persistencia.guardarSocios(biblioteca, "socios.csv");
                    System.out.println("Cerrando la biblioteca.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }

        teclado.close();
    }
}
