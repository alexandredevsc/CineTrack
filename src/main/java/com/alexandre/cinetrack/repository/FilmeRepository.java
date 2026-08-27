package com.alexandre.cinetrack.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandre.cinetrack.entity.Filme;
import com.alexandre.cinetrack.entity.StatusFilme;

/*
 * Interface responsável pelas operações de banco
 * relacionadas à Entity Filme.
 */
public interface FilmeRepository extends JpaRepository<Filme, Long> {

    /*
     * Lista todos os filmes em ordem alfabética.
     */
    List<Filme> findAllByOrderByTituloAsc();

    /*
     * Lista os filmes pertencentes a determinado gênero.
     */
    List<Filme> findByGeneroIdOrderByTituloAsc(Long generoId);

    /*
     * Lista os filmes de acordo com o status.
     */
    List<Filme> findByStatusOrderByTituloAsc(StatusFilme status);

    /*
     * Lista os filmes que possuem nota igual ou superior
     * ao valor informado, da maior nota para a menor.
     */
    List<Filme> findByNotaGreaterThanEqualOrderByNotaDesc(
        BigDecimal notaMinima
    );

    /*
     * Verifica se um gênero está associado a algum filme.
     */
    boolean existsByGeneroId(Long generoId);
}