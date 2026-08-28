package com.alexandre.cinetrack.integration.tmdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.alexandre.cinetrack.entity.Filme;

/*
 * Serviço responsável por preencher os dados de capa de um filme.
 *
 * Ele separa a regra de integração do restante do cadastro.
 */
@Service
public class CapaFilmeService {

    /*
     * Logger registra problemas técnicos no terminal sem interromper
     * o funcionamento normal da aplicação.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(CapaFilmeService.class);

    private final TmdbClient tmdbClient;

    /*
     * Injeção de dependência pelo construtor.
     * O Spring fornece automaticamente o TmdbClient.
     */
    public CapaFilmeService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /*
     * Pesquisa e preenche a capa apenas em filmes novos.
     */
    public void preencherCapa(Filme filme) {
        /*
         * Um ID existente indica que o filme já foi salvo.
         * Assim, evitamos pesquisar novamente durante uma edição.
         */
        if (filme.getId() != null) {
            return;
        }

        try {
            tmdbClient
                .buscarFilme(filme.getTitulo(), filme.getAno())
                .ifPresent(resultado -> {
                    filme.setTmdbId(resultado.id());
                    filme.setCapaUrl(
                        tmdbClient.montarUrlCapa(resultado.posterPath())
                    );
                });
        } catch (RestClientException exception) {
            /*
             * Uma falha externa não deve impedir o cadastro.
             * Registramos somente uma mensagem técnica no terminal.
             */
            LOGGER.warn(
                "Não foi possível buscar a capa de '{}': {}",
                filme.getTitulo(),
                exception.getMessage()
            );
        }
    }
}