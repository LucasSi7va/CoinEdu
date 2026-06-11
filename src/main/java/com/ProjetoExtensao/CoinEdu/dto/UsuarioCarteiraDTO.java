package com.ProjetoExtensao.CoinEdu.dto;

import java.util.List;

public record UsuarioCarteiraDTO(
        Long id ,
        String nome,
        String email,
        String token,
        String fotoPerfil ,
        String capaPerfil ,
        List<String> moeda
) {
}
