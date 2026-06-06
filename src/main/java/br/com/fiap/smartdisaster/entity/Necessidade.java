package br.com.fiap.smartdisaster.entity;

import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_NECESSIDADES")
@Getter
@Setter
@NoArgsConstructor
public class Necessidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidadeNecessaria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNecessidade status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abrigo_id", nullable = false)
    private Abrigo abrigo;
}
