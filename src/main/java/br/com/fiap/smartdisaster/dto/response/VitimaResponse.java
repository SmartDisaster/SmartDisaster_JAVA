package br.com.fiap.smartdisaster.dto.response;

import java.time.LocalDate;

public record VitimaResponse(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String condicaoSaude,
        Long abrigoId,
        String abrigoNome
) {
}
