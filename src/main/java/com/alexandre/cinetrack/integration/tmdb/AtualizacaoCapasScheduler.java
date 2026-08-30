package com.alexandre.cinetrack.integration.tmdb;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
 * Agenda a verificação periódica das capas pendentes.
 *
 * Esta classe decide somente quando executar.
 * A regra de atualização permanece em AtualizacaoCapasService.
 */
@Component
public class AtualizacaoCapasScheduler {

    private final AtualizacaoCapasService atualizacaoCapasService;

    /*
     * O Spring fornece o serviço responsável pela atualização.
     */
    public AtualizacaoCapasScheduler(
        AtualizacaoCapasService atualizacaoCapasService
    ) {
        this.atualizacaoCapasService = atualizacaoCapasService;
    }

    /*
     * Executa a primeira verificação 30 segundos após a aplicação iniciar.
     *
     * Depois de cada execução, aguarda 5 minutos antes de tentar novamente.
     * Os valores podem ser alterados no application.properties.
     */
    @Scheduled(
        initialDelayString =
            "${tmdb.covers.initial-delay-ms:30000}",
        fixedDelayString =
            "${tmdb.covers.refresh-delay-ms:300000}"
    )
    public void atualizarCapasPendentes() {
        atualizacaoCapasService.atualizarCapasPendentes();
    }
}