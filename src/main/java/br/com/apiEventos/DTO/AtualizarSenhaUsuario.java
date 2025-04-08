package br.com.apiEventos.DTO;

public class AtualizarSenhaUsuario {
    public String senhaAtual;
    public String novaSenha;

    public AtualizarSenhaUsuario(String senhaAtual, String novaSenha) {
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }
}
