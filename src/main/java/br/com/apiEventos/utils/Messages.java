package br.com.apiEventos.utils;

public interface Messages {
    public String MSG_CADASTRO = "Cadastro realizado com sucesso!";
    public String MSG_CADASTRO_ERRO = "Erro ao cadastrar!";
    public String MSG_CADASTRO_VAZIO = "Cadastro vazio!";
    public String MSG_ATUALIZADO = "Cadastro atualizado com sucesso!";
    public String MSG_ATUALIZADO_ERRO = "Erro ao atualizar cadastro!";
    public String MSG_CADASTRO_NAO_ENCONTRADO = "Cadastro não encontrado!";
    public String MSG_CADASTRO_NAO_ENCONTRADO_ID = "Cadastro não encontrado pelo ID!";
    public String MSG_CADASTRO_DELETADO = "Cadastro deletado com sucesso!";
    public String MSG_CADASTRO_DELETADO_ERRO = "Erro ao deletar cadastro!";

    public String mensagemToJSON(String msg);
}
