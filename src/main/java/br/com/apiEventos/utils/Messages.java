package br.com.apiEventos.utils;

public interface Messages {
    String MSG_CADASTRO = "Cadastro realizado com sucesso!";
    String MSG_CADASTRO_ERRO = "Erro ao cadastrar!";
    String MSG_CADASTRO_JA_REALIZADO = "Cadastro já realizado!";
    String MSG_CADASTRO_VAZIO = "Cadastro vazio!";
    String MSG_ATUALIZADO = "Cadastro atualizado com sucesso!";
    String MSG_ATUALIZADO_ERRO = "Erro ao atualizar cadastro!";
    String MSG_LISTA_ITENS_VAZIA = "Lista de itens vazia!";
    String MSG_CADASTRO_NAO_ENCONTRADO = "Cadastro não encontrado!";
    String MSG_CADASTRO_NAO_ENCONTRADO_ID = "Cadastro não encontrado pelo ID!";
    String MSG_CADASTRO_DELETADO = "Cadastro deletado com sucesso!";
    String MSG_CADASTRO_DELETADO_ERRO = "Erro ao deletar cadastro!";
    String MSG_CADASTRO_DUPLICADO = "Cadastro já existe!";
    String MSG_SEM_EVENTOS = "Não há eventos disponíveis no momento!";
    String MSG_EVENTO_ADICIONADO_FAVORITOS = "Evento adicionado aos favoritos com sucesso!";
    String MSG_SERVICO_INDISPONIVEL_RATE_LIMITADA = "Serviço indisponível no momento. Você atingiu o limite de requisições permitidas. Tente novamente mais tarde.";

    String mensagemToJSON(String msg);

}
