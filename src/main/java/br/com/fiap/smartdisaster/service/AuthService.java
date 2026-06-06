package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.LoginRequest;
import br.com.fiap.smartdisaster.dto.request.RegisterRequest;
import br.com.fiap.smartdisaster.dto.response.TokenResponse;
import br.com.fiap.smartdisaster.entity.Admin;
import br.com.fiap.smartdisaster.entity.Usuario;
import br.com.fiap.smartdisaster.entity.Voluntario;
import br.com.fiap.smartdisaster.enums.Role;
import br.com.fiap.smartdisaster.exception.NegocioException;
import br.com.fiap.smartdisaster.repository.UsuarioRepository;
import br.com.fiap.smartdisaster.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenProvider.generateToken(usuario);

        return new TokenResponse(token, "Bearer", usuario.getEmail(), usuario.getRole().name());
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new NegocioException("Email já cadastrado: " + request.email());
        }

        if (request.role() == Role.VOLUNTARIO && (request.telefone() == null || request.telefone().isBlank())) {
            throw new NegocioException("Telefone é obrigatório para voluntários.");
        }

        Usuario usuario = construirUsuario(request);
        usuarioRepository.save(usuario);

        String token = tokenProvider.generateToken(usuario);
        return new TokenResponse(token, "Bearer", usuario.getEmail(), usuario.getRole().name());
    }

    private Usuario construirUsuario(RegisterRequest request) {
        String senhaCriptografada = passwordEncoder.encode(request.senha());

        if (request.role() == Role.ADMIN) {
            Admin admin = new Admin();
            admin.setNome(request.nome());
            admin.setEmail(request.email());
            admin.setSenha(senhaCriptografada);
            return admin;
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setNome(request.nome());
        voluntario.setEmail(request.email());
        voluntario.setSenha(senhaCriptografada);
        voluntario.setTelefone(request.telefone());
        return voluntario;
    }
}
