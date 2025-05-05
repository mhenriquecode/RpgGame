package br.ifsp.web.log;

import br.ifsp.web.model.RpgCharacter;
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
    private RpgCharacter player1;

    @ManyToOne(optional = false)
    private RpgCharacter player2;

    @ManyToOne(optional = false)
    private RpgCharacter winner;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public CombatLog(RpgCharacter player1, RpgCharacter player2, RpgCharacter winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.timestamp = LocalDateTime.now();
    }
}

