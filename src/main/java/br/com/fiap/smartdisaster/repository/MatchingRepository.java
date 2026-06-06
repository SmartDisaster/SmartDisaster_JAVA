package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.MatchingDoacaoNecessidade;
import br.com.fiap.smartdisaster.entity.MatchingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<MatchingDoacaoNecessidade, MatchingId> {

    Page<MatchingDoacaoNecessidade> findAllByOrderByDataMatchingDesc(Pageable pageable);
}
