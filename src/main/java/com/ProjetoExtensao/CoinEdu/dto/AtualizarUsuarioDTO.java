package com.ProjetoExtensao.CoinEdu.dto;

public record AtualizarUsuarioDTO(
         String emailAtual ,
         String novoNome,
         String novoEmail,
         String novaSenha
) {
}
