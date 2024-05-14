package com.ferraz.controledepagamentosbackend.domain.horasextras;

import static com.ferraz.controledepagamentosbackend.infra.security.AuthenticationService.getLoggedUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AtualizarHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.AvaliarHorasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.dto.NovasHorasExtrasDTO;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.AtualizarHorasExtrasValidator;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.AvaliarHorasValidator;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.AvaliarViaLinkValidator;
import com.ferraz.controledepagamentosbackend.domain.horasextras.validations.NovasHorasExtrasValidator;
import com.ferraz.controledepagamentosbackend.domain.link.Link;
import com.ferraz.controledepagamentosbackend.domain.link.LinkRepository;
import com.ferraz.controledepagamentosbackend.domain.link.LinkStatus;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import com.ferraz.controledepagamentosbackend.domain.user.UsuarioPerfil;

import jakarta.transaction.Transactional;

@Service
public class HorasExtrasService {

	private final HorasExtrasRepository repository;
	private final UserRepository userRepository;
	private final List<NovasHorasExtrasValidator> novasHorasExtrasValidators;
	private final List<AvaliarHorasValidator> avaliarHorasValidators;
	private final List<AtualizarHorasExtrasValidator> atualizarHorasExtrasValidators;
	private final LinkRepository linkRepository;
	private final SolicitarAprovacaoService solicitarAprovacaoService;
	private final List<AvaliarViaLinkValidator> avaliarViaLinkValidators;

	public HorasExtrasService(HorasExtrasRepository repository, UserRepository userRepository,
			List<NovasHorasExtrasValidator> novasHorasExtrasValidators,
			List<AtualizarHorasExtrasValidator> atualizarHorasExtrasValidators, LinkRepository linkRepository,
			List<AvaliarHorasValidator> avaliarHorasValidators, SolicitarAprovacaoService solicitarAprovacaoService,
			List<AvaliarViaLinkValidator> avaliarViaLinkValidators) {
		this.repository = repository;
		this.userRepository = userRepository;
		this.novasHorasExtrasValidators = novasHorasExtrasValidators;
		this.avaliarHorasValidators = avaliarHorasValidators;
		this.atualizarHorasExtrasValidators = atualizarHorasExtrasValidators;
		this.linkRepository = linkRepository;
		this.solicitarAprovacaoService = solicitarAprovacaoService;
		this.avaliarViaLinkValidators = avaliarViaLinkValidators;
	}

	@Transactional
	public HorasExtras create(NovasHorasExtrasDTO dto) {
		novasHorasExtrasValidators.forEach(validator -> validator.validate(dto));
		User aprovador = userRepository.findById(dto.idAprovador()).orElseThrow();
		HorasExtras horasExtras = new HorasExtras(dto, getLoggedUser(), aprovador);
		repository.save(horasExtras);
		solicitarAprovacaoService.solicitar(horasExtras);
		return horasExtras;
	}

	public Page<HorasExtras> list(Pageable pageable, Long idUsuario, Long idAprovador, LocalDate dataInicio,
			LocalDate dataFim, String descricao, HorasExtrasStatus status) {

		if (getLoggedUser().getPerfil().equals(UsuarioPerfil.ROLE_USER))
			idUsuario = getLoggedUser().getId();

		Page<HorasExtras> page = repository.findByFiltros(pageable, idUsuario, idAprovador, descricao, status);

		if (dataInicio != null) {
			LocalDateTime dataHoraInicio = dataInicio.atTime(LocalTime.MIN);
			List<HorasExtras> list = page.stream()
					.filter(horasExtras -> dataHoraInicio.isBefore(horasExtras.getDataHoraInicio())).toList();
			page = new PageImpl<>(list, pageable, list.size());
		}

		if (dataFim != null) {
			LocalDateTime dataHoraFim = dataFim.atTime(LocalTime.MAX);
			List<HorasExtras> list = page.stream()
					.filter(horasExtras -> dataHoraFim.isAfter(horasExtras.getDataHoraFim())).toList();
			page = new PageImpl<>(list, pageable, list.size());
		}

		return page;
	}

	public HorasExtras findById(Long id) {
		return repository.findById(id).orElseThrow();
	}

	@Transactional
	public HorasExtras update(Long id, AtualizarHorasExtrasDTO atualizarHorasExtrasDTO) {
		atualizarHorasExtrasValidators.forEach(validator -> validator.validate(id, atualizarHorasExtrasDTO));
		HorasExtras horasExtras = repository.findById(id).orElseThrow();
		User aprovador = userRepository.findById(atualizarHorasExtrasDTO.idAprovador()).orElseThrow();
		horasExtras.update(atualizarHorasExtrasDTO, getLoggedUser(), aprovador);
		repository.save(horasExtras);
		solicitarAprovacaoService.solicitar(horasExtras);
		return horasExtras;
	}

	@Transactional
	public void delete(Long id) {
		HorasExtras horasExtras = repository.findById(id).orElseThrow();
		horasExtras.inativar(getLoggedUser());
		repository.save(horasExtras);
	}

	@Transactional
	public HorasExtras avaliarHora(AvaliarHorasDTO dados) {
		avaliarHorasValidators.forEach(validator -> validator.validate(dados));
		HorasExtras hora = repository.findById(dados.id()).orElseThrow();
		hora.setStatus(dados.status());
		hora.setUpdateDatetime(LocalDateTime.now());
		hora.setUpdateUser(getLoggedUser());
		repository.save(hora);
		return hora;
	}

	public Link avaliarViaLink(UUID id) {
		Link link = linkRepository.findByIdAndStatus(id, LinkStatus.CRIADO)
				.orElseThrow(() -> new NoSuchElementException(
						"Nenhum registro com status valido foi encontrado para o hash enviado"));
		avaliarViaLinkValidators.forEach(validator -> validator.validate(link));

		HorasExtras horasExtras = link.getHorasExtras();
		horasExtras.avaliar(link.getAcao().getHorasExtrasStatus());
		repository.save(horasExtras);

		link.marcarComoUsado();
		linkRepository.save(link);

		return link;
	}

}
