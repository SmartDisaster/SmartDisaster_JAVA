package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.request.AbrigoRequest;
import br.com.fiap.smartdisaster.dto.response.AbrigoResponse;
import br.com.fiap.smartdisaster.service.AbrigoService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/abrigos")
@RequiredArgsConstructor
@Tag(name = "Abrigos", description = "Gerenciamento de abrigos de emergência")
@SecurityRequirement(name = "bearerAuth")
public class AbrigoController {

    private final AbrigoService abrigoService;

    @GetMapping
    @Operation(summary = "Lista todos os abrigos", description = "Retorna lista paginada com HATEOAS e page.totalElements")
    public ResponseEntity<PagedModel<EntityModel<AbrigoResponse>>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            PagedResourcesAssembler<AbrigoResponse> assembler) {

        Page<AbrigoResponse> page = abrigoService.listar(pageable);
        return ResponseEntity.ok(assembler.toModel(page, this::toModel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca abrigo por ID")
    public ResponseEntity<EntityModel<AbrigoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(abrigoService.buscarPorId(id)));
    }

    @PostMapping
    @Operation(summary = "Cria novo abrigo")
    public ResponseEntity<EntityModel<AbrigoResponse>> criar(@Valid @RequestBody AbrigoRequest request) {
        AbrigoResponse response = abrigoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza abrigo existente")
    public ResponseEntity<EntityModel<AbrigoResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AbrigoRequest request) {
        return ResponseEntity.ok(toModel(abrigoService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa abrigo (soft delete)", description = "Marca o abrigo como INATIVO sem remoção física")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        abrigoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<AbrigoResponse> toModel(AbrigoResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(AbrigoController.class).buscarPorId(response.id())).withSelfRel(),
                linkTo(methodOn(AbrigoController.class).atualizar(response.id(), null)).withRel("update"),
                linkTo(methodOn(AbrigoController.class).remover(response.id())).withRel("delete"));
    }
}
