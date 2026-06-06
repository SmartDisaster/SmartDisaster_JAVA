package br.com.fiap.smartdisaster.controller;

import br.com.fiap.smartdisaster.dto.response.MatchingResponse;
import br.com.fiap.smartdisaster.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Engine de correspondência entre doações e necessidades")
@SecurityRequirement(name = "bearerAuth")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/executar")
    @Operation(
            summary = "Executa o engine de matching",
            description = "Combina doações PENDENTE_ENTREGA com necessidades PENDENTE do mesmo tipo. "
                    + "Atualiza status para ENTREGUE/ATENDIDA e persiste os matchings realizados."
    )
    public ResponseEntity<CollectionModel<EntityModel<MatchingResponse>>> executarMatching() {
        List<MatchingResponse> realizados = matchingService.executarMatching();

        List<EntityModel<MatchingResponse>> models = realizados.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<MatchingResponse>> collection = CollectionModel.of(models,
                linkTo(methodOn(MatchingController.class).executarMatching()).withSelfRel(),
                linkTo(methodOn(MatchingController.class).listarMatchings(Pageable.unpaged())).withRel("historico"));

        return ResponseEntity.ok(collection);
    }

    @GetMapping
    @Operation(summary = "Lista histórico de matchings realizados")
    public ResponseEntity<CollectionModel<EntityModel<MatchingResponse>>> listarMatchings(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<MatchingResponse> page = matchingService.listarMatchings(pageable);

        List<EntityModel<MatchingResponse>> models = page.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<MatchingResponse>> collection = CollectionModel.of(models,
                linkTo(methodOn(MatchingController.class).listarMatchings(pageable)).withSelfRel(),
                linkTo(methodOn(MatchingController.class).executarMatching()).withRel("executar"));

        return ResponseEntity.ok(collection);
    }

    private EntityModel<MatchingResponse> toModel(MatchingResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(DoacaoController.class).buscarPorId(response.doacaoId())).withRel("doacao"),
                linkTo(methodOn(NecessidadeController.class).buscarPorId(response.necessidadeId())).withRel("necessidade"));
    }
}
