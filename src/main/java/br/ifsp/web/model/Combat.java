package br.ifsp.web.model;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.actions.ChooseUserAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Combat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private RpgCharacter player1;

    @ManyToOne(optional = false)
    private RpgCharacter player2;

    @ManyToOne
    private RpgCharacter winner;

    @Transient
    private ChooseAction actionStrategy1;

    @Transient
    private ChooseAction actionStrategy2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Transient
    private Random random = new Random();

    public Combat(RpgCharacter player1, ChooseAction actionStrategy1,
                  RpgCharacter player2, ChooseAction actionStrategy2) {
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.actionStrategy1 = actionStrategy1;
        this.actionStrategy2 = actionStrategy2;
        this.random = new Random();
    }

    public Combat(RpgCharacter player1, RpgCharacter player2, Random random) {
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.random = random;
    }
}
