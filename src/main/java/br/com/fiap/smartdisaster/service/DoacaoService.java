package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.DoacaoRequest;
import br.com.fiap.smartdisaster.dto.response.DoacaoResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Doacao;
import br.com.fiap.smartdisaster.entity.Necessidade;
import br.com.fiap.smartdisaster.entity.Voluntario;
import br.com.fiap.smartdisaster.enums.StatusDoacao;
import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import br.com.fiap.smartdisaster.exception.EntidadeNaoEncontradaException;
import br.com.fiap.smartdisaster.mapper.DoacaoMapper;
import br.com.fiap.smartdisaster.repository.DoacaoRepository;
import br.com.fiap.smartdisaster.repository.NecessidadeRepository;
import br.com.fiap.smartdisaster.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final NecessidadeRepository necessidadeRepository;
    private final AbrigoService abrigoService;
    private final DoacaoMapper doacaoMapper;

    @Transactional(readOnly = true)
    public Page<DoacaoResponse> listar(Pageable pageable) {
        return doacaoRepository.findAll(pageable).map(doacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public DoacaoResponse buscarPorId(Long id) {
        return doacaoMapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<DoacaoResponse> listarPorAbrigo(Long abrigoId, Pageable pageable) {
        return doacaoRepository.findByAbrigoId(abrigoId, pageable).map(doacaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DoacaoResponse> listarPorVoluntario(Long voluntarioId, Pageable pageable) {
        return doacaoRepository.findByVoluntarioId(voluntarioId, pageable).map(doacaoMapper::toResponse);
    }

    @Transactional
    public DoacaoResponse criar(DoacaoRequest request) {
        Voluntario voluntario = buscarVoluntario(request.voluntarioId());
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        Necessidade necessidade = buscarNecessidade(request.necessidadeId());
        Doacao doacao = doacaoMapper.toEntity(request, voluntario, abrigo, necessidade);
        return doacaoMapper.toResponse(doacaoRepository.save(doacao));
    }

    @Transactional
    public DoacaoResponse atualizar(Long id, DoacaoRequest request) {
        Doacao doacao = buscarEntidade(id);
        Voluntario voluntario = buscarVoluntario(request.voluntarioId());
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        Necessidade necessidade = buscarNecessidade(request.necessidadeId());
        doacaoMapper.updateEntity(doacao, request, voluntario, abrigo, necessidade);
        return doacaoMapper.toResponse(doacaoRepository.save(doacao));
    }

    @Transactional
    public DoacaoResponse marcarComoEntregue(Long id) {
        Doacao doacao = buscarEntidade(id);
        doacao.setStatus(StatusDoacao.ENTREGUE);

        Necessidade necessidade = doacao.getNecessidade();
        if (necessidade != null && doacao.getQuantidade() >= necessidade.getQuantidadeNecessaria()) {
            necessidade.setStatus(StatusNecessidade.ATENDIDA);
            necessidadeRepository.save(necessidade);
        }

        return doacaoMapper.toResponse(doacaoRepository.save(doacao));
    }

    @Transactional
    public DoacaoResponse cancelar(Long id) {
        Doacao doacao = buscarEntidade(id);
        doacao.setStatus(StatusDoacao.CANCELADA);
        return doacaoMapper.toResponse(doacaoRepository.save(doacao));
    }

    @Transactional
    public void remover(Long id) {
        doacaoRepository.delete(buscarEntidade(id));
    }

    public Doacao buscarEntidade(Long id) {
        return doacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Doação", id));
    }

    private Voluntario buscarVoluntario(Long id) {
        return voluntarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Voluntário", id));
    }

    private Necessidade buscarNecessidade(Long id) {
        return necessidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Necessidade", id));
    }
}
