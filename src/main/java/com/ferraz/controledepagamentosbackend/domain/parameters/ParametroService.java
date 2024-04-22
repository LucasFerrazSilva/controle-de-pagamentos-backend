package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.ParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public List<ParametroDTO> findAll(){
        return parametroRepository.findAll().stream().map(ParametroDTO::new).toList();
    }
    public ParametroDTO findOne(Long id){
        Parametro parametro = parametroRepository.findById(id).orElseThrow(() -> new NoSuchElementException(PARAMETRO_NAO_ENCONTRADO));

        return new ParametroDTO(parametro.getId(),parametro.getNome(), parametro.getValor());
    }

    public Parametro update(Long id, UpdateParametroDTO updateParametroDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(PARAMETRO_NAO_ENCONTRADO));

        parametro.setUpdateUser(user);
        BeanUtils.copyProperties(updateParametroDTO, parametro, "id");

        return parametroRepository.save(parametro);
    }

    public void deactivate(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(PARAMETRO_NAO_ENCONTRADO));

        parametro.deactivate(user);
        parametroRepository.save(parametro);

    }

}
