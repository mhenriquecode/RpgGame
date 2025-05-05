package br.ifsp.web.repository;

import br.ifsp.web.log.CombatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CombatLogRepository extends JpaRepository<CombatLog, UUID> {
}

