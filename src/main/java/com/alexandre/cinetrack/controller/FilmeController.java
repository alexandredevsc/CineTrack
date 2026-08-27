package com.alexandre.cinetrack.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexandre.cinetrack.entity.Filme;
import com.alexandre.cinetrack.entity.StatusFilme;
import com.alexandre.cinetrack.service.FilmeService;
import com.alexandre.cinetrack.service.GeneroService;

import jakarta.validation.Valid;

/*
 * Controller responsável pelas rotas de filmes.
 */
@Controller
@RequestMapping("/filmes")
public class FilmeController {

    private static final String PAGINA_LISTA = "filmes/lista";
    private static final String PAGINA_FORMULARIO =
        "filmes/formulario";

    private final FilmeService filmeService;
    private final GeneroService generoService;

    /*
     * Injeção de dependência pelo construtor.
     */
    public FilmeController(
        FilmeService filmeService,
        GeneroService generoService
    ) {
        this.filmeService = filmeService;
        this.generoService = generoService;
    }

    /*
     * GET /filmes
     *
     * Exibe todos os filmes.
     */
    @GetMapping
    public String listar(Model model) {
        prepararListagem(
            model,
            filmeService.listarTodos()
        );

        return PAGINA_LISTA;
    }

    /*
     * GET /filmes/filtrar/genero?generoId=1
     */
    @GetMapping("/filtrar/genero")
    public String filtrarPorGenero(
        @RequestParam Long generoId,
        Model model
    ) {
        try {
            prepararListagem(
                model,
                filmeService.listarPorGenero(generoId)
            );

            model.addAttribute(
                "generoSelecionado",
                generoId
            );
        } catch (IllegalArgumentException exception) {
            prepararListagem(
                model,
                filmeService.listarTodos()
            );

            model.addAttribute(
                "mensagemErro",
                exception.getMessage()
            );
        }

        return PAGINA_LISTA;
    }

    /*
     * GET /filmes/filtrar/status?status=ASSISTIDO
     */
    @GetMapping("/filtrar/status")
    public String filtrarPorStatus(
        @RequestParam StatusFilme status,
        Model model
    ) {
        try {
            prepararListagem(
                model,
                filmeService.listarPorStatus(status)
            );

            model.addAttribute(
                "statusSelecionado",
                status
            );
        } catch (IllegalArgumentException exception) {
            prepararListagem(
                model,
                filmeService.listarTodos()
            );

            model.addAttribute(
                "mensagemErro",
                exception.getMessage()
            );
        }

        return PAGINA_LISTA;
    }

    /*
     * GET /filmes/filtrar/nota?notaMinima=8
     */
    @GetMapping("/filtrar/nota")
    public String filtrarPorNota(
        @RequestParam BigDecimal notaMinima,
        Model model
    ) {
        try {
            prepararListagem(
                model,
                filmeService.listarPorNotaMinima(notaMinima)
            );

            model.addAttribute(
                "notaMinimaSelecionada",
                notaMinima
            );
        } catch (IllegalArgumentException exception) {
            prepararListagem(
                model,
                filmeService.listarTodos()
            );

            model.addAttribute(
                "mensagemErro",
                exception.getMessage()
            );
        }

        return PAGINA_LISTA;
    }

    /*
     * GET /filmes/novo
     *
     * Abre o formulário para cadastrar um filme.
     */
    @GetMapping("/novo")
    public String abrirFormularioCadastro(Model model) {
        prepararFormulario(
            model,
            new Filme(),
            "Novo filme"
        );

        return PAGINA_FORMULARIO;
    }

    /*
     * POST /filmes
     *
     * Cadastra um novo filme.
     */
    @PostMapping
    public String cadastrar(
        @Valid @ModelAttribute("filme") Filme filme,
        BindingResult resultado,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (resultado.hasErrors()) {
            prepararFormulario(
                model,
                filme,
                "Novo filme"
            );

            return PAGINA_FORMULARIO;
        }

        try {
            filmeService.salvar(filme);

            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Filme cadastrado com sucesso."
            );

            return "redirect:/filmes";
        } catch (
            IllegalArgumentException |
            IllegalStateException exception
        ) {
            resultado.reject(
                "filme.invalido",
                exception.getMessage()
            );

            prepararFormulario(
                model,
                filme,
                "Novo filme"
            );

            return PAGINA_FORMULARIO;
        }
    }

    /*
     * GET /filmes/{id}/editar
     */
    @GetMapping("/{id}/editar")
    public String abrirFormularioEdicao(
        @PathVariable Long id,
        Model model
    ) {
        Filme filme = filmeService.buscarPorId(id);

        prepararFormulario(
            model,
            filme,
            "Editar filme"
        );

        return PAGINA_FORMULARIO;
    }

    /*
     * POST /filmes/{id}
     *
     * Atualiza um filme existente.
     */
    @PostMapping("/{id}")
    public String atualizar(
        @PathVariable Long id,
        @Valid @ModelAttribute("filme") Filme filme,
        BindingResult resultado,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        /*
         * O ID da rota é considerado a fonte confiável.
         */
        filme.setId(id);

        if (resultado.hasErrors()) {
            prepararFormulario(
                model,
                filme,
                "Editar filme"
            );

            return PAGINA_FORMULARIO;
        }

        try {
            filmeService.salvar(filme);

            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Filme atualizado com sucesso."
            );

            return "redirect:/filmes";
        } catch (
            IllegalArgumentException |
            IllegalStateException exception
        ) {
            resultado.reject(
                "filme.invalido",
                exception.getMessage()
            );

            prepararFormulario(
                model,
                filme,
                "Editar filme"
            );

            return PAGINA_FORMULARIO;
        }
    }

    /*
     * POST /filmes/{id}/excluir
     *
     * Exclui um filme.
     */
    @PostMapping("/{id}/excluir")
    public String excluir(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            filmeService.excluir(id);

            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Filme excluído com sucesso."
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                "mensagemErro",
                exception.getMessage()
            );
        }

        return "redirect:/filmes";
    }

    /*
     * Prepara os dados compartilhados pela página de listagem.
     */
    private void prepararListagem(
        Model model,
        List<Filme> filmes
    ) {
        model.addAttribute("filmes", filmes);

        model.addAttribute(
            "generos",
            generoService.listarTodos()
        );

        model.addAttribute(
            "statusDisponiveis",
            StatusFilme.values()
        );
    }

    /*
     * Prepara os dados compartilhados pelo formulário.
     *
     * Os gêneros são necessários para montar o campo select.
     * Os status são usados para as opções do filme.
     */
    private void prepararFormulario(
        Model model,
        Filme filme,
        String tituloPagina
    ) {
        model.addAttribute("filme", filme);

        model.addAttribute(
            "generos",
            generoService.listarTodos()
        );

        model.addAttribute(
            "statusDisponiveis",
            StatusFilme.values()
        );

        model.addAttribute(
            "tituloPagina",
            tituloPagina
        );
    }
}