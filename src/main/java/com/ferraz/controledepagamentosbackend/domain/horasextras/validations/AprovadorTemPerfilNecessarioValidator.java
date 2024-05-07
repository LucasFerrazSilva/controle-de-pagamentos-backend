package com.ferraz.controledepagamentosbackend.domain.horasextras.validations;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;
import com.ferraz.controledepagamentosbackend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AprovadorTemPerfilNecessarioValidator implements NovasHorasExtrasValidator, AtualizarHorasExtrasValidator {

    private static final List<UsuarioPerfil> PERFIS_VALIDOS = List.of(UsuarioPerfil.ROLE_GESTOR, UsuarioPerfil.ROLE_ADMIN);

    private UserRepository repository;

    public AprovadorTemPerfilNecessarioValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(NovasHorasExtrasDTO dto) {
        validate(dto.idAprovador());
    }

    @Override
    public void validate(Long id, AtualizarHorasExtrasDTO dto) {
        validate(dto.idAprovador());
    }

    private void validate(Long idAprovador) {
        User aprovador = repository.findById(idAprovador).orElseThrow();

        if (!PERFIS_VALIDOS.contains(aprovador.getPerfil()))
            throw new ValidationException("aprovador", "O aprovador não tem perfil necessário para aprovar horas extras");
    }

}
