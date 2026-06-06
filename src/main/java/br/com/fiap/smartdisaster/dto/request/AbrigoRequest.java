package br.com.fiap.smartdisaster.dto.request;

import br.com.fiap.smartdisaster.dto.EnderecoDTO;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AbrigoRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Capacidade máxima é obrigatória")
        @Positive(message = "Capacidade máxima deve ser positiva")
        Integer capacidadeMaxima,

        Double latitude,

        Double longitude,

        @NotNull(message = "Status é obrigatório")
        StatusAbrigo status,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoDTO endereco
) {
}
