package com.ProjetoExtensao.CoinEdu.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ProjetoExtensao.CoinEdu.dto.MoedaDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.model.Moeda;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ElasticService {

    private final ElasticsearchClient elasticsearchClient;
    private final ServiceMoedaAPI serviceMoedaAPI;

    @PostConstruct
    public void setup() {
        try {
            boolean exists = elasticsearchClient.indices().exists(e -> e.index("moedas")).value();
            if (!exists) {
                elasticsearchClient.indices().create(c -> c
                        .index("moedas")
                        .mappings(m -> m
                                .properties("nome", p -> p
                                        .text(t -> t
                                                .fields("keyword", f -> f.keyword(k -> k))
                                        )
                                )
                        )
                );
            }
            sincronizarMoedas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 60000)
    public void atualizarMoedas() throws IOException {
        try {
            System.out.println("Iniciando sincronização...");
            sincronizarMoedas();
            System.out.println("Sincronização concluída!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sincronizarMoedas() throws IOException {
        List<MoedaDto> moedas = serviceMoedaAPI.getMercado();


        List<MoedaDto> topMoedas = moedas.stream()
                .sorted(Comparator.comparing(MoedaDto::marketCap).reversed())
                .limit(50)
                .toList();

        if(topMoedas.isEmpty())return;

        BulkRequest.Builder bulk = new BulkRequest.Builder();

        topMoedas.forEach(moeda -> {
            Moeda m = new Moeda();
            m.setId(moeda.id());
            m.setNome(moeda.nome());
            m.setSimbolo(moeda.symbol());
            m.setImagem(moeda.imagem());
            m.setPrecoAtual(BigDecimal.valueOf(moeda.precoAtual()));
            m.setRank(moeda.rank());
            m.setMarketCap(BigDecimal.valueOf(moeda.marketCap()));


            bulk.operations(op -> op.index(i -> i.index("moedas").id(moeda.id()).document(m)));

        });
        elasticsearchClient.bulk(bulk.build());


    }

    public List<Moeda> listarTudo() throws IOException {
        SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
                        .index("moedas")
                        .query(q -> q.matchAll(m -> m))
                        .size(100),
                Moeda.class);


        return response.hits().hits()
                .stream()
                .map(Hit::source)
                .toList();
    }

    public List<Moeda> buscarPorNome(String nome) throws IOException {
        SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
                        .index("moedas")
                        .query(q -> q
                                .bool(b -> b
                                        .should(sh -> sh.match(m -> m
                                                .field("nome")
                                                .query(nome)
                                                .fuzziness("AUTO")
                                                .boost(2.0f)
                                        ))

                                        .should(sh -> sh.prefix(p -> p
                                                .field("nome.keyword")
                                                .value(nome.toLowerCase())
                                        ))

                                        .should(sh -> sh.wildcard(w -> w
                                                .field("nome")
                                                .value("*" + nome.toLowerCase() + "*")
                                        ))
                                        .minimumShouldMatch("1")
                                )
                        ),
                Moeda.class);

        return response.hits().hits().stream().map(Hit::source).toList();
    }


    public List<Moeda> buscarPorFiltro(FiltroGlobal filtroGlobal) throws IOException {
        SearchResponse<Moeda> response = elasticsearchClient.search(s -> s
                        .index("moedas")
                        .query(q -> q
                                .bool(b -> {
                                    Optional.ofNullable(filtroGlobal.precoMax())
                                            .ifPresent(precoAtual -> b.filter(f -> f
                                                    .range(r -> r.field("precoAtual").lte(JsonData.of(precoAtual)))
                                            ));
                                    Optional.ofNullable(filtroGlobal.precoMin())
                                                    .ifPresent(precoAtual -> b.filter(f -> f
                                                            .range(r -> r.field("precoAtual").gte(JsonData.of(precoAtual)))));


                                    Optional.ofNullable(filtroGlobal.rank())
                                            .ifPresent(rank -> b.filter(f -> f
                                                    .term(t -> t.field("rank").value(rank))
                                            ));

                                    return b;
                                }))
                , Moeda.class);

        return response.hits().hits().stream().map(Hit::source).toList();
    }

}