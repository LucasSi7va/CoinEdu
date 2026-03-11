package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carteira")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Carteira
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne

    @JoinColumn(name = "usuario_id" , nullable = false)
    private Usuario usuario;

    @Column(precision = 20, scale = 10)
    private BigDecimal saldoFiat = BigDecimal.ZERO;


    @OneToMany(mappedBy = "carteira" , cascade = CascadeType.ALL)
    private List<AtivoCarteira> ativos = new ArrayList<>();
}
