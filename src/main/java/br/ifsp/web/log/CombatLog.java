package br.ifsp.web.log;

import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_entity_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private RpgCharacterEntity player1Entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_entity_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private RpgCharacterEntity player2Entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_entity_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private RpgCharacterEntity winnerEntity;

    @Column(nullable = false)
    private UUID player1Id;

    @Column(nullable = false)
    private String player1Name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassType player1ClassType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race player1Race;

    @Column(nullable = false)
    private UUID player2Id;

    @Column(nullable = false)
    private String player2Name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassType player2ClassType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race player2Race;


    @Column(nullable = false)
    private UUID winnerId;

    @Column(nullable = false)
    private String winnerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassType winnerClassType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race winnerRace;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public CombatLog(RpgCharacterEntity player1, RpgCharacterEntity player2, RpgCharacterEntity winner) {
        this.player1Entity = player1;
        this.player2Entity = player2;
        this.winnerEntity = winner;

        this.player1Id = player1.getId();
        this.player1Name = player1.getName();
        this.player1ClassType = player1.getClassType();
        this.player1Race = player1.getRace();

        this.player2Id = player2.getId();
        this.player2Name = player2.getName();
        this.player2ClassType = player2.getClassType();
        this.player2Race = player2.getRace();

        this.winnerId = winner.getId();
        this.winnerName = winner.getName();
        this.winnerClassType = winner.getClassType();
        this.winnerRace = winner.getRace();

        this.timestamp = LocalDateTime.now();
    }
}
