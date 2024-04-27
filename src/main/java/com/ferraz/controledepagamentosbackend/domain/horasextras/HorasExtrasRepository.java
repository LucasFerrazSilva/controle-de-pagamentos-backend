package com.ferraz.controledepagamentosbackend.domain.horasextras;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorasExtrasRepository extends JpaRepository<HorasExtras, Long> {

    @Query("select he from HorasExtras he where" +
            " (:idUsuario is null or :idUsuario = he.user.id)" +
            " and (:idAprovador is null or :idAprovador = he.aprovador.id)" +
            " and (:descricao is null or he.descricao like CONCAT('%',  cast(:descricao AS text), '%'))" +
            " and (:status is null or :status = he.status)"
    )
    Page<HorasExtras> findByFiltros(Pageable pageable, @Param("idUsuario") Long idUsuario,
                                    @Param("idAprovador") Long idAprovador, @Param("descricao") String descricao,
                                    @Param("status") HorasExtrasStatus status
    );

}
