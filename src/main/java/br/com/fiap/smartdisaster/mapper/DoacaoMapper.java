package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.request.DoacaoRequest;
import br.com.fiap.smartdisaster.dto.response.DoacaoResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Doacao;
import br.com.fiap.smartdisaster.entity.Necessidade;
import br.com.fiap.smartdisaster.entity.Voluntario;
import br.com.fiap.smartdisaster.enums.StatusDoacao;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DoacaoMapper {

    public Doacao toEntity(DoacaoRequest request, Voluntario voluntario, Abrigo abrigo, Necessidade necessidade) {
        Doacao doacao = new Doacao();
        doacao.setTipo(request.tipo());
        doacao.setDescricao(request.descricao());
        doacao.setQuantidade(request.quantidade());
        doacao.setDataDoacao(LocalDate.now());
        doacao.setStatus(StatusDoacao.PENDENTE_ENTREGA);
        doacao.setVoluntario(voluntario);
        doacao.setAbrigo(abrigo);
        doacao.setNecessidade(necessidade);
        return doacao;
    }

    public DoacaoResponse toResponse(Doacao doacao) {
        return new DoacaoResponse(
                doacao.getId(),
                doacao.getTipo(),
                doacao.getDescricao(),
                doacao.getQuantidade(),
                doacao.getDataDoacao(),
                doacao.getStatus(),
                doacao.getVoluntario().getId(),
                doacao.getVoluntario().getNome(),
                doacao.getAbrigo() != null ? doacao.getAbrigo().getId() : null,
                doacao.getAbrigo() != null ? doacao.getAbrigo().getNome() : null,
                doacao.getNecessidade() != null ? doacao.getNecessidade().getId() : null,
                doacao.getNecessidade() != null ? doacao.getNecessidade().getDescricao() : null
        );
    }

    public void updateEntity(Doacao doacao, DoacaoRequest request, Voluntario voluntario,
                             Abrigo abrigo, Necessidade necessidade) {
        doacao.setTipo(request.tipo());
        doacao.setDescricao(request.descricao());
        doacao.setQuantidade(request.quantidade());
        doacao.setVoluntario(voluntario);
        doacao.setAbrigo(abrigo);
        doacao.setNecessidade(necessidade);
    }
}
