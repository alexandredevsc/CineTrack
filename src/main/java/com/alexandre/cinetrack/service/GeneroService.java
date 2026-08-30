package com.alexandre.cinetrack.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexandre.cinetrack.entity.Genero;
import com.alexandre.cinetrack.repository.FilmeRepository;
import com.alexandre.cinetrack.repository.GeneroRepository;

/*
 * @Service informa ao Spring que esta classe contém
 * regras de negócio da aplicação.
 */
@Service

/*
 * Por padrão, os métodos serão usados apenas para leitura.
 * Isso ajuda o JPA a trabalhar de maneira mais eficiente.
 */
@Transactional(readOnly = true)
public class GeneroService {

    /*
     * As dependências são finais porque não devem ser
     * substituídas depois que o objeto for criado.
     */
    private final GeneroRepository generoRepository;
    private final FilmeRepository filmeRepository;

    /*
     * Injeção de dependência pelo construtor.
     *
     * O Spring encontra as implementações dos Repositories
     * e as fornece automaticamente.
     */
    public GeneroService(
        GeneroRepository generoRepository,
        FilmeRepository filmeRepository
    ) {
        this.generoRepository = generoRepository;
        this.filmeRepository = filmeRepository;
    }

    /*
     * Lista todos os gêneros em ordem alfabética.
     */
    public List<Genero> listarTodos() {
        return generoRepository.findAllByOrderByNomeAsc();
    }

    /*
     * Busca um gênero pelo ID.
     *
     * findById devolve Optional porque o registro pode não existir.
     * orElseThrow interrompe a operação com uma mensagem clara
     * caso o ID não seja encontrado.
     */
    public Genero buscarPorId(Long id) {
        return generoRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException(
                    "Gênero não encontrado."
                )
            );
    }

    /*
     * @Transactional sem readOnly permite alterar o banco.
     */
    @Transactional
    public Genero salvar(Genero genero) {

        /*
         * Remove espaços extras no começo e no final.
         *
         * Exemplo:
         * "  Ação  " passa a ser "Ação".
         */
        String nomeNormalizado = genero.getNome().trim();
        genero.setNome(nomeNormalizado);

        /*
         * Durante o cadastro, o ID é nulo.
         */
        boolean novoGenero = genero.getId() == null;

        if (novoGenero) {
            validarNomeNovo(nomeNormalizado);
        } else {
            validarNomeEditado(
                nomeNormalizado,
                genero.getId()
            );
        }

        /*
         * save cadastra quando o ID é nulo e atualiza
         * quando o objeto já possui um ID.
         */
        return generoRepository.save(genero);
    }

    /*
     * Não permite excluir um gênero que esteja associado
     * a algum filme.
     */
    @Transactional
    public void excluir(Long id) {

        /*
         * Primeiro confirma se o gênero realmente existe.
         */
        buscarPorId(id);

        if (filmeRepository.existsByGeneroId(id)) {
            throw new IllegalStateException(
                "Não é possível excluir um gênero que possui filmes."
            );
        }

        generoRepository.deleteById(id);
    }

    /*
     * Validação utilizada durante um novo cadastro.
     */
    private void validarNomeNovo(String nome) {
        if (generoRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalStateException(
                "Já existe um gênero com esse nome."
            );
        }
    }

    /*
     * Durante a edição, ignoramos o próprio registro.
     *
     * Exemplo: editar o gênero de ID 1 e manter o nome
     * "Drama" não deve ser considerado duplicado.
     */
    private void validarNomeEditado(String nome, Long id) {
        boolean outroGeneroPossuiNome =
            generoRepository.existsByNomeIgnoreCaseAndIdNot(
                nome,
                id
            );

        if (outroGeneroPossuiNome) {
            throw new IllegalStateException(
                "Já existe outro gênero com esse nome."
            );
        }
    }
}
