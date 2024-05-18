package com.ferraz.controledepagamentosbackend.domain.horasextras;

import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorasExtrasRepository extends JpaRepository<HorasExtras, Long> {

    @Query("select he from HorasExtras he where" +
            " (:idUsuario is null or :idUsuario = he.user.id)" +
            " and (:idAprovador is null or :idAprovador = he.aprovador.id)" +
            " and (:descricao is null or UPPER(he.descricao) like CONCAT('%',  UPPER(cast(:descricao AS text)), '%'))" +
            " and (:status is null or :status = he.status)"
    )
    Page<HorasExtras> findByFiltros(Pageable pageable, @Param("idUsuario") Long idUsuario,
                                    @Param("idAprovador") Long idAprovador, @Param("descricao") String descricao,
                                    @Param("status") HorasExtrasStatus status
    );

    List<HorasExtras> findByUserAndStatusIn(User user, List<HorasExtrasStatus> status);

    List<HorasExtras> findByIdNotAndUserAndStatusIn(Long id, User user, List<HorasExtrasStatus> status);

    List<HorasExtras> findByUserAndStatusAndPagoIsFalse(User prestador, HorasExtrasStatus aprovado);
}
