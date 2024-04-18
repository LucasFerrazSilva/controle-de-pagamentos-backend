package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ParametroService {

    private final ParametroRepository parametroRepository;

    public ParametroService(ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }

    public Parametro save(NovoParametroDTO novoParametroDTO){
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication();

        Parametro parameter = new Parametro(novoParametroDTO, loggedUser);
        return parametroRepository.save(parameter);

    }


}
