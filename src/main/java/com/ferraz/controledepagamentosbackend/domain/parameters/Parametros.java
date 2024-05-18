package com.ferraz.controledepagamentosbackend.domain.parameters;

public enum Parametros {
    TEMPO_EXPIRACAO_LINK(1l),
    DEVE_ENVIAR_EMAIL_AVALIACAO(2l),
	DEVE_ENVIAR_CREDENCIAIS_DE_ACESSO(6l);

    private Long id;

    Parametros(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

}
