package com.ProjetoExtensao.CoinEdu.dto;

import java.math.BigDecimal;

public record SimulacaoDto(
        String moedaId ,
        String moedaNome ,
        BigDecimal valorInvestido ,
        BigDecimal valorAtual ,
        BigDecimal quantidadeObtida
) {
}
