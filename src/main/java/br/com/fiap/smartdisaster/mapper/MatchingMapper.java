package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.response.MatchingResponse;
import br.com.fiap.smartdisaster.entity.MatchingDoacaoNecessidade;
import org.springframework.stereotype.Component;

@Component
public class MatchingMapper {

    public MatchingResponse toResponse(MatchingDoacaoNecessidade matching) {
        return new MatchingResponse(
                matching.getDoacao().getId(),
                matching.getNecessidade().getId(),
                matching.getDoacao().getTipo(),
                matching.getNecessidade().getTipo(),
                matching.getNecessidade().getAbrigo().getNome(),
                matching.getDataMatching()
        );
    }
}
