package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametroService {

    private final ParametroRepository parametroRepository;

    public ParametroService(ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }

    public static final String PARAMETRO_NAO_ENCONTRADO = "Parametro não encontrado";

    public Parametro save(NovoParametroDTO novoParametroDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();

        Parametro parameter = new Parametro(novoParametroDTO, loggedUser);
        return parametroRepository.save(parameter);

    }

    public List<Parametro> findAll(){
        return parametroRepository.findAll();
    }
    public Parametro findOne(Long id){
        return parametroRepository.findById(id).orElseThrow(() -> new NullPointerException(PARAMETRO_NAO_ENCONTRADO) );
    }

    public Parametro updateParametro(Long id, UpdateParametroDTO updateParametroDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> new NullPointerException(PARAMETRO_NAO_ENCONTRADO));

        BeanUtils.copyProperties(updateParametroDTO, parametro, "id");

        parametro.setUpdateUserAndTime(user);
        return parametroRepository.save(parametro);
    }

    public void deactivateParametro(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> new NullPointerException(PARAMETRO_NAO_ENCONTRADO));

        parametro.setStatus(ParametroStatus.INATIVO);
        parametro.setUpdateUserAndTime(user);

        parametroRepository.save(parametro);

    }

}
