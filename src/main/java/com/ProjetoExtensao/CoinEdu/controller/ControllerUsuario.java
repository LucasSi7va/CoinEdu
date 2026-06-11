package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.*;

import com.ProjetoExtensao.CoinEdu.model.Usuario;

import com.ProjetoExtensao.CoinEdu.service.ServiceUsuario;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControllerUsuario {

@Autowired
private final ServiceUsuario serviceUsuario;


@GetMapping
public ResponseEntity<List<UsuarioDto>> listResponseEntity() {
    return serviceUsuario.listartodos();
}


@GetMapping("{id}")
public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id){

    return  serviceUsuario.getIdUsuario(id);
}

@PostMapping("/cadastrar")
    public ResponseEntity<CadastroResponseDto> CadastrarUsuario(@RequestBody Usuario usuario , HttpServletRequest request){
    return serviceUsuario.cadastrarUsuario(usuario , request.getRemoteAddr());
}

@PostMapping("/confirmar-cadastro")
public ResponseEntity<UsuarioCadastradoDTO> confirmarCadastro(
        @RequestBody ConfirmarCadastroDto dto) {
return serviceUsuario.confirmarCadastro(dto.email() , dto.codigo());
}

@PostMapping("/login")
public ResponseEntity<UsuarioCarteiraDTO> login(@RequestBody LoginDto loginDto){
    return serviceUsuario.acessarLogin(loginDto);
}

@PostMapping("/editar-fotoPerfil")
public ResponseEntity<String> ajustarFoto(@RequestParam String email , @RequestParam String foto){
    return serviceUsuario.atualizarFoto(email ,foto);
}


@PostMapping("/editar-fotoPerfil/capaPerfil")
public ResponseEntity<String> ajustarFotoECapa(@RequestBody PerfilRequestDto requestDto){
    return serviceUsuario.atualizarCapaDePefil(requestDto.email(), requestDto.fotoPerfil(), requestDto.capaPerfil());
}


    @PutMapping("/atualizar-usuario")
    public ResponseEntity<String> atualizar(@RequestBody AtualizarUsuarioDTO dto,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("DTO recebido: " + dto);
        System.out.println("UserDetails: " + userDetails);
        System.out.println("Email: " + userDetails.getUsername());
        String emailAutenticado = userDetails.getUsername();
        return serviceUsuario.atualizarUsuario(dto, emailAutenticado);
    }





    @PatchMapping("/{id}/alternar-modo-idoso")
    public ResponseEntity<ModoIdosoDto> ModoIdoso(@PathVariable Long id) {
        return serviceUsuario.modoIdoso(id);
    }



}
