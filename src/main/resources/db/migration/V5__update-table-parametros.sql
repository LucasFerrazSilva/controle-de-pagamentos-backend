UPDATE TB_PARAMETERS SET STATUS = 'ATIVO' WHERE STATUS = '1';
UPDATE TB_PARAMETERS SET STATUS = 'INATIVO' WHERE STATUS = '2';
COMMIT;