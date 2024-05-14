CREATE TABLE TB_NOTAS_FISCAIS (

	ID_PAGAMENTO BIGINT NOT NULL PRIMARY KEY,
	ID_USUARIO BIGINT NOT NULL,
	MES INT NOT NULL,
	ANO INT NOT NULL,
    VALOR DECIMAL NOT NULL,
    FILE_PATH VARCHAR(100) NOT NULL,
	STATUS VARCHAR(100) NOT NULL,
	CREATE_DATETIME TIMESTAMP NOT NULL DEFAULT NOW(),
	CREATE_USER_ID BIGINT,
	UPDATE_DATETIME TIMESTAMP,
	UPDATE_USER_ID BIGINT,
	CONSTRAINT FK_TB_NOTAS_FISCAIS_ID_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES TB_USERS(ID_USER),
	CONSTRAINT FK_TB_NOTAS_FISCAIS_CREATE_USER_ID FOREIGN KEY (CREATE_USER_ID) REFERENCES TB_USERS(ID_USER),
	CONSTRAINT FK_TB_NOTAS_FISCAIS_UPDATE_USER_ID FOREIGN KEY (UPDATE_USER_ID) REFERENCES TB_USERS(ID_USER)
);
CREATE SEQUENCE TB_NOTAS_FISCAIS_SEQ;