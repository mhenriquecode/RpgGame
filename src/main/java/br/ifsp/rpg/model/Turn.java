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
        switch (actionChoice){
            case 1 -> attack();
            case 2 -> defend();
            case 3 -> dodge();
        }
    }

    public void attack() {
    }

    public void defend(){
    }
    public void dodge(){

    }

}
