package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.request.NecessidadeRequest;
import br.com.fiap.smartdisaster.dto.response.NecessidadeResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Necessidade;
import org.springframework.stereotype.Component;

@Component
public class NecessidadeMapper {

    public Necessidade toEntity(NecessidadeRequest request, Abrigo abrigo) {
        Necessidade necessidade = new Necessidade();
        necessidade.setTipo(request.tipo());
        necessidade.setDescricao(request.descricao());
        necessidade.setQuantidadeNecessaria(request.quantidadeNecessaria());
        necessidade.setStatus(request.status());
        necessidade.setAbrigo(abrigo);
        return necessidade;
    }

    public NecessidadeResponse toResponse(Necessidade necessidade) {
        return new NecessidadeResponse(
                necessidade.getId(),
                necessidade.getTipo(),
                necessidade.getDescricao(),
                necessidade.getQuantidadeNecessaria(),
                necessidade.getStatus(),
                necessidade.getAbrigo().getId(),
                necessidade.getAbrigo().getNome()
        );
    }

    public void updateEntity(Necessidade necessidade, NecessidadeRequest request, Abrigo abrigo) {
        necessidade.setTipo(request.tipo());
        necessidade.setDescricao(request.descricao());
        necessidade.setQuantidadeNecessaria(request.quantidadeNecessaria());
        necessidade.setStatus(request.status());
        necessidade.setAbrigo(abrigo);
    }
}
