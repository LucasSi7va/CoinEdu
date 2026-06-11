package com.ProjetoExtensao.CoinEdu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "carteira")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "usuario_id")
    @OneToOne
    @JsonIgnore
    private Usuario usuario;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "moedas_favoritas" , joinColumns = @JoinColumn(name = "carteira_id"))
    @Column(name = "moeda_id")
    private List<String> moedasFavoritas;

    @Column(precision = 25, scale = 2)
    private BigDecimal saldoSimulado;
}