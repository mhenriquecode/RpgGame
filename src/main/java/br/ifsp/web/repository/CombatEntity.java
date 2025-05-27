package br.ifsp.web.repository;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.RpgCharacter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
public class CombatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private RpgCharacterEntity player1;

    @ManyToOne(optional = false)
    private RpgCharacterEntity player2;

    @ManyToOne
    private RpgCharacterEntity winner;

    @Transient
    private ChooseAction actionStrategy1;

    @Transient
    private ChooseAction actionStrategy2;
}