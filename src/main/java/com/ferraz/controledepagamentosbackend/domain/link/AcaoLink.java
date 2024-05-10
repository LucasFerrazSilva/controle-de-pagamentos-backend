package com.ferraz.controledepagamentosbackend.domain.link;

import com.ferraz.controledepagamentosbackend.domain.horasextras.HorasExtrasStatus;

public enum AcaoLink {
    APROVAR(HorasExtrasStatus.APROVADO),
    RECUSAR(HorasExtrasStatus.RECUSADO);

    private HorasExtrasStatus horasExtrasStatus;
    AcaoLink(HorasExtrasStatus horasExtrasStatus) {
        this.horasExtrasStatus = horasExtrasStatus;
    }
    public HorasExtrasStatus getHorasExtrasStatus() {
        return this.horasExtrasStatus;
    }
}
