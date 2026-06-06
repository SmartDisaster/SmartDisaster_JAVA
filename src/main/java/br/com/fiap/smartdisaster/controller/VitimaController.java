package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.request.VitimaRequest;
import br.com.fiap.smartdisaster.dto.response.VitimaResponse;
import br.com.fiap.smartdisaster.service.VitimaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/vitimas")
@RequiredArgsConstructor
@Tag(name = "Vítimas", description = "Gerenciamento de vítimas vinculadas a abrigos")
@SecurityRequirement(name = "bearerAuth")
public class VitimaController {

    private final VitimaService vitimaService;

    @GetMapping
    @Operation(summary = "Lista vítimas", description = "Retorna lista paginada; filtra por abrigo se abrigoId for informado")
    public ResponseEntity<PagedModel<EntityModel<VitimaResponse>>> listar(
            @Parameter(description = "Filtrar por ID do abrigo")
            @RequestParam(required = false) Long abrigoId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            PagedResourcesAssembler<VitimaResponse> assembler) {

        Page<VitimaResponse> page = vitimaService.listar(abrigoId, pageable);
        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca vítima por ID")
    public ResponseEntity<EntityModel<VitimaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(vitimaService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Cadastra nova vítima")
    public ResponseEntity<EntityModel<VitimaResponse>> criar(@Valid @RequestBody VitimaRequest request) {
        VitimaResponse response = vitimaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza vítima existente")
    public ResponseEntity<EntityModel<VitimaResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VitimaRequest request) {
        return ResponseEntity.ok(toModel(vitimaService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove vítima")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        vitimaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<VitimaResponse> toModel(VitimaResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(VitimaController.class).buscarPorId(response.id())).withSelfRel(),
                linkTo(methodOn(VitimaController.class).atualizar(response.id(), null)).withRel("update"),
                linkTo(methodOn(VitimaController.class).remover(response.id())).withRel("delete"),
                linkTo(methodOn(AbrigoController.class).buscarPorId(response.abrigoId())).withRel("abrigo"));
    }
}
