package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

@Service
public class HorasExtrasService {

    private final HorasExtrasRepository repository;
    private final UserRepository userRepository;

    public HorasExtrasService(HorasExtrasRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public HorasExtras create(NovasHorasExtrasDTO dto) {
        User aprovador = userRepository.findById(dto.idAprovador()).orElseThrow();
        HorasExtras horasExtras = new HorasExtras(dto, getLoggedUser(), aprovador);
        repository.save(horasExtras);
        return horasExtras;
    }

    public Page<HorasExtras> list(Pageable pageable, Long idUsuario, Long idAprovador, LocalDate dataInicio,
                                  LocalDate dataFim, String descricao, HorasExtrasStatus status) {

        Page<HorasExtras> page = repository.findByFiltros(pageable, idUsuario, idAprovador, descricao, status);

        if (dataInicio != null) {
            LocalDateTime dataHoraInicio = dataInicio.atTime(LocalTime.MIN);
            List<HorasExtras> list = page.stream().filter(horasExtras -> dataHoraInicio.isBefore(horasExtras.getDataHoraInicio())).toList();
            page = new PageImpl<>(list, pageable, list.size());
        }

        if (dataFim != null) {
            LocalDateTime dataHoraFim = dataFim.atTime(LocalTime.MAX);
            List<HorasExtras> list = page.stream().filter(horasExtras -> dataHoraFim.isAfter(horasExtras.getDataHoraFim())).toList();
            page = new PageImpl<>(list, pageable, list.size());
        }

        return page;
    }

    public HorasExtras findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    // Read - list - findById
    // Update
    // Delete

}
