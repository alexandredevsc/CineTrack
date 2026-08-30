package com.alexandre.cinetrack.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            BigDecimal notaMinima);

    /*
     * Verifica se um gênero está associado a algum filme.
     */
    boolean existsByGeneroId(Long generoId);

    /*
     * Localiza os filmes que ainda não possuem capa.
     *
     * A consulta considera tanto valores nulos quanto textos vazios,
     * garantindo que nenhum cadastro pendente seja ignorado.
     *
     * JPQL utiliza o nome da Entity e dos atributos Java,
     * e não os nomes da tabela e das colunas do MySQL.
     */
    @Query("""
            SELECT filme
            FROM Filme filme
            WHERE filme.capaUrl IS NULL
               OR TRIM(filme.capaUrl) = ''
            ORDER BY filme.titulo ASC
            """)
    List<Filme> findFilmesSemCapa();
}