package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.request.VitimaRequest;
import br.com.fiap.smartdisaster.dto.response.VitimaResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Vitima;
import org.springframework.stereotype.Component;

@Component
public class VitimaMapper {

    public Vitima toEntity(VitimaRequest request, Abrigo abrigo) {
        Vitima vitima = new Vitima();
        vitima.setNome(request.nome());
        vitima.setCpf(request.cpf());
        vitima.setDataNascimento(request.dataNascimento());
        vitima.setCondicaoSaude(request.condicaoSaude());
        vitima.setAbrigo(abrigo);
        return vitima;
    }

    public VitimaResponse toResponse(Vitima vitima) {
        return new VitimaResponse(
                vitima.getId(),
                vitima.getNome(),
                vitima.getCpf(),
                vitima.getDataNascimento(),
                vitima.getCondicaoSaude(),
                vitima.getAbrigo().getId(),
                vitima.getAbrigo().getNome()
        );
    }

    public void updateEntity(Vitima vitima, VitimaRequest request, Abrigo abrigo) {
        vitima.setNome(request.nome());
        vitima.setCpf(request.cpf());
        vitima.setDataNascimento(request.dataNascimento());
        vitima.setCondicaoSaude(request.condicaoSaude());
        vitima.setAbrigo(abrigo);
    }
}
