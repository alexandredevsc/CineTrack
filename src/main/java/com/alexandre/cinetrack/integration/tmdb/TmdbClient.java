package com.alexandre.cinetrack.integration.tmdb;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.alexandre.cinetrack.integration.tmdb.dto.TmdbFilmeResponse;
import com.alexandre.cinetrack.integration.tmdb.dto.TmdbPesquisaResponse;

/*
 * Cliente responsável exclusivamente pela comunicação com o TMDB.
 *
 * @Component permite que o Spring crie e administre esta classe.
 */
@Component
public class TmdbClient {

    private final RestClient restClient;
    private final String imageBaseUrl;

    /*
     * O Spring fornece as configurações definidas no
     * application.properties e constrói o cliente HTTP.
     */
    public TmdbClient(
        RestClient.Builder builder,
        @Value("${tmdb.api.base-url}") String apiBaseUrl,
        @Value("${tmdb.api.token}") String apiToken,
        @Value("${tmdb.image.base-url}") String imageBaseUrl
    ) {
        this.restClient = builder
            .baseUrl(apiBaseUrl)
            .defaultHeader(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + apiToken
            )
            .build();

        this.imageBaseUrl = imageBaseUrl;
    }

    /*
     * Pesquisa um filme pelo título e pelo ano.
     *
     * Optional representa a possibilidade de nenhum filme
     * com capa ser encontrado.
     */
    public Optional<TmdbFilmeResponse> buscarFilme(
        String titulo,
        Integer ano
    ) {
        TmdbPesquisaResponse resposta = restClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/search/movie")
                .queryParam("query", titulo)
                .queryParam("year", ano)
                .queryParam("language", "pt-BR")
                .queryParam("include_adult", false)
                .build()
            )
            .retrieve()
            .body(TmdbPesquisaResponse.class);

        /*
         * Protege a aplicação contra uma resposta vazia.
         */
        if (resposta == null || resposta.results() == null) {
            return Optional.empty();
        }

        /*
         * Seleciona o primeiro resultado que realmente possui capa.
         */
        return resposta.results()
            .stream()
            .filter(filme -> filme.posterPath() != null)
            .filter(filme -> !filme.posterPath().isBlank())
            .findFirst();
    }

    /*
     * Transforma o caminho retornado pela API em uma URL completa.
     */
    public String montarUrlCapa(String posterPath) {
        return imageBaseUrl + posterPath;
    }
}