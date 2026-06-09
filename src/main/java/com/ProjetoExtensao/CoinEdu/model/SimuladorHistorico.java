package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulacao_historico")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimuladorHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // ← LAZY evita carregar usuario inteiro desnecessariamente
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String nomeMoeda;

    @Column(nullable = false, precision = 19, scale = 8)  // ← precisão para crypto
    private BigDecimal valorDigitado;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal precoAtualDaMoeda;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal resultado;

    @Column(nullable = false)
    private LocalDateTime dataSimulacao;
}
