package com.ProjetoExtensao.CoinEdu.scheduler;

import com.ProjetoExtensao.CoinEdu.repository.SimuladorHistoricoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class SimulacaoLimpezaScheduler {


    private final SimuladorHistoricoRepository simuladorHistoricoRepository;


    @Scheduled(fixedRate = 3 * 24 * 60 * 60 * 1000L)
    @Transactional
    public void LimparHistoricoAntigo() {
                LocalDateTime tresdiasAtras = LocalDateTime.now().minusDays(3);
                simuladorHistoricoRepository.deleteByDataSimulacaoBefore(tresdiasAtras);
                System.out.println("Historico de simulacoes limpo em: " + LocalDateTime.now());
            }
}
