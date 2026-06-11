package com.ProjetoExtensao.CoinEdu.dto;

public record UsuarioDto(
        Long id,
        String nome,
        String email ,
        String FotoPerfil ,
        String CapaPerfil ) {
}
