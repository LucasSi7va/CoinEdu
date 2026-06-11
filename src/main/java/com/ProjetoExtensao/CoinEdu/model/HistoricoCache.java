package com.ProjetoExtensao.CoinEdu.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "historico_moedas")
@Getter @Setter
public class HistoricoCache {
    private String moeda;
    private String data;
    private Double preco_brl;
}
