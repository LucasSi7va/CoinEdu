package com.ProjetoExtensao.CoinEdu.dto;

import com.ProjetoExtensao.CoinEdu.model.Usuario;

import java.time.LocalDateTime;

public record PendenteCadastroDto(
        Usuario usuario,
        String codigo ,
        LocalDateTime expiracao
) {
}
