package com.alexandre.cinetrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandre.cinetrack.entity.Genero;

/*
 * JpaRepository fornece automaticamente as operações básicas:
 *
 * save()       -> cadastrar e atualizar
 * findAll()    -> listar
 * findById()   -> buscar pelo ID
 * existsById() -> verificar se existe
 * deleteById() -> excluir
 *
 * Genero é a Entity administrada.
 * Long é o tipo do ID da Entity.
 */
public interface GeneroRepository extends JpaRepository<Genero, Long> {

    /*
     * O Spring interpreta o nome do método e gera uma consulta
     * que lista os gêneros em ordem alfabética.
     */
    List<Genero> findAllByOrderByNomeAsc();

    /*
     * Verifica se já existe um gênero com o mesmo nome,
     * ignorando diferenças entre letras maiúsculas e minúsculas.
     *
     * Exemplo: "Drama" e "drama" serão considerados iguais.
     */
    boolean existsByNomeIgnoreCase(String nome);

    /*
     * Será usado durante a edição.
     *
     * Verifica se outro registro, com ID diferente,
     * já utiliza o nome informado.
     */
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
