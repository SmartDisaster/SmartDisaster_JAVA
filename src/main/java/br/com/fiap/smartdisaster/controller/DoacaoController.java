package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.request.DoacaoRequest;
import br.com.fiap.smartdisaster.dto.response.DoacaoResponse;
import br.com.fiap.smartdisaster.service.DoacaoService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/doacoes")
@RequiredArgsConstructor
@Tag(name = "Doações", description = "Fluxo de doações vinculadas a abrigos e necessidades")
@SecurityRequirement(name = "bearerAuth")
public class DoacaoController {

    private final DoacaoService doacaoService;

    @GetMapping
    @Operation(summary = "Lista todas as doações")
    public ResponseEntity<PagedModel<EntityModel<DoacaoResponse>>> listar(
            @PageableDefault(size = 10, sort = "dataDoacao") Pageable pageable,
            PagedResourcesAssembler<DoacaoResponse> assembler) {
        Page<DoacaoResponse> page = doacaoService.listar(pageable);
        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca doação por ID")
    public ResponseEntity<EntityModel<DoacaoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(doacaoService.buscarPorId(id)));
    }

    @GetMapping("/abrigo/{abrigoId}")
    @Operation(summary = "Lista doações de um abrigo")
    public ResponseEntity<PagedModel<EntityModel<DoacaoResponse>>> listarPorAbrigo(
            @PathVariable Long abrigoId,
            @PageableDefault(size = 20, sort = "dataDoacao") Pageable pageable,
            PagedResourcesAssembler<DoacaoResponse> assembler) {
        Page<DoacaoResponse> page = doacaoService.listarPorAbrigo(abrigoId, pageable);
        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @GetMapping("/voluntario/{voluntarioId}")
    @Operation(summary = "Lista doações de um voluntário")
    public ResponseEntity<PagedModel<EntityModel<DoacaoResponse>>> listarPorVoluntario(
            @PathVariable Long voluntarioId,
            @PageableDefault(size = 20, sort = "dataDoacao") Pageable pageable,
            PagedResourcesAssembler<DoacaoResponse> assembler) {
        Page<DoacaoResponse> page = doacaoService.listarPorVoluntario(voluntarioId, pageable);
        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @PostMapping
    @Operation(summary = "Registra nova doação")
    public ResponseEntity<EntityModel<DoacaoResponse>> criar(@Valid @RequestBody DoacaoRequest request) {
        DoacaoResponse response = doacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza doação existente")
    public ResponseEntity<EntityModel<DoacaoResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DoacaoRequest request) {
        return ResponseEntity.ok(toModel(doacaoService.atualizar(id, request)));
    }

    @PatchMapping("/{id}/entregar")
    @Operation(summary = "Marca doação como entregue e aplica regra de necessidade")
    public ResponseEntity<EntityModel<DoacaoResponse>> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(doacaoService.marcarComoEntregue(id)));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancela uma doação")
    public ResponseEntity<EntityModel<DoacaoResponse>> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(doacaoService.cancelar(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove doação")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        doacaoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<DoacaoResponse> toModel(DoacaoResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(DoacaoController.class).buscarPorId(response.id())).withSelfRel(),
                linkTo(methodOn(DoacaoController.class).entregar(response.id())).withRel("entregar"),
                linkTo(methodOn(DoacaoController.class).cancelar(response.id())).withRel("cancelar"),
                linkTo(methodOn(DoacaoController.class).remover(response.id())).withRel("delete"));
    }
}
