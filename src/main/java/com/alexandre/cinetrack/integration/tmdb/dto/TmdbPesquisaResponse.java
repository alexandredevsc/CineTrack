package com.alexandre.cinetrack.integration.tmdb.dto;

import java.util.List;

/*
 * Representa a resposta completa de uma pesquisa no TMDB.
 *
 * A API coloca todos os filmes encontrados dentro
 * de uma propriedade chamada "results".
 */
public record TmdbPesquisaResponse(
    List<TmdbFilmeResponse> results
) {
}