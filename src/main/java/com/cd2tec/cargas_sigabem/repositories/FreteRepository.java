package com.cd2tec.cargas_sigabem.repositories;

import com.cd2tec.cargas_sigabem.models.Frete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {
}
