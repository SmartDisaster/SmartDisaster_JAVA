package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.AbrigoRequest;
import br.com.fiap.smartdisaster.dto.response.AbrigoResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;
import br.com.fiap.smartdisaster.exception.EntidadeNaoEncontradaException;
import br.com.fiap.smartdisaster.mapper.AbrigoMapper;
import br.com.fiap.smartdisaster.repository.AbrigoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;
    private final AbrigoMapper abrigoMapper;

    @Transactional(readOnly = true)
    public Page<AbrigoResponse> listar(Pageable pageable) {
        return abrigoRepository.findAll(pageable)
                .map(abrigoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public AbrigoResponse buscarPorId(Long id) {
        return abrigoMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public AbrigoResponse criar(AbrigoRequest request) {
        Abrigo abrigo = abrigoMapper.toEntity(request);
        return abrigoMapper.toResponse(abrigoRepository.save(abrigo));
    }

    @Transactional
    public AbrigoResponse atualizar(Long id, AbrigoRequest request) {
        Abrigo abrigo = buscarEntidade(id);
        abrigoMapper.updateEntity(abrigo, request);
        return abrigoMapper.toResponse(abrigoRepository.save(abrigo));
    }

    @Transactional
    public void remover(Long id) {
        Abrigo abrigo = buscarEntidade(id);
        abrigo.setStatus(StatusAbrigo.INATIVO);
        abrigoRepository.save(abrigo);
    }

    public Abrigo buscarEntidade(Long id) {
        return abrigoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Abrigo", id));
    }
}
