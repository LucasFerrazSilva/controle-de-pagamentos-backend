package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations.AtualizarNotasFiscaisValidator;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations.NovasNotasFiscaisValidator;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class NotaFiscalService {
    private final NotaFiscalRepository repository;
    private final UserRepository userRepository;
    private final List<NovasNotasFiscaisValidator> novasNotasFiscaisValidators;
    private final List<AtualizarNotasFiscaisValidator> atualizarNotasFiscaisValidators;

    public NotaFiscalService(NotaFiscalRepository notaFiscalRepository, UserRepository userRepository, List<NovasNotasFiscaisValidator> novasNotasFiscaisValidators, List<AtualizarNotasFiscaisValidator> atualizarNotasFiscaisValidators) {
        this.repository = notaFiscalRepository;
        this.userRepository = userRepository;
        this.novasNotasFiscaisValidators = novasNotasFiscaisValidators;
        this.atualizarNotasFiscaisValidators = atualizarNotasFiscaisValidators;
    }

    @Transactional
    public NotaFiscal create(NovaNotaFiscalDTO dto){
        novasNotasFiscaisValidators.forEach(validator -> validator.validate(dto));

        User user = userRepository.findById(dto.idUser()).orElseThrow();
        NotaFiscal notaFiscal = new NotaFiscal(dto, getLoggedUser(), user);
        repository.save(notaFiscal);

        return notaFiscal;
    }

    public Page<NotaFiscal> list(Pageable pageable, Long idUsuario, Integer mes, Integer ano, BigDecimal valor, NotaFiscalStatus status){
        return repository.findByFiltros(pageable, idUsuario , mes, ano, valor, status);
    }

    public NotaFiscal findById(Long id){
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public NotaFiscal update(Long id, AtualizarNotaFiscalDTO dto){
        atualizarNotasFiscaisValidators.forEach(validator -> validator.validate(id, dto));
        NotaFiscal notaFiscal = repository.findById(id).orElseThrow();
        User user = userRepository.findById(dto.idUser()).orElseThrow();
        notaFiscal.update(dto, getLoggedUser(), user);
        repository.save(notaFiscal);

        return notaFiscal;
    }

    @Transactional
    public void delete(Long id){
        NotaFiscal notaFiscal = repository.findById(id).orElseThrow();
        notaFiscal.deactivate(getLoggedUser());
        repository.save(notaFiscal);
    }
}
