package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.dropbox.core.DbxException;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.AtualizarNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.dto.NovaNotaFiscalDTO;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations.AtualizarNotasFiscaisValidator;
import com.ferraz.controledepagamentosbackend.domain.notasfiscais.validations.NovasNotasFiscaisValidator;
import com.ferraz.controledepagamentosbackend.domain.notificacao.NotificacaoService;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class NotaFiscalService {
    private final NotaFiscalRepository repository;
    private final List<NovasNotasFiscaisValidator> novasNotasFiscaisValidators;
    private final List<AtualizarNotasFiscaisValidator> atualizarNotasFiscaisValidators;
    private final NotificacaoService notificacaoService;
    private final UserRepository userRepository;
    private final UploadNotaFiscalService uploadNotaFiscalService;

    public NotaFiscalService(NotaFiscalRepository notaFiscalRepository, List<NovasNotasFiscaisValidator> novasNotasFiscaisValidators, List<AtualizarNotasFiscaisValidator> atualizarNotasFiscaisValidators, NotificacaoService notificacaoService, UserRepository userRepository, UploadNotaFiscalService uploadNotaFiscalService) {
        this.repository = notaFiscalRepository;
        this.novasNotasFiscaisValidators = novasNotasFiscaisValidators;
        this.atualizarNotasFiscaisValidators = atualizarNotasFiscaisValidators;
        this.notificacaoService = notificacaoService;
        this.userRepository = userRepository;
        this.uploadNotaFiscalService = uploadNotaFiscalService;
    }

    @Transactional
    public NotaFiscal create(NovaNotaFiscalDTO dto){
        novasNotasFiscaisValidators.forEach(validator -> validator.validate(dto));
        User user = userRepository.findById(dto.idUser()).orElseThrow();
        NotaFiscal notaFiscal = new NotaFiscal(dto, getLoggedUser(), user);
        repository.save(notaFiscal);
        notificacaoService.create(notaFiscal.getUser(), "A emissão de uma nota fiscal foi solicitada para voce", "/notas-fiscais");

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
        notaFiscal.update(dto, getLoggedUser());
        repository.save(notaFiscal);

        return notaFiscal;
    }

    @Transactional
    public void delete(Long id){
        NotaFiscal notaFiscal = repository.findById(id).orElseThrow();
        notaFiscal.deactivate(getLoggedUser());
        repository.save(notaFiscal);
    }

    @Transactional
    public void upload(Long id, MultipartFile multipartFile) throws IOException, DbxException {
        NotaFiscal notaFiscal = repository.findById(id).orElseThrow();
        String filePath = uploadNotaFiscalService.upload(multipartFile, notaFiscal.getUser());
        notaFiscal.marcarComoEnviada(filePath, getLoggedUser());
        repository.save(notaFiscal);
    }
}
