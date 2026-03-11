package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Entity
@Table(name = "moeda")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Moeda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false , unique = true)
    private String sigla;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false , precision = 20, scale = 10)
    private BigDecimal precoAtual;

    private BigDecimal marketCap;
    private BigDecimal ultimaAtualizacao;

}
