package br.com.fiap.smartdisaster.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MATCHING_DOACAO_NECESSIDADE")
@Getter
@Setter
@NoArgsConstructor
public class MatchingDoacaoNecessidade {

    @EmbeddedId
    private MatchingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("doacaoId")
    @JoinColumn(name = "doacao_id")
    private Doacao doacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("necessidadeId")
    @JoinColumn(name = "necessidade_id")
    private Necessidade necessidade;

    @Column(nullable = false)
    private LocalDateTime dataMatching;

    public MatchingDoacaoNecessidade(Doacao doacao, Necessidade necessidade) {
        this.doacao = doacao;
        this.necessidade = necessidade;
        this.id = new MatchingId(doacao.getId(), necessidade.getId());
        this.dataMatching = LocalDateTime.now();
    }
}
