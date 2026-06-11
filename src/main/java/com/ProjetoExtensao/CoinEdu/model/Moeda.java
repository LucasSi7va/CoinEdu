package com.ProjetoExtensao.CoinEdu.model;


import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "moedas")
@Getter
@Setter
public class Moeda {

    @Id
    private String id;

    @Field(type = FieldType.Text , name = "nome")
    private String nome;

    @Field(type = FieldType.Keyword , name = "simbolo")
    private String simbolo;

    @Field(type = FieldType.Keyword , name = "imagem")
    private String imagem;

    @Field(type = FieldType.Double , name = "preco_atual")
    private BigDecimal precoAtual;

    @Field(type = FieldType.Integer , name = "rank")
    private Integer rank;

    @Field(type = FieldType.Double , name = "market_cap")
    private BigDecimal MarketCap;

    @Field(type = FieldType.Double , name = "volume_24h")
    private BigDecimal volume24h;
}
