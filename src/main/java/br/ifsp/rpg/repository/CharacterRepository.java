package br.ifsp.rpg.repository;

import br.ifsp.rpg.model.RpgCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<RpgCharacter, UUID> {
}
