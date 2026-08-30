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
    private static final Logger LOGGER = LoggerFactory.getLogger(CapaFilmeService.class);

    private final TmdbClient tmdbClient;

    /*
     * Injeção de dependência pelo construtor.
     * O Spring fornece automaticamente o TmdbClient.
     */
    public CapaFilmeService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /*
     * Pesquisa e preenche a capa quando o filme ainda não possui uma.
     *
     * O retorno informa se uma capa foi encontrada:
     * true = os dados do TMDB foram preenchidos;
     * false = nenhuma alteração foi realizada.
     */
    public boolean preencherCapa(Filme filme) {
        /*
         * Evita uma nova chamada ao TMDB quando o filme
         * já possui uma capa válida.
         */
        if (filme.getCapaUrl() != null
                && !filme.getCapaUrl().isBlank()) {
            return false;
        }

        try {
            /*
             * Pesquisa o filme usando o título e o ano cadastrados.
             */
            var resultadoEncontrado = tmdbClient.buscarFilme(
                    filme.getTitulo(),
                    filme.getAno());

            /*
             * Se a pesquisa não encontrou um resultado com capa,
             * não há dados para atualizar.
             */
            if (resultadoEncontrado.isEmpty()) {
                return false;
            }

            var resultado = resultadoEncontrado.get();

            /*
             * Guarda o identificador do TMDB e a URL completa da capa
             * dentro da Entity Filme.
             */
            filme.setTmdbId(resultado.id());
            filme.setCapaUrl(
                    tmdbClient.montarUrlCapa(resultado.posterPath()));

            return true;
        } catch (RestClientException exception) {
            /*
             * Problemas de internet, autenticação ou indisponibilidade
             * do TMDB não interrompem o cadastro do filme.
             */
            LOGGER.warn(
                    "Não foi possível buscar a capa de '{}': {}",
                    filme.getTitulo(),
                    exception.getMessage());

            return false;
        }
    }
}