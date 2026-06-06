package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.Vitima;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VitimaRepository extends JpaRepository<Vitima, Long> {

    Page<Vitima> findByAbrigoId(Long abrigoId, Pageable pageable);

    Optional<Vitima> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
