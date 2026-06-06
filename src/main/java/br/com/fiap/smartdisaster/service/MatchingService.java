package br.com.fiap.smartdisaster.service;

import br.com.fiap.smartdisaster.dto.response.MatchingResponse;
import br.com.fiap.smartdisaster.entity.Doacao;
import br.com.fiap.smartdisaster.entity.MatchingDoacaoNecessidade;
import br.com.fiap.smartdisaster.entity.MatchingId;
import br.com.fiap.smartdisaster.entity.Necessidade;
import br.com.fiap.smartdisaster.enums.StatusDoacao;
import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import br.com.fiap.smartdisaster.mapper.MatchingMapper;
import br.com.fiap.smartdisaster.repository.DoacaoRepository;
import br.com.fiap.smartdisaster.repository.MatchingRepository;
import br.com.fiap.smartdisaster.repository.NecessidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final DoacaoRepository doacaoRepository;
    private final NecessidadeRepository necessidadeRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingMapper matchingMapper;

    /**
     * Regra 2: localiza doações PENDENTE_ENTREGA e necessidades PENDENTE do mesmo tipo.
     * Cria o matching, atualiza doação para ENTREGUE e necessidade para ATENDIDA.
     */
    @Transactional
    public List<MatchingResponse> executarMatching() {
        List<Doacao> doacoesDisponiveis = doacaoRepository.findByStatus(StatusDoacao.PENDENTE_ENTREGA);
        List<MatchingDoacaoNecessidade> matchingsRealizados = new ArrayList<>();

        for (Doacao doacao : doacoesDisponiveis) {
            List<Necessidade> necessidadesCompatíveis = necessidadeRepository
                    .findByStatusAndTipo(StatusNecessidade.PENDENTE, doacao.getTipo());

            for (Necessidade necessidade : necessidadesCompatíveis) {
                MatchingId matchingId = new MatchingId(doacao.getId(), necessidade.getId());
                boolean jaExiste = matchingRepository.existsById(matchingId);

                if (!jaExiste) {
                    MatchingDoacaoNecessidade matching = new MatchingDoacaoNecessidade(doacao, necessidade);
                    matchingRepository.save(matching);
                    matchingsRealizados.add(matching);

                    doacao.setStatus(StatusDoacao.ENTREGUE);
                    doacaoRepository.save(doacao);

                    necessidade.setStatus(StatusNecessidade.ATENDIDA);
                    necessidadeRepository.save(necessidade);

                    break;
                }
            }
        }

        return matchingsRealizados.stream()
                .map(matchingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MatchingResponse> listarMatchings(Pageable pageable) {
        return matchingRepository.findAllByOrderByDataMatchingDesc(pageable)
                .map(matchingMapper::toResponse);
    }
}
