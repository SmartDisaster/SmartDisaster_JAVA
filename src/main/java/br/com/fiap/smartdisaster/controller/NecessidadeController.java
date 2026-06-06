package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.request.NecessidadeRequest;
import br.com.fiap.smartdisaster.dto.response.NecessidadeResponse;
import br.com.fiap.smartdisaster.service.NecessidadeService;
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
@RequestMapping("/necessidades")
@RequiredArgsConstructor
@Tag(name = "Necessidades", description = "Gerenciamento de necessidades de abrigos")
@SecurityRequirement(name = "bearerAuth")
public class NecessidadeController {

    private final NecessidadeService necessidadeService;

    @GetMapping
    @Operation(summary = "Lista necessidades. Filtra por abrigoId se fornecido")
    public ResponseEntity<PagedModel<EntityModel<NecessidadeResponse>>> listar(
            @RequestParam(required = false) Long abrigoId,
            @PageableDefault(size = 20, sort = "tipo") Pageable pageable,
            PagedResourcesAssembler<NecessidadeResponse> assembler) {

        Page<NecessidadeResponse> page = (abrigoId != null)
                ? necessidadeService.listarPorAbrigo(abrigoId, pageable)
                : necessidadeService.listar(pageable);

        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca necessidade por ID")
    public ResponseEntity<EntityModel<NecessidadeResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(necessidadeService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Registra nova necessidade")
    public ResponseEntity<EntityModel<NecessidadeResponse>> criar(
            @Valid @RequestBody NecessidadeRequest request) {
        NecessidadeResponse response = necessidadeService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza necessidade existente")
    public ResponseEntity<EntityModel<NecessidadeResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NecessidadeRequest request) {
        return ResponseEntity.ok(toModel(necessidadeService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove necessidade")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        necessidadeService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<NecessidadeResponse> toModel(NecessidadeResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(NecessidadeController.class).buscarPorId(response.id())).withSelfRel(),
                linkTo(methodOn(NecessidadeController.class).atualizar(response.id(), null)).withRel("update"),
                linkTo(methodOn(NecessidadeController.class).remover(response.id())).withRel("delete"),
                linkTo(methodOn(AbrigoController.class).buscarPorId(response.abrigoId())).withRel("abrigo"));
    }
}
