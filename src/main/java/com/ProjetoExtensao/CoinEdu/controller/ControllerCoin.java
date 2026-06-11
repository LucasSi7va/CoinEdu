package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.model.Moeda;
import com.ProjetoExtensao.CoinEdu.service.ElasticService;
import com.ProjetoExtensao.CoinEdu.service.ServiceMoedaAPI;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coin")
@AllArgsConstructor
public class ControllerCoin {
    @Autowired
    private final ServiceMoedaAPI serviceMoedaAPI;


    @Autowired
    private final ElasticService elasticService;


    @GetMapping
    public ResponseEntity<List<Moeda>> listarTudo() throws IOException {
        return ResponseEntity.ok(elasticService.listarTudo());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Moeda>> buscarNome(@PathVariable String nome) throws IOException {

        return ResponseEntity.ok(elasticService.buscarPorNome(nome));
    }


    @GetMapping("filtra")
    public ResponseEntity<List<Moeda>> buscarPorFiltro(FiltroGlobal filtroGlobal) throws IOException {
        return ResponseEntity.ok(elasticService.buscarPorFiltro(filtroGlobal));
    }


    @GetMapping("/{id}/historico")
    public Map<String, Object> getHistorico(
            @PathVariable String id,
            @RequestParam(defaultValue = "3") int dias) {
        return serviceMoedaAPI.getHistoricoPorDias(id.toLowerCase(), dias);
    }

    @GetMapping("/{id}/historico/lista")
    public List<Map<String, Object>> getHistoricoLista(
            @PathVariable String id,
            @RequestParam(defaultValue = "7") int dias) {
        return serviceMoedaAPI.getListaHistorico(id.toLowerCase(), dias);
    }


}
