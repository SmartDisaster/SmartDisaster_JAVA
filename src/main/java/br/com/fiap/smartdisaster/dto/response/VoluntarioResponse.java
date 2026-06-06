package br.com.fiap.smartdisaster.dto.response;

public record VoluntarioResponse(
        Long id,
        String nome,
        String email,
        String telefone
) {
}
