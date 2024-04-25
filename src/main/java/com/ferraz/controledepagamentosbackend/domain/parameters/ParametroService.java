package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.NovoParametroDTO;
import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ParametroService {
    private final ParametroRepository parametroRepository;

    public ParametroService(ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }

    public static final NoSuchElementException noSuchElementException = new NoSuchElementException("Parametro não encontrado");

    public Parametro save(NovoParametroDTO novoParametroDTO){

        Parametro parameter = new Parametro(novoParametroDTO, AuthenticationService.getLoggedUser());
        return parametroRepository.save(parameter);

    }

    public Page<Parametro> findAll(Pageable pageable){
        return parametroRepository.findAllByStatus(pageable, ParametroStatus.ATIVO);
    }
    public Parametro findOne(Long id){

        return parametroRepository.findById(id).orElseThrow(() -> noSuchElementException);
    }

    public Parametro update(Long id, UpdateParametroDTO updateParametroDTO){
        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> noSuchElementException);

        parametro.setUpdateUser(AuthenticationService.getLoggedUser());
        BeanUtils.copyProperties(updateParametroDTO, parametro, "id");

        return parametroRepository.save(parametro);
    }

    public void deactivate(Long id){
        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> noSuchElementException);

        parametro.deactivate(AuthenticationService.getLoggedUser());
        parametroRepository.save(parametro);

    }

}
