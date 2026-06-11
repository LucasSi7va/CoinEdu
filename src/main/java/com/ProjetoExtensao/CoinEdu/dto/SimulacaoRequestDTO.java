package com.ProjetoExtensao.CoinEdu.dto;

import java.math.BigDecimal;

public record SimulacaoRequestDTO(

        Long usuarioId ,
        String moeda ,
        BigDecimal valorCompra ,
        BigDecimal precoAtual
) {
}
