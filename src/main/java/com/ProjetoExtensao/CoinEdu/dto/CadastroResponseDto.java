package com.ProjetoExtensao.CoinEdu.dto;

public record CadastroResponseDto(
        UsuarioDto usuarioDto,
        String mensagem
) {
}
