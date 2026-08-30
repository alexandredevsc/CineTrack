package com.alexandre.cinetrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexandre.cinetrack.entity.Genero;
import com.alexandre.cinetrack.service.GeneroService;

import jakarta.validation.Valid;

/*
 * @Controller informa ao Spring que esta classe recebe
 * requisições do navegador e devolve páginas HTML.
 */
@Controller

/*
 * Todas as rotas deste Controller começam com /generos.
 */
@RequestMapping("/generos")
public class GeneroController {

    private static final String PAGINA_LISTA = "generos/lista";
    private static final String PAGINA_FORMULARIO =
        "generos/formulario";

    private final GeneroService generoService;

    /*
     * Injeção de dependência pelo construtor.
     */
    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    /*
     * GET /generos
     *
     * Lista todos os gêneros cadastrados.
     */
    @GetMapping
    public String listar(Model model) {

        /*
         * Coloca a lista no Model para o Thymeleaf acessar
         * usando ${generos}.
         */
        model.addAttribute(
            "generos",
            generoService.listarTodos()
        );

        return PAGINA_LISTA;
    }

    /*
     * GET /generos/novo
     *
     * Abre o formulário de cadastro.
     */
    @GetMapping("/novo")
    public String abrirFormularioCadastro(Model model) {
        model.addAttribute("genero", new Genero());
        model.addAttribute("tituloPagina", "Novo gênero");

        return PAGINA_FORMULARIO;
    }

    /*
     * POST /generos
     *
     * Recebe e valida o novo gênero.
     */
    @PostMapping
    public String cadastrar(
        @Valid @ModelAttribute("genero") Genero genero,
        BindingResult resultado,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        /*
         * BindingResult precisa vir imediatamente depois
         * do parâmetro anotado com @Valid.
         */
        if (resultado.hasErrors()) {
            model.addAttribute(
                "tituloPagina",
                "Novo gênero"
            );

            return PAGINA_FORMULARIO;
        }

        try {
            generoService.salvar(genero);

            /*
             * Flash attribute existe somente durante
             * o próximo redirecionamento.
             */
            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Gênero cadastrado com sucesso."
            );

            return "redirect:/generos";
        } catch (IllegalStateException exception) {
            /*
             * Adiciona um erro geral ao formulário,
             * como nome de gênero duplicado.
             */
            resultado.reject(
                "genero.invalido",
                exception.getMessage()
            );

            model.addAttribute(
                "tituloPagina",
                "Novo gênero"
            );

            return PAGINA_FORMULARIO;
        }
    }

    /*
     * GET /generos/{id}/editar
     *
     * Exemplo: /generos/1/editar
     */
    @GetMapping("/{id}/editar")
    public String abrirFormularioEdicao(
        @PathVariable Long id,
        Model model
    ) {
        model.addAttribute(
            "genero",
            generoService.buscarPorId(id)
        );

        model.addAttribute(
            "tituloPagina",
            "Editar gênero"
        );

        return PAGINA_FORMULARIO;
    }

    /*
     * POST /generos/{id}
     *
     * Atualiza um gênero existente.
     */
    @PostMapping("/{id}")
    public String atualizar(
        @PathVariable Long id,
        @Valid @ModelAttribute("genero") Genero genero,
        BindingResult resultado,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        /*
         * O ID confiável vem da rota.
         */
        genero.setId(id);

        if (resultado.hasErrors()) {
            model.addAttribute(
                "tituloPagina",
                "Editar gênero"
            );

            return PAGINA_FORMULARIO;
        }

        try {
            generoService.salvar(genero);

            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Gênero atualizado com sucesso."
            );

            return "redirect:/generos";
        } catch (
            IllegalArgumentException |
            IllegalStateException exception
        ) {
            resultado.reject(
                "genero.invalido",
                exception.getMessage()
            );

            model.addAttribute(
                "tituloPagina",
                "Editar gênero"
            );

            return PAGINA_FORMULARIO;
        }
    }

    /*
     * POST /generos/{id}/excluir
     *
     * Exclusão usa POST porque altera os dados.
     * Uma rota GET não deve excluir registros.
     */
    @PostMapping("/{id}/excluir")
    public String excluir(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            generoService.excluir(id);

            redirectAttributes.addFlashAttribute(
                "mensagemSucesso",
                "Gênero excluído com sucesso."
            );
        } catch (
            IllegalArgumentException |
            IllegalStateException exception
        ) {
            redirectAttributes.addFlashAttribute(
                "mensagemErro",
                exception.getMessage()
            );
        }

        return "redirect:/generos";
    }
}