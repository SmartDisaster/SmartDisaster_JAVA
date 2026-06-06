package br.com.fiap.smartdisaster.repository;

import br.com.fiap.smartdisaster.entity.SensorLeitura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorLeituraRepository extends JpaRepository<SensorLeitura, Long> {

    List<SensorLeitura> findByAbrigoIdOrderByTimestampDesc(Long abrigoId);

    Page<SensorLeitura> findAllByOrderByTimestampDesc(Pageable pageable);
}
