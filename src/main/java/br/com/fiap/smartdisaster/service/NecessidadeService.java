package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.NecessidadeRequest;
import br.com.fiap.smartdisaster.dto.response.NecessidadeResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Necessidade;
import br.com.fiap.smartdisaster.exception.EntidadeNaoEncontradaException;
import br.com.fiap.smartdisaster.mapper.NecessidadeMapper;
import br.com.fiap.smartdisaster.repository.NecessidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NecessidadeService {

    private final NecessidadeRepository necessidadeRepository;
    private final AbrigoService abrigoService;
    private final NecessidadeMapper necessidadeMapper;

    @Transactional(readOnly = true)
    public Page<NecessidadeResponse> listar(Pageable pageable) {
        return necessidadeRepository.findAll(pageable).map(necessidadeMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NecessidadeResponse> listarPorAbrigo(Long abrigoId, Pageable pageable) {
        return necessidadeRepository.findByAbrigoId(abrigoId, pageable).map(necessidadeMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public NecessidadeResponse buscarPorId(Long id) {
        return necessidadeMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public NecessidadeResponse criar(NecessidadeRequest request) {
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        Necessidade necessidade = necessidadeMapper.toEntity(request, abrigo);
        return necessidadeMapper.toResponse(necessidadeRepository.save(necessidade));
    }

    @Transactional
    public NecessidadeResponse atualizar(Long id, NecessidadeRequest request) {
        Necessidade necessidade = buscarEntidade(id);
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        necessidadeMapper.updateEntity(necessidade, request, abrigo);
        return necessidadeMapper.toResponse(necessidadeRepository.save(necessidade));
    }

    @Transactional
    public void remover(Long id) {
        necessidadeRepository.delete(buscarEntidade(id));
    }

    public Necessidade buscarEntidade(Long id) {
        return necessidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Necessidade", id));
    }
}
