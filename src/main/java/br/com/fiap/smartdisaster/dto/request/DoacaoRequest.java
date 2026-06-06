package br.com.fiap.smartdisaster.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DoacaoRequest(
        @NotBlank(message = "Tipo é obrigatório")
        String tipo,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        Integer quantidade,

        @NotNull(message = "Id do abrigo é obrigatório")
        Long abrigoId,

        @NotNull(message = "Id da necessidade é obrigatório")
        Long necessidadeId,

        @NotNull(message = "Id do voluntário é obrigatório")
        Long voluntarioId
) {
}
