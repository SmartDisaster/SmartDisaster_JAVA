package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.Necessidade;
import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NecessidadeRepository extends JpaRepository<Necessidade, Long> {

    List<Necessidade> findByStatus(StatusNecessidade status);

    List<Necessidade> findByStatusAndTipo(StatusNecessidade status, String tipo);

    Page<Necessidade> findByAbrigoId(Long abrigoId, Pageable pageable);
}
