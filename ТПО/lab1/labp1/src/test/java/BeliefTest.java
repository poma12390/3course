import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.ifmo.se.task3.*;

class BeliefTest {
    private Something thing;
    private Chance chance;
    private Rarity rarity;
    private Proof proof;
    private Belief belief;
    private Thinker thinker;

    @BeforeEach
    void setUp() {
        thing = new Something("Яблоко", 5);
        chance = new Chance("Невероятное совпадение", RarityLevel.EPIC);
        rarity = new Rarity(chance, thing);
        proof = new Proof(rarity);
    }

    @Test
    @DisplayName("Verify belief with proof of rarity level is correct")
    void beliefWithProofOfRarityLevelIsCorrect() {
        // Arrange
        belief = new Belief(false, proof);
        thinker = new Thinker("Джон Доу", belief);

        // Act + Assert
        Assertions.assertEquals(RarityLevel.EPIC,
                thinker.getBelief().getProof().getRarity().getChance().getRarityLevel());
        Assertions.assertFalse(thinker.getBelief().isExistsGod());
    }

    @Test
    @DisplayName("Change of belief about existence of God is reflected")
    void changeOfBeliefAboutExistenceOfGodIsReflected() {
        // Arrange
        belief = new Belief(true, proof);
        thinker = new Thinker("Джон Доу", belief);

        // Act
        thinker.getBelief().setExistsGod(false);

        // Assert
        Assertions.assertFalse(thinker.getBelief().isExistsGod());
    }

    @Test
    @DisplayName("Verify usefulness of the thing in belief's proof is correct")
    void usefulnessOfThingInBeliefsProofIsCorrect() {
        // Arrange
        thing = new Something("Яблоко", 7);
        proof = new Proof(new Rarity(chance, thing));
        belief = new Belief(true, proof);
        thinker = new Thinker("Джон Доу", belief);

        // Act + Assert
        Assertions.assertEquals(7, thinker.getBelief().getProof().getRarity().getUsefulThing().getUsefulness());
    }

    @Test
    @DisplayName("Rename of thing in belief's proof is reflected")
    void renameOfThingInBeliefsProofIsReflected() {
        // Arrange
        belief = new Belief(true, proof);
        thinker = new Thinker("Джон Доу", belief);

        // Act
        thinker.getBelief().getProof().getRarity().getUsefulThing().setName("Груша");

        // Assert
        Assertions.assertEquals("Груша", thinker.getBelief().getProof().getRarity().getUsefulThing().getName());
    }
}
