package com.alexandre.cinetrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * @Entity informa ao JPA/Hibernate que esta classe
 * representa uma tabela no banco de dados.
 */
@Entity

/*
 * Define explicitamente o nome da tabela.
 * Sem essa anotação, o Hibernate escolheria um nome automaticamente.
 */
@Table(name = "generos")
public class Genero {

    /*
     * @Id identifica a chave primária da tabela.
     */
    @Id

    /*
     * IDENTITY faz o MySQL gerar o ID automaticamente:
     * 1, 2, 3, 4...
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Impede que o nome seja vazio no formulário.
     *
     * Essa é uma validação do servidor e não depende
     * de JavaScript ou do navegador.
     */
    @NotBlank(message = "Informe o nome do gênero.")

    /*
     * Limita o texto a 50 caracteres.
     */
    @Size(
        max = 50,
        message = "O gênero deve ter no máximo 50 caracteres."
    )

    /*
     * nullable = false cria a coluna como obrigatória.
     * unique = true evita dois gêneros com o mesmo nome.
     * length = 50 define o tamanho da coluna no MySQL.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    /*
     * O JPA precisa de um construtor vazio para criar
     * objetos quando recupera registros do banco.
     */
    public Genero() {
    }

    /*
     * Construtor auxiliar para criar um gênero informando o nome.
     */
    public Genero(String nome) {
        this.nome = nome;
    }

    /*
     * Getter: devolve o ID do gênero.
     */
    public Long getId() {
        return id;
    }

    /*
     * Setter: altera o ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * Getter: devolve o nome do gênero.
     */
    public String getNome() {
        return nome;
    }

    /*
     * Setter: altera o nome do gênero.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}