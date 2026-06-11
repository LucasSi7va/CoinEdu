package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.*;
import com.ProjetoExtensao.CoinEdu.model.Carteira;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.SimuladorHistoricoRepository;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import com.ProjetoExtensao.CoinEdu.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ServiceUsuario {

    private final UsuarioRepository usuarioRepository;

    private final SimuladorHistoricoRepository simuladorHistoricoRepository;

    private final PasswordEncoder passwordEncoder;
    private final ServiceMoedaAPI serviceMoedaAPI;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    private final Map<String, PendenteCadastroDto> pendentes = new ConcurrentHashMap<>();

    public final Map<String , Integer> tentativas = new ConcurrentHashMap<>();

    public ResponseEntity<List<UsuarioDto>> listartodos() {
        List<Usuario> usuarios = usuarioRepository.findUsuariosComFavoritos();

        List<UsuarioDto> dtos = usuarios.stream()
                .map(u -> new UsuarioDto(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getFotoPerfil(),
                        u.getCapaPerfil()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


    public ResponseEntity<UsuarioDto> getIdUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id nao encontrado"));

        return ResponseEntity.ok(new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoPerfil() ,
                usuario.getCapaPerfil()
        ));
    }



    public ResponseEntity<CadastroResponseDto> cadastrarUsuario(Usuario usuario , String ip) {

        int count = tentativas.getOrDefault(ip , 0);

        if (count >= 3) {
            throw new RuntimeException("Muitas tentativas , tente novamente mais tarde. ");

        }
        tentativas.merge(ip , 1 , Integer::sum);

        CompletableFuture.delayedExecutor(10, TimeUnit.MINUTES)
                .execute(() -> tentativas.remove(ip));

        usuario.setNome(usuario.getNome().trim());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ja cadastrado");
        }

        if (pendentes.containsKey(usuario.getEmail())) {
            throw new RuntimeException("Confirmação já enviada. Verifique seu email.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        String codigo = emailService.gerarCodigo();


        if (usuario.getFotoPerfil() == null) {
            usuario.setFotoPerfil("https://i.pinimg.com/1200x/f0/2a/8c/f02a8c54b85505713fdaf12d4de3df20.jpg" + usuario.getNome());
        }

        if (usuario.getCapaPerfil() == null) {
            usuario.setCapaPerfil("default-cover-url.jpg");
        }

        pendentes.put(usuario.getEmail() , new PendenteCadastroDto(
                usuario , codigo , LocalDateTime.now().plusMinutes(15)
        ));

        emailService.enviarCodigo(usuario.getEmail() , codigo , "cadastro");

        return ResponseEntity.ok( new CadastroResponseDto(
                    null  ,  "Codigo enviado para " + usuario.getEmail()
        ));
    }



    public ResponseEntity<UsuarioCadastradoDTO> confirmarCadastro(String email, String codigo) {
        PendenteCadastroDto pendente = pendentes.get(email);

        if (pendente == null) {
            throw new RuntimeException("Conta já verificada");
        }

        if (!pendente.codigo().equals(codigo)) {
            throw new RuntimeException("Código inválido");
        }

        if (LocalDateTime.now().isAfter(pendente.expiracao())) {
            pendentes.remove(email);
            throw new RuntimeException("Código expirado");
        }

        Usuario usuario = pendente.usuario();
        usuario.setVerificado(true);

        Carteira carteira = new Carteira();
        carteira.setUsuario(usuario);
        carteira.setMoedasFavoritas(new ArrayList<>());
        carteira.setSaldoSimulado(BigDecimal.ZERO);
        usuario.setCarteira(carteira);

        usuarioRepository.save(usuario);
        pendentes.remove(email);


        return ResponseEntity.ok( new UsuarioCadastradoDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha() ,
                usuario.getFotoPerfil() ,
                usuario.getCapaPerfil()
        ));
    }



    public ResponseEntity<UsuarioCarteiraDTO> acessarLogin(LoginDto loginDto) {

        Usuario usuario = usuarioRepository.findByEmail(loginDto.email()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        if (!passwordEncoder.matches(loginDto.senha() , usuario.getSenha())) {
            throw new RuntimeException("Senha invãlida");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());

       List<String> favoritas = usuario.getCarteira().getMoedasFavoritas();

        return ResponseEntity.ok(new UsuarioCarteiraDTO(
        usuario.getId(),
        usuario.getNome() ,
        usuario.getEmail() ,
        token,
        usuario.getFotoPerfil(),
        usuario.getCapaPerfil(),
        favoritas
        ));

    }

    public ResponseEntity<String> atualizarFoto(String email , String novaFoto) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuario.setFotoPerfil(novaFoto);
        usuarioRepository.save(usuario);

    return ResponseEntity.ok("Foto atualizada com sucesso");
    }


    public ResponseEntity<String> atualizarCapaDePefil(String email , String fotoPerfil  , String fotoCapa){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuario.setFotoPerfil(fotoPerfil);
        usuario.setCapaPerfil(fotoCapa);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Foto atualizada com sucesso");

    }



    public ResponseEntity<ModoIdosoDto> modoIdoso(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        boolean valorAtual = usuario.getModoIdoso() != null && usuario.getModoIdoso();
        usuario.setModoIdoso(!valorAtual);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ModoIdosoDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getModoIdoso()
        ));
    }

    public ResponseEntity<String> atualizarUsuario(AtualizarUsuarioDTO dto, String emailAutenticado) {

        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));


        if (dto.novoNome() != null && !dto.novoNome().isBlank()) {
            usuario.setNome(dto.novoNome());
        }

        if (dto.novoEmail() != null && !dto.novoEmail().isBlank()) {
            usuarioRepository.findByEmail(dto.novoEmail()).ifPresent( u -> {
                if (!u.getEmail().equals(emailAutenticado)) {
                    throw new RuntimeException("Email ja esta em uso");
                }
                    });
            usuario.setEmail(dto.novoEmail());
        }

        if (dto.novaSenha() != null && !dto.novaSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("nome e senha atualizada com sucesso");


    }


}
