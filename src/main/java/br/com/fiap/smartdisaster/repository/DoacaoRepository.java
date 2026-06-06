package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.Doacao;
import br.com.fiap.smartdisaster.enums.StatusDoacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findByStatus(StatusDoacao status);

    List<Doacao> findByStatusAndTipo(StatusDoacao status, String tipo);

    Page<Doacao> findByAbrigoId(Long abrigoId, Pageable pageable);

    Page<Doacao> findByVoluntarioId(Long voluntarioId, Pageable pageable);
}
