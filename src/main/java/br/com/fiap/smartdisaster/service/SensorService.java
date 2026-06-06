package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.request.SensorLeituraRequest;
import br.com.fiap.smartdisaster.dto.response.SensorLeituraResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.SensorLeitura;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;
import br.com.fiap.smartdisaster.mapper.SensorLeituraMapper;
import br.com.fiap.smartdisaster.repository.AbrigoRepository;
import br.com.fiap.smartdisaster.repository.SensorLeituraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorLeituraRepository sensorLeituraRepository;
    private final AbrigoRepository abrigoRepository;
    private final AbrigoService abrigoService;
    private final SensorLeituraMapper sensorLeituraMapper;

    /**
     * Regra 1: se ocupacaoAtual >= capacidadeMaxima do abrigo, atualiza status para LOTADO.
     */
    @Transactional
    public SensorLeituraResponse registrarLeitura(SensorLeituraRequest request) {
        Abrigo abrigo = abrigoService.buscarEntidade(request.abrigoId());
        SensorLeitura leitura = sensorLeituraMapper.toEntity(request, abrigo);
        SensorLeitura salva = sensorLeituraRepository.save(leitura);

        if (request.ocupacaoAtual() >= abrigo.getCapacidadeMaxima()) {
            abrigo.setStatus(StatusAbrigo.LOTADO);
            abrigoRepository.save(abrigo);
        }

        return sensorLeituraMapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public Page<SensorLeituraResponse> listarLeituras(Pageable pageable) {
        return sensorLeituraRepository.findAllByOrderByTimestampDesc(pageable)
                .map(sensorLeituraMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SensorLeituraResponse> listarPorAbrigo(Long abrigoId) {
        abrigoService.buscarEntidade(abrigoId);
        return sensorLeituraRepository.findByAbrigoIdOrderByTimestampDesc(abrigoId)
                .stream()
                .map(sensorLeituraMapper::toResponse)
                .collect(Collectors.toList());
    }
}
