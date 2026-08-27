package com.alexandre.cinetrack.entity;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * Indica que Filme é uma entidade persistida pelo JPA.
 */
@Entity

/*
 * Define o nome da tabela no banco.
 */
@Table(name = "filmes")
public class Filme {

    /*
     * Chave primária gerada automaticamente pelo MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * O título é obrigatório e pode ter no máximo 120 caracteres.
     */
    @NotBlank(message = "Informe o título do filme.")
    @Size(
        max = 120,
        message = "O título deve ter no máximo 120 caracteres."
    )
    @Column(nullable = false, length = 120)
    private String titulo;

    /*
     * O primeiro filme conhecido foi produzido em 1888.
     * O limite 2100 impede anos claramente inválidos.
     */
    @NotNull(message = "Informe o ano do filme.")
    @Min(value = 1888, message = "O ano deve ser igual ou posterior a 1888.")
    @Max(value = 2100, message = "Informe um ano válido.")
    @Column(nullable = false)
    private Integer ano;

    /*
     * A nota não é obrigatória porque um filme que o usuário
     * ainda deseja assistir pode não possuir avaliação.
     *
     * BigDecimal é usado para manter precisão decimal.
     */
    @DecimalMin(
        value = "0.0",
        message = "A nota mínima é 0."
    )
    @DecimalMax(
        value = "10.0",
        message = "A nota máxima é 10."
    )
    @NumberFormat(pattern = "#0.0")
    @Column(precision = 3, scale = 1)
    private BigDecimal nota;

    /*
     * Comentário opcional com até 1000 caracteres.
     */
    @Size(
        max = 1000,
        message = "O comentário deve ter no máximo 1000 caracteres."
    )
    @Column(length = 1000)
    private String comentario;

    /*
     * EnumType.STRING armazena no banco os textos
     * QUERO_ASSISTIR ou ASSISTIDO.
     *
     * É mais seguro do que armazenar a posição numérica do enum.
     */
    @NotNull(message = "Selecione o status do filme.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusFilme status = StatusFilme.QUERO_ASSISTIR;

    /*
     * Muitos filmes podem pertencer ao mesmo gênero.
     *
     * A coluna genero_id será uma chave estrangeira
     * apontando para a tabela generos.
     */
    @NotNull(message = "Selecione o gênero do filme.")
    @ManyToOne
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero;

    /*
     * Construtor vazio exigido pelo JPA.
     */
    public Filme() {
    }

    /*
     * Construtor auxiliar.
     * Não recebemos o ID porque ele será gerado pelo MySQL.
     */
    public Filme(
        String titulo,
        Integer ano,
        BigDecimal nota,
        String comentario,
        StatusFilme status,
        Genero genero
    ) {
        this.titulo = titulo;
        this.ano = ano;
        this.nota = nota;
        this.comentario = comentario;
        this.status = status;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public StatusFilme getStatus() {
        return status;
    }

    public void setStatus(StatusFilme status) {
        this.status = status;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}