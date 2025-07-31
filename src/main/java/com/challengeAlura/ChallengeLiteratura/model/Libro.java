package com.challengeAlura.ChallengeLiteratura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private String idioma;
    private int descargas;

    public Libro() {}

    public Libro(String titulo, Autor autor, String idioma, int descargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.descargas = descargas;
    }

    public Libro(DatosLibro libro) {
        this.titulo = libro.title();
        this.descargas = libro.downloadCount();
        this.idioma = libro.languages().isEmpty() ? "desconocido" : libro.languages().get(0);

        if (libro.authors().isEmpty()) {
            this.autor = new Autor("Autor desconocido", null, null);
        } else {
            var datosAutor = libro.authors().get(0);
            this.autor = new Autor(
                    datosAutor.getName(),
                    datosAutor.getBirth_year(),
                    datosAutor.getDeath_year()
            );
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Getters y Setters
    @Override
    public String toString() {
        return """
                LIBRO
            ──────────────────────────────
             Título     : %s
             Autor      : %s
             Idioma     : %s
             Descargas  : %,d
            ──────────────────────────────
            """.formatted(titulo, autor.getName(), idioma, descargas);
    }

    public double getDescargas() {
        return descargas;
    }
}
