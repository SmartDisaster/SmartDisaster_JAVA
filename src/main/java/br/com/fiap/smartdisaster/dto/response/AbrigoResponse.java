package br.com.fiap.smartdisaster.dto.response;

import br.com.fiap.smartdisaster.dto.EnderecoDTO;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;

public record AbrigoResponse(
        Long id,
        String nome,
        Integer capacidadeMaxima,
        Double latitude,
        Double longitude,
        StatusAbrigo status,
        EnderecoDTO endereco
) {
}
