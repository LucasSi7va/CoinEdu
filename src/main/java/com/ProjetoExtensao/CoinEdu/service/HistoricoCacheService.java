package com.ProjetoExtensao.CoinEdu.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.ProjetoExtensao.CoinEdu.model.HistoricoCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class HistoricoCacheService {

    private final ElasticsearchClient elasticsearchClient;

    public void salvarHistorico(String moedaId, String data, Map<String, Object> dados) throws IOException {
        String docId = moedaId + "_" + data;

        HistoricoCache cache = new HistoricoCache();
        cache.setMoeda((String) dados.get("moeda"));
        cache.setData((String) dados.get("data"));
        cache.setPreco_brl(((Number) dados.get("preco_brl")).doubleValue());

        elasticsearchClient.index(i -> i
                .index("historico_moedas")
                .id(docId)
                .document(cache));
    }

    public Map<String, Object> buscarHistoricoSalvo(String moedaId, String data) throws IOException {
        String docId = moedaId + "_" + data;

        var response = elasticsearchClient.get(g -> g
                .index("historico_moedas")
                .id(docId), HistoricoCache.class);

        if (response.found()) {
            HistoricoCache cache = response.source();
            return Map.of(
                    "moeda", cache.getMoeda(),
                    "data", cache.getData(),
                    "preco_brl", cache.getPreco_brl()
            );
        }

        throw new RuntimeException("API indisponível e sem histórico salvo para " + moedaId + " em " + data);
    }
}
