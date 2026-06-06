package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.Abrigo;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbrigoRepository extends JpaRepository<Abrigo, Long> {

    Page<Abrigo> findByStatusNot(StatusAbrigo status, Pageable pageable);

    List<Abrigo> findByStatus(StatusAbrigo status);
}
