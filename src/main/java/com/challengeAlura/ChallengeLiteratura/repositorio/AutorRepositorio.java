

package com.challengeAlura.ChallengeLiteratura.repositorio;

import com.challengeAlura.ChallengeLiteratura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNameAndBirthAndDeath(String name, Integer birthYear, Integer deathYear);

    @Query("SELECT a FROM Autor a WHERE (a.birth IS NULL OR a.birth <= :anio) AND (a.death IS NULL OR a.death >= :anio)")
    List<Autor> buscarAutoresVivosEn(int anio);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllConLibros();
}
