import java.util.*;

public class GestionBiblioteca {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // Crear la biblioteca
        Biblioteca biblioteca = new Biblioteca("Mi Biblioteca", new ArrayList<>(), new ArrayList<>());

        // Cargar datos guardados si existen
        Persistencia.cargarLibros(biblioteca, "libros.csv");
        Persistencia.cargarSocios(biblioteca, "socios.csv");
        Persistencia.cargarPrestamos(biblioteca, "prestamos.csv");

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
            System.out.println("7. Informes y Consultas");
            System.out.println("8. Buscar un libro (Ver información)");
            System.out.println("9. Buscar un socio (Ver información y préstamos)");
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

                case 7: // Informes y Consultas
                    System.out.println("\n===== INFORMES DE LA BIBLIOTECA =====");
                    
                    // 1. ¿Qué cantidad de socios de tipo Estudiante hay?
                    System.out.println("\n-> Cantidad de socios de tipo Estudiante:");
                    int cantEstudiantes = biblioteca.cantidadSociosPorTipo("Estudiante");
                    System.out.println("Rta: " + cantEstudiantes + " estudiantes.");
                    
                    // 2. ¿Cuál es la lista de docentes que nunca han adeudado ni adeudan libros?
                    System.out.println("\n-> Docentes responsables (sin deudas históricas):");
                    System.out.print(biblioteca.listaDeDocentesResponsables());
                    
                    // 3. ¿Qué socio tiene prestado el libro “Programando con JAVA”?
                    System.out.println("\nIngrese el título del libro a buscar (ej. Programando con JAVA): ");
                    String tituloBuscado = teclado.nextLine(); // Leemos lo que escribe el usuario
                    
                    Libro libroBuscado = biblioteca.buscarLibroPrestado(tituloBuscado);
                    if (libroBuscado != null) {
                        try {
                            String nombreSocio = biblioteca.quienTieneElLibro(libroBuscado);
                            System.out.println("Rta: El libro '" + tituloBuscado + "' lo tiene el socio " + nombreSocio);
                        } catch (LibroNoPrestadoException e) {
                            System.out.println("Rta: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Rta: El libro '" + tituloBuscado + "' no existe o actualmente no se encuentra prestado.");
                    }

                    // 4. Qué libros están prestados, cuyo plazo de devolución se encuentre vencido.
                    System.out.println("\n-> Préstamos con plazo vencido:");
                    ArrayList<Prestamo> vencidos = biblioteca.prestamosVencidos();
                    if (vencidos.isEmpty()) {
                        System.out.println("Rta: Excelente, no hay préstamos vencidos al día de la fecha.");
                    } else {
                        for (Prestamo p : vencidos) {
                            System.out.println("- Libro: '" + p.getLibro().getTitulo() + "' | Socio deudor: " + p.getSocio().getNombre());
                        }
                    }
                    
                    case 8: // Buscar Libro (Ver información completa)
                    System.out.print("Ingrese el título del libro a buscar: ");
                    String tituloBuscar = teclado.nextLine();
                    boolean libroEncontrado = false;
                    
                    System.out.println("\n--- Resultados de la búsqueda ---");
                    // Recorremos la biblioteca en caso de que existan múltiples copias del mismo título
                    for (Libro l : biblioteca.getLibros()) {
                        if (l.getTitulo().equalsIgnoreCase(tituloBuscar)) {
                            System.out.println("Título: " + l.getTitulo());
                            System.out.println("Edición: " + l.getEdicion());
                            System.out.println("Editorial: " + l.getEditorial());
                            System.out.println("Año: " + l.getAnio());
                            System.out.println("Estado actual: " + (l.prestado() ? "PRESTADO" : "DISPONIBLE EN BIBLIOTECA"));
                            System.out.println("---------------------------------");
                            libroEncontrado = true;
                        }
                    }
                    if (!libroEncontrado) {
                        System.out.println("No se encontraron libros con el título '" + tituloBuscar + "'.");
                    }
                    break;

                case 9: // Buscar Socio (Ver información y préstamos)
                    System.out.print("Ingrese el DNI del socio a buscar: ");
                    int dniBusqueda = teclado.nextInt();
                    teclado.nextLine(); // Limpiar el buffer del scanner
                    
                    Socio socioBuscado = biblioteca.buscarSocio(dniBusqueda);
                    
                    if (socioBuscado != null) {
                        System.out.println("\n--- Información del Socio ---");
                        System.out.println(socioBuscado.toString()); // Muestra nombre, DNI, etc.
                        System.out.println("Tipo de Socio: " + socioBuscado.soyDeLaClase());
                        System.out.println("Libros actualmente en su poder: " + socioBuscado.cantLibrosPrestados());
                        
                        System.out.println("\n--- Historial de Préstamos ---");
                        if (socioBuscado.getPrestamos().isEmpty()) {
                            System.out.println("-> Este socio no tiene préstamos registrados.");
                        } else {
                            // Si tiene préstamos, recorremos su lista usando el toString() de Prestamo
                            for (Prestamo p : socioBuscado.getPrestamos()) {
                                System.out.println("-> " + p.toString());
                            }
                        }
                    } else {
                        System.out.println("No se encontró ningún socio registrado con el DNI " + dniBusqueda + ".");
                    }
                    break;

                case 0: // Salir
                    System.out.println("Guardando datos antes de salir...");
                    Persistencia.guardarLibros(biblioteca, "libros.csv");
                    Persistencia.guardarSocios(biblioteca, "socios.csv");
                    Persistencia.guardarPrestamos(biblioteca, "prestamos.csv");
                    System.out.println("Cerrando la biblioteca.");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }

        teclado.close();
    }
}
