package com.ProjetoExtensao.CoinEdu.service;


import com.ProjetoExtensao.CoinEdu.dto.AlertaDto;
import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ServiceAlerta {

private final UsuarioRepository usuarioRepository;
private final ServiceMoedaAPI serviceMoedaAPI;
private final EmailService emailService;
private final ServiceSse serviceSse;

    private final Map<String, LocalDateTime> ultimosAlertas = new ConcurrentHashMap<>();
    private final Map<String, Double> ultimosPrecos = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 300000)
    public void verificarAlertas() {
    List<MoedaDto> mercado = serviceMoedaAPI.getMercado();
    List<Usuario> usuarios = usuarioRepository.findAll();


        for (MoedaDto moedaDto : mercado) {
        Double precoAnterior = ultimosPrecos.get(moedaDto.id());
        Double precoAtual = moedaDto.precoAtual();

        if (precoAnterior == null) {
            ultimosPrecos.put(moedaDto.id(), precoAtual);
            continue;
        }
        double variacao = ((precoAtual - precoAnterior) / precoAnterior) * 100;

        if (Math.abs(variacao) >= 5.0) {
            for (Usuario usuario : usuarios) {
                if (usuario.getCarteira() == null) continue;
                if (!usuario.getCarteira().getMoedasFavoritas().contains(moedaDto.id())) continue;

                String chave = usuario.getId() + "_" + moedaDto.id();
                LocalDateTime ultimoEnvio = ultimosAlertas.get(chave);

                if (ultimoEnvio != null && ultimoEnvio.isAfter(LocalDateTime.now().minusHours(24))) continue;

                if (variacao <= -5.0) {
                    emailService.enviarAlertaQueda(
                            usuario.getEmail(),
                            usuario.getNome(),
                            moedaDto.nome(),
                            precoAtual,
                            variacao
                            );

                    serviceSse.enviar(
                        new AlertaDto(
                                moedaDto.id() ,
                                precoAnterior,
                                precoAtual,
                                variacao
                        )
                    );

                    ultimosAlertas.put(chave, LocalDateTime.now());

                }
                else if (variacao >= 10.0) {

                    emailService.enviarAlertaSubida(
                            usuario.getEmail(),
                            usuario.getNome(),
                            moedaDto.nome(),
                            precoAtual,
                            variacao
                    );

                    serviceSse.enviar(
                            new AlertaDto(
                                    moedaDto.id(),
                                    precoAnterior,
                                    precoAtual,
                                    variacao
                            )
                    );

                    ultimosAlertas.put(chave, LocalDateTime.now());
                }
            }
        }

            ultimosPrecos.put(moedaDto.id(), precoAtual);
        }
    }
}

