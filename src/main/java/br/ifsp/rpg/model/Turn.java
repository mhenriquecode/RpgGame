package br.ifsp.rpg.model;

public class Turn {
    private final RpgCharacter current;
    private final RpgCharacter opponent;
    private RollHitDice hitDice;

    public Turn(RpgCharacter current, RpgCharacter opponent) {
        this.current = current;
        this.opponent = opponent;
    }

    public void execute(int actionChoice) {
        if (actionChoice == 1) {
            attack();
        } else if (actionChoice == 2){
            defend();
        } else if (actionChoice == 3){
            dodge();
        }
    }

    public void attack() {
    }

    public void defend(){
    }
    public void dodge(){

    }

}
