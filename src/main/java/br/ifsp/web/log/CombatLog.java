package br.ifsp.web.log;

import br.ifsp.web.repository.RpgCharacterEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CombatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private RpgCharacterEntity player1;

    @ManyToOne(optional = false)
    private RpgCharacterEntity player2;

    @ManyToOne(optional = false)
    private RpgCharacterEntity winner;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public CombatLog(RpgCharacterEntity player1, RpgCharacterEntity player2, RpgCharacterEntity winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.timestamp = LocalDateTime.now();
    }
}
