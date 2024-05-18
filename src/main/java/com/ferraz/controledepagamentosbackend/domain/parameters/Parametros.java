package com.ferraz.controledepagamentosbackend.domain.parameters;

public enum Parametros {
    TEMPO_EXPIRACAO_LINK(1l),
    DEVE_ENVIAR_EMAIL_AVALIACAO(2l),
	DEVE_ENVIAR_CREDENCIAIS_DE_ACESSO(6l),
    DIA_HORA_FECHAMENTO(3l),
    HORAS_MES(4l),
    DEVE_ENVIAR_EMAIL_NOTA_FISCAL(5l);


    private Long id;

    Parametros(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

}
