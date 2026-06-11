package com.ProjetoExtensao.CoinEdu.repository;

import com.ProjetoExtensao.CoinEdu.model.SimuladorHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SimuladorHistoricoRepository  extends JpaRepository<SimuladorHistorico , Long> {

    List<SimuladorHistorico> findByUsuarioIdAndNomeMoedaOrderByDataSimulacaoDesc(Long usuarioId, String nomeMoeda);

    Long countByUsuarioId(Long usuarioId);

    void deleteByDataSimulacaoBefore(LocalDateTime data);

}
