package br.com.fiap.smartdisaster.dto.request;

import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NecessidadeRequest(
        @NotBlank(message = "Tipo é obrigatório")
        String tipo,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "Quantidade necessária é obrigatória")
        @Positive(message = "Quantidade necessária deve ser positiva")
        Integer quantidadeNecessaria,

        @NotNull(message = "Status é obrigatório")
        StatusNecessidade status,

        @NotNull(message = "Id do abrigo é obrigatório")
        Long abrigoId
) {
}
