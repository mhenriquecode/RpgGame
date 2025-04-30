package br.ifsp.rpg.model;

public class Turn {
    private final RpgCharacter current;
    private final RpgCharacter opponent;

    public Turn(RpgCharacter current, RpgCharacter opponent) {
        this.current = current;
        this.opponent = opponent;
    }

}
