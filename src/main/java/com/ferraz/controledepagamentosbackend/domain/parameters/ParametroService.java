package com.ferraz.controledepagamentosbackend.domain.parameters;

import com.ferraz.controledepagamentosbackend.domain.parameters.dto.UpdateParametroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class ParametroService {
    private final ParametroRepository parametroRepository;

    public ParametroService(ParametroRepository parametroRepository){
        this.parametroRepository = parametroRepository;
    }

    public static final NoSuchElementException noSuchElementException = new NoSuchElementException("Parametro não encontrado");


    public Page<Parametro> findAll(Pageable pageable, String nome, String valor, ParametroStatus status){
        return parametroRepository.findByFiltros(pageable, nome, valor, status);
    }
    public Parametro findOne(Long id){
        return parametroRepository.findById(id).orElseThrow(() -> noSuchElementException);
    }

    public Parametro update(Long id, UpdateParametroDTO updateParametroDTO){
        Parametro parametro = parametroRepository.findById(id)
                .orElseThrow(() -> noSuchElementException);

        parametro.update(updateParametroDTO, getLoggedUser());
        return parametroRepository.save(parametro);
    }

}
