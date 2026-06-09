package com.ProjetoExtensao.CoinEdu.service;

import com.ProjetoExtensao.CoinEdu.dto.CompraSimuladaDTO;
import com.ProjetoExtensao.CoinEdu.model.SimuladorHistorico;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.SimuladorHistoricoRepository;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimuladorHistoricoService {


private final SimuladorHistoricoRepository simuladorHistoricoRepository;
private final UsuarioRepository usuarioRepository;


@Transactional
    public CompraSimuladaDTO salvarHistoricoSimulacao(
            Long usuarioId,
            String moeda,
            BigDecimal valorCompra,
            BigDecimal precoAtual
) {

    if (precoAtual.compareTo(BigDecimal.ZERO) <= 0) {
        throw  new IllegalArgumentException("Preco atual invalido");
    }


    Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));


    long totalSimulacoes = simuladorHistoricoRepository.countByUsuarioId(usuarioId);


    if (totalSimulacoes >= 3) {
        throw new RuntimeException("Limite de 3 simulacoes antigido. Aguarde a limpeza automatica");
    }

    BigDecimal resultado = valorCompra.divide(precoAtual, 8 , RoundingMode.HALF_UP);

    SimuladorHistorico historico = new SimuladorHistorico();

    historico.setUsuario(usuario);
    historico.setNomeMoeda(moeda);
    historico.setValorDigitado(valorCompra);
    historico.setPrecoAtualDaMoeda(precoAtual);
    historico.setResultado(resultado);
    historico.setDataSimulacao(LocalDateTime.now());

    simuladorHistoricoRepository.save(historico);

    return new CompraSimuladaDTO(moeda , valorCompra , resultado, precoAtual , historico.getDataSimulacao());
}


public List<CompraSimuladaDTO> buscarHistorico(Long usuarioId , String nomeMoeda) {
    return simuladorHistoricoRepository
            .findByUsuarioIdAndNomeMoedaOrderByDataSimulacaoDesc(usuarioId , nomeMoeda)
            .stream()
            .map(h -> new CompraSimuladaDTO(
                    h.getNomeMoeda(),
                    h.getValorDigitado(),
                    h.getResultado(),
                    h.getPrecoAtualDaMoeda() ,
                    h.getDataSimulacao()
            )).toList();
}


}
