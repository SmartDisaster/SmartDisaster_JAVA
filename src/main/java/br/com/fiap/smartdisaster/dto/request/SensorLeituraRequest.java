package br.com.fiap.smartdisaster.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record SensorLeituraRequest(
        @NotNull(message = "Ocupação atual é obrigatória")
        @PositiveOrZero(message = "Ocupação atual deve ser zero ou positiva")
        Integer ocupacaoAtual,

        @NotNull(message = "Temperatura é obrigatória")
        Double temperatura,

        @NotNull(message = "Id do abrigo é obrigatório")
        Long abrigoId
) {
}
