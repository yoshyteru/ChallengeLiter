package com.exemplo.literatura.repository;

import com.exemplo.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // DERIVED QUERY: busca autor pelo nome exato
    // Spring gera: SELECT * FROM autores WHERE nome = ?
    Optional<Autor> findByNome(String nome);

    // DERIVED QUERY: autores vivos em determinado ano
    // anoNascimento <= ano AND anoFalecimento >= ano
    // Spring gera: SELECT * FROM autores WHERE ano_nascimento <= ? AND ano_falecimento >= ?
    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(
            Integer anoNascimento, Integer anoFalecimento);
}