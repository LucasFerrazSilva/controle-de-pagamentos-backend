package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ArquivoEhPdfValidator implements UploadNotaFiscalValidator {

    @Override
    public void validate(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getContentType() == null)
            throw new ValidationException("file", "O arquivo e sua extensao nao podem ser nulos");


        if (!"application/pdf".equalsIgnoreCase(multipartFile.getContentType()))
            throw new ValidationException("contentType", "O arquivo deve ser um PDF");
    }

}
