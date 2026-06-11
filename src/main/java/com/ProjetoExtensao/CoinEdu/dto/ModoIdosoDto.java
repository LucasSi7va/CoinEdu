package com.ProjetoExtensao.CoinEdu.dto;

public record ModoIdosoDto(
        Long id,
        String nome,
        String email,
        String senha,
        boolean modoIdoso
) {
}
