package com.ProjetoExtensao.CoinEdu.dto;

public record UsuarioCadastradoDTO(

        Long id,
        String nome,
        String email ,
        String FotoPerfil ,
        String CapaPerfil,
        String senha
) {
}
