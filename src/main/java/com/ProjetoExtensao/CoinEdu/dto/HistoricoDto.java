package com.ProjetoExtensao.CoinEdu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HistoricoDto(
        String id,
        String name,

        @JsonProperty("market_data")
        MarketData marketData
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MarketData(
            @JsonProperty("current_price")
            CurrentPrice currentPrice
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentPrice(
            Double brl
    ) {}
}
