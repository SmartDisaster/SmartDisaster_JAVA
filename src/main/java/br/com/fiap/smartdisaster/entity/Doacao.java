package br.com.fiap.smartdisaster.entity;

import br.com.fiap.smartdisaster.enums.StatusDoacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "TB_DOACOES")
@Getter
@Setter
@NoArgsConstructor
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDate dataDoacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDoacao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voluntario_id", nullable = false)
    private Voluntario voluntario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abrigo_id", nullable = false)
    private Abrigo abrigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "necessidade_id", nullable = false)
    private Necessidade necessidade;
}
