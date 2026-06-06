package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.request.SensorLeituraRequest;
import br.com.fiap.smartdisaster.dto.response.SensorLeituraResponse;
import br.com.fiap.smartdisaster.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
@Tag(name = "Sensor", description = "Leituras de sensores de abrigos — ativa regra de lotação automaticamente")
@SecurityRequirement(name = "bearerAuth")
public class SensorController {

    private final SensorService sensorService;

    @PostMapping("/leitura")
    @Operation(
            summary = "Registra leitura de sensor",
            description = "Salva a leitura e, se ocupação >= capacidade máxima, marca o abrigo como LOTADO automaticamente"
    )
    public ResponseEntity<EntityModel<SensorLeituraResponse>> registrarLeitura(
            @Valid @RequestBody SensorLeituraRequest request) {
        SensorLeituraResponse response = sensorService.registrarLeitura(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @GetMapping("/leitura")
    @Operation(summary = "Lista todas as leituras de sensores ordenadas pela mais recente")
    public ResponseEntity<CollectionModel<EntityModel<SensorLeituraResponse>>> listarLeituras(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<SensorLeituraResponse> page = sensorService.listarLeituras(pageable);

        List<EntityModel<SensorLeituraResponse>> models = page.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SensorLeituraResponse>> collection = CollectionModel.of(models,
                linkTo(methodOn(SensorController.class).listarLeituras(pageable)).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/leitura/abrigo/{id}")
    @Operation(summary = "Lista leituras de sensor de um abrigo específico")
    public ResponseEntity<CollectionModel<EntityModel<SensorLeituraResponse>>> listarPorAbrigo(
            @PathVariable Long id) {

        List<EntityModel<SensorLeituraResponse>> models = sensorService.listarPorAbrigo(id)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SensorLeituraResponse>> collection = CollectionModel.of(models,
                linkTo(methodOn(SensorController.class).listarPorAbrigo(id)).withSelfRel(),
                linkTo(methodOn(AbrigoController.class).buscarPorId(id)).withRel("abrigo"));

        return ResponseEntity.ok(collection);
    }

    private EntityModel<SensorLeituraResponse> toModel(SensorLeituraResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(SensorController.class).listarPorAbrigo(response.abrigoId())).withRel("leituras-abrigo"),
                linkTo(methodOn(AbrigoController.class).buscarPorId(response.abrigoId())).withRel("abrigo"));
    }
}
