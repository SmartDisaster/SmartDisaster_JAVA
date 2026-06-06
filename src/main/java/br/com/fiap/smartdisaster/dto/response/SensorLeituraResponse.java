package br.com.fiap.smartdisaster.dto.response;

import java.time.LocalDateTime;

public record SensorLeituraResponse(
        Long id,
        Integer ocupacaoAtual,
        Double temperatura,
        LocalDateTime timestamp,
        Long abrigoId,
        String abrigoNome
) {
}
