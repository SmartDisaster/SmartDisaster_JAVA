package br.com.fiap.smartdisaster.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com id " + id + " não encontrado(a).");
    }
}
