package com.alexandre.cinetrack.entity;

/*
 * Enum representa uma lista fechada de opções.
 *
 * Usamos enum porque um filme só pode ter um dos
 * status definidos pelo sistema.
 */
public enum StatusFilme {

    /*
     * Valores que serão armazenados no banco:
     * QUERO_ASSISTIR
     * ASSISTIDO
     */
    QUERO_ASSISTIR("Quero assistir"),
    ASSISTIDO("Assistido");

    /*
     * Texto amigável que aparecerá nas páginas HTML.
     */
    private final String descricao;

    /*
     * O construtor associa cada constante à sua descrição.
     */
    StatusFilme(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
