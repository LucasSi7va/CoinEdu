package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.CompraSimuladaDTO;
import com.ProjetoExtensao.CoinEdu.dto.SimulacaoDto;
import com.ProjetoExtensao.CoinEdu.dto.SimulacaoRequestDTO;
import com.ProjetoExtensao.CoinEdu.service.ServiceCarteira;
import com.ProjetoExtensao.CoinEdu.service.SimuladorHistoricoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/carteira")
@AllArgsConstructor
public class ControllerCarteira {

    @Autowired
    private final ServiceCarteira serviceCarteira;

    @Autowired
    private final SimuladorHistoricoService serviceSimuladorHistorico;

    @PostMapping("/favoritar")
    public ResponseEntity<String> favoritar(
            @RequestParam Long usuarioId,
            @RequestParam String moeda
    ) {
        serviceCarteira.salvarCarteira(usuarioId, moeda);

        return ResponseEntity.ok("Moeda adicionada com sucesso");
    }


    @DeleteMapping("/remover")
    public ResponseEntity<String> removerCarteira(
            @RequestParam Long usuarioId,
            @RequestParam String moeda
    ) {
        serviceCarteira.removerCarteira(usuarioId, moeda);

        return ResponseEntity.ok("Moeda Removida com sucesso");
    }

    @GetMapping("/simulacao")
    public ResponseEntity<SimulacaoDto> simularCompra(
            @RequestParam String moeda,
            @RequestParam BigDecimal valorCompra,
            @RequestParam Long usuarioId,
            @RequestParam BigDecimal precoAtual) {

        return serviceCarteira.simularCompra(usuarioId ,  moeda , valorCompra  , precoAtual);
    }


    @PostMapping("/historico")
    public ResponseEntity<CompraSimuladaDTO> simular(@RequestBody SimulacaoRequestDTO request) {
        CompraSimuladaDTO resultado = serviceSimuladorHistorico.salvarHistoricoSimulacao(
                request.usuarioId(),
                request.moeda(),
                request.valorCompra(),
                request.precoAtual()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping("/historico/{usuarioId}/{nomeMoeda}")
    public ResponseEntity<List<CompraSimuladaDTO>> buscarHistorico(@PathVariable Long usuarioId , @PathVariable String nomeMoeda) {
        return ResponseEntity.ok(serviceSimuladorHistorico.buscarHistorico(usuarioId , nomeMoeda));
    }
}