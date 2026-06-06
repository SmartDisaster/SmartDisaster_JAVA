package br.com.fiap.smartdisaster.mapper;

import br.com.fiap.smartdisaster.dto.EnderecoDTO;
import br.com.fiap.smartdisaster.dto.request.AbrigoRequest;
import br.com.fiap.smartdisaster.dto.response.AbrigoResponse;
import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class AbrigoMapper {

    public Abrigo toEntity(AbrigoRequest request) {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome(request.nome());
        abrigo.setCapacidadeMaxima(request.capacidadeMaxima());
        abrigo.setLatitude(request.latitude());
        abrigo.setLongitude(request.longitude());
        abrigo.setStatus(request.status());
        abrigo.setEndereco(toEndereco(request.endereco()));
        return abrigo;
    }

    public AbrigoResponse toResponse(Abrigo abrigo) {
        return new AbrigoResponse(
                abrigo.getId(),
                abrigo.getNome(),
                abrigo.getCapacidadeMaxima(),
                abrigo.getLatitude(),
                abrigo.getLongitude(),
                abrigo.getStatus(),
                abrigo.getEndereco() != null ? toEnderecoDTO(abrigo.getEndereco()) : null
        );
    }

    public void updateEntity(Abrigo abrigo, AbrigoRequest request) {
        abrigo.setNome(request.nome());
        abrigo.setCapacidadeMaxima(request.capacidadeMaxima());
        abrigo.setLatitude(request.latitude());
        abrigo.setLongitude(request.longitude());
        abrigo.setStatus(request.status());
        abrigo.setEndereco(toEndereco(request.endereco()));
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return Endereco.builder()
                .rua(dto.rua())
                .numero(dto.numero())
                .bairro(dto.bairro())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .cep(dto.cep())
                .build();
    }

    private EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }
}
