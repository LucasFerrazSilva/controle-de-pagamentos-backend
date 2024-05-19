package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import org.springframework.web.multipart.MultipartFile;

public interface UploadNotaFiscalValidator {

    void validate(MultipartFile multipartFile);

}
