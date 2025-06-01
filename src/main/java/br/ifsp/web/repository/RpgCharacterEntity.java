package br.ifsp.web.repository;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity(name = "characters")
@Getter
@Setter
@NoArgsConstructor(force = true)
public class RpgCharacterEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassType classType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weapon weapon;

    public RpgCharacterEntity(String name, ClassType classType, Race race, Weapon weapon) {
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
    }

}