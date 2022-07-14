package ch.usi.si.bsc.sa4.devinecodemy.model.statistics;

import ch.usi.si.bsc.sa4.devinecodemy.model.exceptions.StatisticInexistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("The LevelStatistics")
public class LevelStatisticsTests {

    private LevelStatistics levelStatistics1;

    @BeforeEach
    void setup() {
        levelStatistics1 = new LevelStatistics(false);
    }

    @DisplayName("after creation")
    @Nested
    class AfterCreationTests {

        @Test
        @DisplayName("it should be possible to check if the level is completed")
        public void testIsCompleted() {
            var actualCompletion = levelStatistics1.isCompleted();
            var expectedCompletion = false;

            assertEquals(expectedCompletion,actualCompletion,
                    "completed is not the same as in the constructor");
        }

        @Test
        @DisplayName("it should be possible to get an empty list of attempts")
        public void testGetAllAttempts() {
            var actualSize = levelStatistics1.getAllAttempts().size();
            var expectedSize = 0;

            assertEquals(expectedSize,actualSize, "data is not empty");
        }

        @Test
        @DisplayName("it should not be possible to get a not played attempts")
        public void testGetAttemptEmpty() {
            Executable actual = () -> levelStatistics1.getAttempt(0);

            assertThrows(StatisticInexistentException.class, actual,
                    "asking for a not played attempt should throw an exception");
        }

        @Test
        @DisplayName("it should be possible to set a list of attempts")
        public void testSetData() {
            List<String> data = List.of("string1", "string2");
            levelStatistics1.setData(data);
        }

        @Test
        @DisplayName("it should be possible to set a new completed status")
        public void testSetCompleted() {
            boolean completed = true;
            levelStatistics1.setCompleted(completed);
        }

        @Test
        @DisplayName("it should be possible to add a new attempt")
        public void testAdd() {
            levelStatistics1.add("attempt1");
        }
    }

    @DisplayName("after an attempt")
    @Nested
    class AfterAddingTests {

        @BeforeEach
        public void setup() {
            levelStatistics1.add("moveForward");
        }

        @Test
        @DisplayName("it should be possible to retrieve it")
        public void testGetAttemptFullCorrect() {
            var actualAttempt = levelStatistics1.getAttempt(0);
            var expectedAttempt = "moveForward";
            assertEquals(expectedAttempt,actualAttempt,
                    "the attempt is not the one played");
        }

        @Test
        @DisplayName("it should be possible to retrieve the size of the data")
        public void testGetAttemptFullSpecial() {
            var actualAttempt = levelStatistics1.getAttempt(-1);
            var expectedAttempt = "moveForward";
            assertEquals(expectedAttempt,actualAttempt,
                    "the attempt is not the one played");
        }
    }
}
