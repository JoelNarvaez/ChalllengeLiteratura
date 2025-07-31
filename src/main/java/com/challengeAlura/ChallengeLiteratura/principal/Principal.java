package com.challengeAlura.ChallengeLiteratura.principal;

import com.challengeAlura.ChallengeLiteratura.model.Autor;
import com.challengeAlura.ChallengeLiteratura.model.DatosLibro;
import com.challengeAlura.ChallengeLiteratura.model.DatosRespuesta;
import com.challengeAlura.ChallengeLiteratura.model.Libro;
import com.challengeAlura.ChallengeLiteratura.repositorio.AutorRepositorio;
import com.challengeAlura.ChallengeLiteratura.repositorio.LibroRepositorio;
import com.challengeAlura.ChallengeLiteratura.service.ConsumoApi;
import com.challengeAlura.ChallengeLiteratura.service.ConvierteDatos;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books";
    private final String SEARCH = "?search=";
    private final LibroRepositorio libroRepositorio;
    private final AutorRepositorio autorRepositorio;

    public Principal(LibroRepositorio libroRepositorio, AutorRepositorio autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                    Joel Narvaez Martinez
                    API con persistencia de datos
                    ------------------------------------
                    1.- Consultar libro
                    2.- Listar libros buscados
                    3.- Buscar libros por idioma
                    4.- Listar autores
                    5.- Listar autores vivos en un año
                    6.- Mostrar resumen de descargas
                    7.- Top 10 libros más descargados
                    0.- Salir
                    ------------------------------------
                    """);

            System.out.print("Ingrese una opcion: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opcion invalida.");
                continue;
            }

            switch (opcion) {
                case 1 -> consultarLibro();
                case 2 -> listarLibrosBuscados();
                case 3 -> listarLibrosPorIdioma();
                case 4 -> listarAutores();
                case 5 -> listarAutoresVivosEnAnio();
                case 6 -> mostrarResumenDescargas();
                case 7 -> mostrarTop10Libros();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida");
            }
        }
    }

    private void listarLibrosBuscados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros guardados.");
        } else {
            System.out.println("\nLibros en la base de datos:");
            libros.forEach(System.out::println);
        }
        System.out.println();
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Ingrese el idioma (por ejemplo: 'en', 'es', 'fr'): ");
        String idioma = scanner.nextLine().trim();

        List<Libro> libros = libroRepositorio.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            System.out.println("Libros en idioma '" + idioma + "':");
            libros.forEach(System.out::println);
        }
        System.out.println();
    }

    private void listarAutores() {
        List<Autor> autores = autorRepositorio.findAllConLibros(); // cambio aquí

        if (autores.isEmpty()) {
            System.out.println("No hay autores guardados.");
        } else {
            for (Autor autor : autores) {
                System.out.println("\nAutor: " + autor.getName());
                System.out.println("Nacimiento: " + (autor.getBirth() != null ? autor.getBirth() : "¿?"));
                System.out.println("Fallecimiento: " + (autor.getDeath() != null ? autor.getDeath() : "¿?"));

                List<Libro> libros = autor.getLibros();
                if (libros == null || libros.isEmpty()) {
                    System.out.println("Libros: Ninguno registrado todavía.");
                } else {
                    System.out.println("Libros:");
                    libros.forEach(libro -> System.out.println(" - " + libro.getTitulo()));
                }
            }
        }
        System.out.println();
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("Ingrese el año que desea consultar: ");
        try {
            int anio = Integer.parseInt(scanner.nextLine());
            List<Autor> autores = autorRepositorio.buscarAutoresVivosEn(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en ese año.");
            } else {
                System.out.println("Autores vivos en el año " + anio + ":");
                autores.forEach(a -> System.out.println("- " + a.getName()));
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido.");
        }
        System.out.println();
    }

    private void consultarLibro() {
        System.out.print("Ingrese el nombre del libro o autor que desea consultar: ");
        String nombre = scanner.nextLine();

        String url = URL_BASE + SEARCH + nombre.replace(" ", "+");
        String json;

        try {
            json = consumoApi.obtenerDatos(url);
        } catch (Exception e) {
            System.out.println("Error al intentar conectarse con la API: " + e.getMessage());
            return;
        }

        if (json == null || json.isBlank()) {
            System.out.println("La API devolvió una respuesta vacía.");
            return;
        }

        try {
            DatosRespuesta respuesta = conversor.obtenerDatos(json, DatosRespuesta.class);
            List<DatosLibro> libros = respuesta.results();

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros.");
                return;
            }

            System.out.println("\nLibros encontrados:");
            for (int i = 0; i < libros.size(); i++) {
                System.out.println((i + 1) + ". " + libros.get(i).title());
            }

            int seleccion;
            while (true) {
                System.out.print("\nIngrese el número del libro que desea ver en detalle (0 si no es ninguno): ");
                try {
                    seleccion = Integer.parseInt(scanner.nextLine());
                    if (seleccion == 0) return;
                    if (seleccion < 1 || seleccion > libros.size()) {
                        System.out.println("Número inválido.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida.");
                }
            }

            DatosLibro libro = libros.get(seleccion - 1);
            Optional<Libro> libroExistente = libroRepositorio.findByTitulo(libro.title());

            if (libroExistente.isPresent()) {
                System.out.println("Ese libro ya fue guardado previamente.");
            } else {
                Autor autor;
                if (libro.authors().isEmpty()) {
                    autor = new Autor("Autor desconocido", null, null);
                } else {
                    var datosAutor = libro.authors().get(0);
                    Optional<Autor> autorExistente = autorRepositorio
                            .findByNameAndBirthAndDeath(
                                    datosAutor.getName(),
                                    datosAutor.getBirth_year(),
                                    datosAutor.getDeath_year()
                            );

                    autor = autorExistente.orElse(
                            new Autor(
                                    datosAutor.getName(),
                                    datosAutor.getBirth_year(),
                                    datosAutor.getDeath_year()
                            )
                    );
                }

                String idioma = libro.languages().isEmpty() ? "desconocido" : libro.languages().get(0);
                Libro libroAGuardar = new Libro(libro.title(), autor, idioma, libro.downloadCount());
                libroRepositorio.save(libroAGuardar);
                System.out.println("Libro guardado exitosamente.");
            }

            mostrarLibro(libro);

        } catch (RuntimeException e) {
            System.out.println("Error al convertir JSON: " + e.getMessage());
        }
        System.out.println();
    }

    private void mostrarResumenDescargas() {
        List<Libro> libros = libroRepositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros guardados.");
            return;
        }

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getDescargas)
                .summaryStatistics();

        System.out.println("\nResumen de descargas:");
        System.out.println("Cantidad de libros: " + stats.getCount());
        System.out.println("Total de descargas: " + stats.getSum());
        System.out.println("Promedio de descargas: " + stats.getAverage());
        System.out.println("Máximo de descargas: " + stats.getMax());
        System.out.println("Mínimo de descargas: " + stats.getMin());
        System.out.println();
    }

    private void mostrarTop10Libros() {
        List<Libro> topLibros = libroRepositorio.findTop10ByOrderByDescargasDesc();

        if (topLibros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\nTop 10 libros más descargados:");
        int i = 1;
        for (Libro libro : topLibros) {
            System.out.printf("%d. %s (Descargas: %.0f)%n", i++, libro.getTitulo(), libro.getDescargas());
        }
        System.out.println();
    }


    private void mostrarLibro(DatosLibro libro) {
        System.out.println("\nTítulo: " + libro.title());
        System.out.println("Autor(es): " + libro.authors().stream()
                .map(autor -> autor.getName())
                .toList());
        System.out.println("Temas: " + libro.subjects());
        System.out.println("Resumen: " + libro.summaries());
        System.out.println("Idiomas: " + libro.languages());
        System.out.println("Descargas: " + libro.downloadCount());
        System.out.println("Leer en línea: " + libro.formats().get("text/html"));
        System.out.println();
    }
}
