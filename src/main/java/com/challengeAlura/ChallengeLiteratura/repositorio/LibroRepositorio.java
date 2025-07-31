package com.challengeAlura.ChallengeLiteratura.repositorio;

import com.challengeAlura.ChallengeLiteratura.model.Autor;
import com.challengeAlura.ChallengeLiteratura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface LibroRepositorio extends JpaRepository<Libro, Integer> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByIdioma(String idioma); // Para filtrar por idioma
    List<Libro> findTop10ByOrderByDescargasDesc();

}
