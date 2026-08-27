package com.alexandre.cinetrack.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexandre.cinetrack.entity.Filme;
import com.alexandre.cinetrack.entity.Genero;
import com.alexandre.cinetrack.entity.StatusFilme;
import com.alexandre.cinetrack.repository.FilmeRepository;
import com.alexandre.cinetrack.repository.GeneroRepository;

/*
 * Service responsável pelas regras de negócio dos filmes.
 */
@Service
@Transactional(readOnly = true)
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final GeneroRepository generoRepository;

    /*
     * Injeção de dependência pelo construtor.
     *
     * Essa abordagem facilita testes e impede que as
     * dependências sejam alteradas depois da criação do Service.
     */
    public FilmeService(
        FilmeRepository filmeRepository,
        GeneroRepository generoRepository
    ) {
        this.filmeRepository = filmeRepository;
        this.generoRepository = generoRepository;
    }

    /*
     * Lista todos os filmes em ordem alfabética.
     */
    public List<Filme> listarTodos() {
        return filmeRepository.findAllByOrderByTituloAsc();
    }

    /*
     * Busca um filme pelo ID.
     */
    public Filme buscarPorId(Long id) {
        return filmeRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "Filme não encontrado."
                )
            );
    }

    /*
     * Filtra os filmes pelo gênero.
     */
    public List<Filme> listarPorGenero(Long generoId) {

        /*
         * Confirma que o gênero informado existe.
         */
        buscarGeneroPorId(generoId);

        return filmeRepository
            .findByGeneroIdOrderByTituloAsc(generoId);
    }

    /*
     * Filtra por QUERO_ASSISTIR ou ASSISTIDO.
     */
    public List<Filme> listarPorStatus(StatusFilme status) {
        if (status == null) {
            throw new IllegalArgumentException(
                "Informe um status válido."
            );
        }

        return filmeRepository
            .findByStatusOrderByTituloAsc(status);
    }

    /*
     * Filtra por nota mínima.
     *
     * Exemplo: nota mínima 8 retorna filmes com
     * notas 8, 8.5, 9 e 10.
     */
    public List<Filme> listarPorNotaMinima(BigDecimal notaMinima) {
        validarNotaMinima(notaMinima);

        return filmeRepository
            .findByNotaGreaterThanEqualOrderByNotaDesc(
                notaMinima
            );
    }

    /*
     * Cadastra ou atualiza um filme.
     */
    @Transactional
    public Filme salvar(Filme filme) {

        /*
         * Confirma que o objeto recebido não é nulo.
         */
        if (filme == null) {
            throw new IllegalArgumentException(
                "Os dados do filme são obrigatórios."
            );
        }

        /*
         * Em uma edição, confirma que o filme existe.
         */
        if (filme.getId() != null) {
            buscarPorId(filme.getId());
        }

        /*
         * Normaliza os textos para evitar espaços extras.
         */
        normalizarTextos(filme);

        /*
         * Aplica as regras relacionadas ao status e à nota.
         */
        validarStatusENota(filme);

        /*
         * Confirma que o gênero selecionado existe no banco.
         */
        if (filme.getGenero() == null
            || filme.getGenero().getId() == null) {

            throw new IllegalArgumentException(
                "Selecione um gênero válido."
            );
        }

        Genero generoEncontrado =
            buscarGeneroPorId(filme.getGenero().getId());

        /*
         * Substitui o objeto recebido pela Entity recuperada
         * do banco, garantindo um relacionamento válido.
         */
        filme.setGenero(generoEncontrado);

        return filmeRepository.save(filme);
    }

    /*
     * Exclui um filme depois de confirmar sua existência.
     */
    @Transactional
    public void excluir(Long id) {
        buscarPorId(id);
        filmeRepository.deleteById(id);
    }

    /*
     * Busca interna de gênero.
     */
    private Genero buscarGeneroPorId(Long id) {
        return generoRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "Gênero não encontrado."
                )
            );
    }

    /*
     * Remove espaços desnecessários dos campos de texto.
     */
    private void normalizarTextos(Filme filme) {
        if (filme.getTitulo() != null) {
            filme.setTitulo(filme.getTitulo().trim());
        }

        if (filme.getComentario() != null) {
            String comentarioNormalizado =
                filme.getComentario().trim();

            /*
             * Salva null em vez de uma String vazia.
             */
            filme.setComentario(
                comentarioNormalizado.isEmpty()
                    ? null
                    : comentarioNormalizado
            );
        }
    }

    /*
     * Regra de negócio:
     *
     * Um filme marcado como ASSISTIDO deve possuir uma nota.
     * Um filme que ainda será assistido não deve possuir nota.
     */
    private void validarStatusENota(Filme filme) {
        if (filme.getStatus() == null) {
            throw new IllegalArgumentException(
                "Selecione o status do filme."
            );
        }

        if (
            filme.getStatus() == StatusFilme.ASSISTIDO
            && filme.getNota() == null
        ) {
            throw new IllegalStateException(
                "Informe uma nota para o filme assistido."
            );
        }

        if (filme.getStatus() == StatusFilme.QUERO_ASSISTIR) {
            filme.setNota(null);
        }
    }

    /*
     * A nota usada no filtro precisa estar entre 0 e 10.
     */
    private void validarNotaMinima(BigDecimal notaMinima) {
        if (notaMinima == null) {
            throw new IllegalArgumentException(
                "Informe a nota mínima."
            );
        }

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal dez = BigDecimal.TEN;

        boolean menorQueZero =
            notaMinima.compareTo(zero) < 0;

        boolean maiorQueDez =
            notaMinima.compareTo(dez) > 0;

        if (menorQueZero || maiorQueDez) {
            throw new IllegalArgumentException(
                "A nota mínima deve estar entre 0 e 10."
            );
        }
    }
}
