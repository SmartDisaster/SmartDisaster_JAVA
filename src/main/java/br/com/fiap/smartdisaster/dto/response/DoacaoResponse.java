package br.com.fiap.smartdisaster.dto.response;

import br.com.fiap.smartdisaster.enums.StatusDoacao;

import java.time.LocalDate;

public record DoacaoResponse(
        Long id,
        String tipo,
        String descricao,
        Integer quantidade,
        LocalDate dataDoacao,
        StatusDoacao status,
        Long voluntarioId,
        String voluntarioNome,
        Long abrigoId,
        String abrigoNome,
        Long necessidadeId,
        String necessidadeDescricao
) {
}
