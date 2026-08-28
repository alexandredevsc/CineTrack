package com.alexandre.cinetrack.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Representa os dados de um filme devolvidos pela API do TMDB.
 *
 * Usamos record porque este objeto apenas transporta dados
 * e não precisa permitir alterações depois de ser criado.
 */
public record TmdbFilmeResponse(
    Long id,

    String title,

    /*
     * Converte o nome release_date do JSON para releaseDate no Java.
     */
    @JsonProperty("release_date")
    String releaseDate,

    /*
     * Recebe somente o caminho da capa.
     * A URL completa será montada em outra classe.
     */
    @JsonProperty("poster_path")
    String posterPath
) {
}
