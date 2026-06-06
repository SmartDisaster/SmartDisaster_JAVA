package br.com.fiap.smartdisaster.dto.response;

import java.time.LocalDateTime;

public record MatchingResponse(
        Long doacaoId,
        Long necessidadeId,
        String tipoDoacao,
        String tipoNecessidade,
        String abrigoNome,
        LocalDateTime dataMatching
) {
}
