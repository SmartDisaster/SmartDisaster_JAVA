package br.com.fiap.smartdisaster.dto.response;

import br.com.fiap.smartdisaster.enums.StatusNecessidade;

public record NecessidadeResponse(
        Long id,
        String tipo,
        String descricao,
        Integer quantidadeNecessaria,
        StatusNecessidade status,
        Long abrigoId,
        String abrigoNome
) {
}
