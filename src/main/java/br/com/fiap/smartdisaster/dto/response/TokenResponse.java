package br.com.fiap.smartdisaster.dto.response;

public record TokenResponse(
        String token,
        String tipo,
        String email,
        String role
) {
}
