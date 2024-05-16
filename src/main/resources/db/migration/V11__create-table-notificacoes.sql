CREATE TABLE TB_NOTIFICACOES (
	ID_NOTIFICACAO BIGINT NOT NULL PRIMARY KEY,
	ID_USUARIO BIGINT NOT NULL,
	DESCRICAO VARCHAR(100) NOT NULL,
	PATH VARCHAR(100) NOT NULL,
	STATUS VARCHAR(100) NOT NULL,
	CREATE_DATETIME TIMESTAMP NOT NULL DEFAULT NOW(),
	CREATE_USER_ID BIGINT,
	UPDATE_DATETIME TIMESTAMP,
	UPDATE_USER_ID BIGINT,
	CONSTRAINT FK_TB_NOTIFICACOES_ID_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES TB_USERS(ID_USER),
	CONSTRAINT FK_TB_NOTIFICACOES_CREATE_USER_ID FOREIGN KEY (CREATE_USER_ID) REFERENCES TB_USERS(ID_USER),
	CONSTRAINT FK_TB_NOTIFICACOES_UPDATE_USER_ID FOREIGN KEY (UPDATE_USER_ID) REFERENCES TB_USERS(ID_USER)
);
