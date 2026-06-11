package com.ProjetoExtensao.CoinEdu.dto;

public record AlertaDto(
        String moedaId,
        Double precoAnterior,
        Double precoAtual,
        Double variacaoPercent
) {
}
