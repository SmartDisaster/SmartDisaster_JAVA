package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.request.SensorLeituraRequest;
import br.com.fiap.smartdisaster.dto.response.SensorLeituraResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.SensorLeitura;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SensorLeituraMapper {

    public SensorLeitura toEntity(SensorLeituraRequest request, Abrigo abrigo) {
        SensorLeitura leitura = new SensorLeitura();
        leitura.setOcupacaoAtual(request.ocupacaoAtual());
        leitura.setTemperatura(request.temperatura());
        leitura.setTimestamp(LocalDateTime.now());
        leitura.setAbrigo(abrigo);
        return leitura;
    }

    public SensorLeituraResponse toResponse(SensorLeitura leitura) {
        return new SensorLeituraResponse(
                leitura.getId(),
                leitura.getOcupacaoAtual(),
                leitura.getTemperatura(),
                leitura.getTimestamp(),
                leitura.getAbrigo().getId(),
                leitura.getAbrigo().getNome()
        );
    }
}
