package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.VitimaRequest;
import br.com.fiap.smartdisaster.dto.response.VitimaResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Vitima;
import br.com.fiap.smartdisaster.exception.EntidadeNaoEncontradaException;
import br.com.fiap.smartdisaster.exception.NegocioException;
import br.com.fiap.smartdisaster.mapper.VitimaMapper;
import br.com.fiap.smartdisaster.repository.VitimaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VitimaService {

    private final VitimaRepository vitimaRepository;
    private final AbrigoService abrigoService;
    private final VitimaMapper vitimaMapper;

    @Transactional(readOnly = true)
    public Page<VitimaResponse> listar(Long abrigoId, Pageable pageable) {
        if (abrigoId != null) {
            return vitimaRepository.findByAbrigoId(abrigoId, pageable)
                    .map(vitimaMapper::toResponse);
        }
        return vitimaRepository.findAll(pageable)
                .map(vitimaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public VitimaResponse buscarPorId(Long id) {
        return vitimaMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public VitimaResponse criar(VitimaRequest request) {
        if (vitimaRepository.existsByCpf(request.cpf())) {
            throw new NegocioException("CPF já cadastrado: " + request.cpf());
        }
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        Vitima vitima = vitimaMapper.toEntity(request, abrigo);
        return vitimaMapper.toResponse(vitimaRepository.save(vitima));
    }

    @Transactional
    public VitimaResponse atualizar(Long id, VitimaRequest request) {
        Vitima vitima = buscarEntidade(id);

        boolean cpfAlterado = !vitima.getCpf().equals(request.cpf());
        if (cpfAlterado && vitimaRepository.existsByCpf(request.cpf())) {
            throw new NegocioException("CPF já cadastrado: " + request.cpf());
        }

        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        vitimaMapper.updateEntity(vitima, request, abrigo);
        return vitimaMapper.toResponse(vitimaRepository.save(vitima));
    }

    @Transactional
    public void remover(Long id) {
        Vitima vitima = buscarEntidade(id);
        vitimaRepository.delete(vitima);
    }

    private Vitima buscarEntidade(Long id) {
        return vitimaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vítima", id));
    }
}
