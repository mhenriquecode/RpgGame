package br.ifsp.web.repository;

import br.ifsp.web.model.RpgCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<RpgCharacterEntity, UUID> {
}
