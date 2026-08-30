package com.alexandre.cinetrack.integration.tmdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alexandre.cinetrack.entity.Filme;
import com.alexandre.cinetrack.repository.FilmeRepository;

/*
 * Serviço responsável por atualizar filmes que foram
 * cadastrados sem uma capa.
 *
 * Esta classe coordena o banco de dados e a integração,
 * sem conhecer detalhes da requisição HTTP.
 */
@Service
public class AtualizacaoCapasService {

    /*
     * Logger utilizado para registrar o resultado da atualização
     * sem mostrar mensagens técnicas ao usuário da aplicação.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtualizacaoCapasService.class);

    private final FilmeRepository filmeRepository;
    private final CapaFilmeService capaFilmeService;

    /*
     * O Spring fornece as dependências pelo construtor.
     */
    public AtualizacaoCapasService(
            FilmeRepository filmeRepository,
            CapaFilmeService capaFilmeService) {
        this.filmeRepository = filmeRepository;
        this.capaFilmeService = capaFilmeService;
    }

    /*
     * Pesquisa e atualiza todos os filmes que ainda estão sem capa.
     *
     * O retorno é a quantidade de filmes realmente atualizados.
     */
    public int atualizarCapasPendentes() {
        var filmesSemCapa = filmeRepository.findFilmesSemCapa();

        int quantidadeAtualizada = 0;

        /*
         * Tenta preencher cada filme individualmente.
         *
         * Se uma consulta falhar, CapaFilmeService trata a falha
         * e o processamento pode continuar com os demais filmes.
         */
        for (Filme filme : filmesSemCapa) {
            boolean capaEncontrada = capaFilmeService.preencherCapa(filme);

            if (capaEncontrada) {
                filmeRepository.save(filme);
                quantidadeAtualizada++;
            }
        }

        /*
         * Só registra a mensagem quando alguma capa foi atualizada,
         * evitando poluir o terminal em verificações sem novidades.
         */
        if (quantidadeAtualizada > 0) {
            LOGGER.info(
                    "{} capa(s) atualizada(s) automaticamente.",
                    quantidadeAtualizada);
        }

        return quantidadeAtualizada;
    }
}