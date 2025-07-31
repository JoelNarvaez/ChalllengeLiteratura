package com.challengeAlura.ChallengeLiteratura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name= "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "birth_year", nullable = true)
    private Integer birth;
    @Column(name = "death_year", nullable = true)
    private Integer death;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    public List<Libro> getLibros() {
        return libros;
    }

    public Autor() {
    }

    public Autor(String nombre, Integer nacimiento, Integer fallecimiento) {
        this.name = nombre;
        this.birth = nacimiento;
        this.death = fallecimiento;
    }
    // Getters y Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth_year() {
        return birth;
    }

    public void setBirth_year(int birth_year) {
        this.birth = birth_year;
    }

    public int getDeath_year() {
        return death;
    }

    public void setDeath_year(int death_year) {
        this.death = death_year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBirth() {
        return birth;
    }

    public void setBirth(Integer birth) {
        this.birth = birth;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)",
                name,
                birth != null ? birth : "?",
                death != null ? death : "¿vivo?");
    }
}