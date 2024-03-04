import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.ifmo.se.task2.BPlusTree;

public class BPlusTreeTest {
    private BPlusTree tree;

    @BeforeEach
    public void setUp() {
        tree = new BPlusTree();
    }

    @Test
    @DisplayName("Insert sequential keys and verify tree root keys")
    public void shouldInsertSequentialKeysCorrectly() {
        // Arrange
        int[] expectedKeys = {5, 10, 15, 0, 0, 0, 0};

        // Act
        insertKeys(5, 10, 15);

        // Assert
        Assertions.assertArrayEquals(expectedKeys, tree.getRoot().getKeys());
    }

    @Test
    @DisplayName("Maintain key order after mixed insertions")
    public void shouldMaintainKeyOrderAfterInsertions() {
        // Arrange
        int[] expectedKeys = {1, 5, 10, 0, 0, 0, 0};

        // Act
        insertKeys(5, 10, 1);

        // Assert
        Assertions.assertArrayEquals(expectedKeys, tree.getRoot().getKeys());
    }

    @Test
    @DisplayName("Handle overflow by splitting nodes correctly")
    public void shouldHandleOverflowBySplittingCorrectly() {
        // Arrange
        int[] expectedRootKeys = {4, 0, 0, 0, 0, 0, 0};
        int[] expectedChildKeys = {1, 2, 3, 0, 0, 0, 0};
        int[] expectedSiblingKeys = {5, 6, 7, 8, 0, 0, 0};

        // Act
        insertKeys(1, 2, 3, 4, 5, 6, 7, 8);

        // Assert
        Assertions.assertArrayEquals(expectedRootKeys, tree.getRoot().getKeys());
        Assertions.assertArrayEquals(expectedChildKeys, tree.getRoot().getChildren()[0].getKeys());
        Assertions.assertArrayEquals(expectedSiblingKeys, tree.getRoot().getChildren()[1].getKeys());
    }

    private void insertKeys(int... keys) {
        for (int key : keys) {
            tree.insert(key);
        }
    }
}
