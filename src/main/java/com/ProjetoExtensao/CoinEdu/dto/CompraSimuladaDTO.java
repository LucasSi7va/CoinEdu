package com.ProjetoExtensao.CoinEdu.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CompraSimuladaDTO(
        String nomeMoeda ,
        BigDecimal valorDigitado,
        BigDecimal QuantidadeObtida,
        BigDecimal valorAtual ,
        LocalDateTime dataSimulacao
) {
}
