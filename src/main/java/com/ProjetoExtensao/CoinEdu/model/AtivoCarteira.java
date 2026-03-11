package com.ProjetoExtensao.CoinEdu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo_carteira")
@AllArgsConstructor @Getter @Setter
@NoArgsConstructor
public class AtivoCarteira {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "carteira_id" , nullable = false)
private Carteira carteira;

@ManyToOne
@JoinColumn(name = "moeda_id" , nullable = false)
private Moeda moeda;

@Column(nullable = false,precision = 20, scale = 10)
private BigDecimal quantidade;

}
