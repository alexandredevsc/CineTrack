/*
 * Executa as configurações somente depois que o HTML
 * estiver completamente carregado.
 */
document.addEventListener("DOMContentLoaded", () => {
    configurarConfirmacaoDeExclusao();
    configurarCampoDeNota();
    configurarContadorDeComentario();
});


/*
 * Exibe uma confirmação antes de enviar qualquer
 * formulário cuja rota termine com /excluir.
 *
 * A exclusão continua sendo realizada pelo Java.
 * O JavaScript apenas evita cliques acidentais.
 */
function configurarConfirmacaoDeExclusao() {
    const formularios = document.querySelectorAll(
        'form[action$="/excluir"]'
    );

    formularios.forEach((formulario) => {
        formulario.addEventListener("submit", (evento) => {
            const confirmou = window.confirm(
                "Deseja realmente excluir este registro?"
            );

            if (!confirmou) {
                evento.preventDefault();
            }
        });
    });
}


/*
 * A nota só fica disponível para filmes marcados
 * como ASSISTIDO.
 *
 * Essa regra também existe no FilmeService, pois
 * o servidor continua sendo a autoridade.
 */
function configurarCampoDeNota() {
    const campoStatus = document.querySelector("#status");
    const campoNota = document.querySelector("#nota");
    const textoAjuda = document.querySelector("#nota-ajuda");

    /*
     * Se a página atual não for o formulário de filme,
     * os elementos não existirão e a função será encerrada.
     */
    if (!campoStatus || !campoNota) {
        return;
    }

    function atualizarCampo() {
        const filmeAssistido =
            campoStatus.value === "ASSISTIDO";

        campoNota.disabled = !filmeAssistido;
        campoNota.required = filmeAssistido;

        if (!filmeAssistido) {
            campoNota.value = "";
        }

        if (textoAjuda) {
            textoAjuda.textContent = filmeAssistido
                ? "Informe uma nota entre 0 e 10."
                : "A nota será liberada quando o status for Assistido.";
        }
    }

    campoStatus.addEventListener(
        "change",
        atualizarCampo
    );

    atualizarCampo();
}


/*
 * Cria um contador para o comentário sem precisar
 * adicionar outro elemento manualmente no HTML.
 */
function configurarContadorDeComentario() {
    const campoComentario =
        document.querySelector("#comentario");

    if (!campoComentario) {
        return;
    }

    const limite = campoComentario.maxLength;
    const contador = document.createElement("span");

    contador.className = "character-counter";
    contador.setAttribute("aria-live", "polite");

    /*
     * Insere o contador logo depois do textarea.
     */
    campoComentario.insertAdjacentElement(
        "afterend",
        contador
    );

    function atualizarContador() {
        const quantidade = campoComentario.value.length;

        contador.textContent =
            `${quantidade} / ${limite} caracteres`;
    }

    campoComentario.addEventListener(
        "input",
        atualizarContador
    );

    atualizarContador();
}