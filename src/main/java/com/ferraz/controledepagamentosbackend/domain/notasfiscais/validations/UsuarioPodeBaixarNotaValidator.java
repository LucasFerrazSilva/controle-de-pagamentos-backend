package com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscal;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.NotaFiscalRepository;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Component
public class UsuarioPodeBaixarNotaValidator implements DownloadNotaFiscalValidator {

    private final NotaFiscalRepository repository;

    public UsuarioPodeBaixarNotaValidator(NotaFiscalRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Long id) {
        NotaFiscal notaFiscal = repository.findById(id).orElseThrow();

        User loggedUser = getLoggedUser();
        if (loggedUser.getPerfil().equals(UsuarioPerfil.ROLE_USER) && !loggedUser.equals(notaFiscal.getUser()))
            throw new ValidationException("user", "Você não pode baixar essa nota fiscal");
    }
}
