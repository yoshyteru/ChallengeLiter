package com.exemplo.literatura.repository;

import com.exemplo.literatura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // DERIVED QUERY: busca por titulo ignorando maiusculas/minusculas
    // Spring gera: SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(%?%)
    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    // DERIVED QUERY: busca por idioma exato
    // Spring gera: SELECT * FROM livros WHERE idioma = ?
    List<Livro> findByIdioma(String idioma);

    // DERIVED QUERY: conta quantos livros tem no idioma
    // Spring gera: SELECT COUNT(*) FROM livros WHERE idioma = ?
    long countByIdioma(String idioma);
}