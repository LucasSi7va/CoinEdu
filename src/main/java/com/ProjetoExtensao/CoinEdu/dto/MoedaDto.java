package com.ProjetoExtensao.CoinEdu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MoedaDto(
       String id,

       @JsonProperty("name")
        String nome,

        @JsonProperty("symbol")
        String symbol ,

        @JsonProperty("image")
        String imagem ,

       @JsonProperty("current_price")
       Double precoAtual,

        @JsonProperty("price_change_percentage_24h")
        Double variacao24h,

        @JsonProperty("market_cap_rank")
        Integer rank,

        @JsonProperty("market_cap")
        Double marketCap
) {
}
