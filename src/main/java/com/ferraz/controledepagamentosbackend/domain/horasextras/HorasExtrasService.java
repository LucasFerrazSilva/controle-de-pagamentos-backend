package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    // Read - list - findById
    // Update
    // Delete

}
