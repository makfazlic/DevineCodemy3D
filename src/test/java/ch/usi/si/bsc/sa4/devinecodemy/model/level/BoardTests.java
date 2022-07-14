package ch.usi.si.bsc.sa4.devinecodemy.model.level;

import ch.usi.si.bsc.sa4.devinecodemy.model.EOrientation;
import ch.usi.si.bsc.sa4.devinecodemy.model.item.CoinItem;
import ch.usi.si.bsc.sa4.devinecodemy.model.item.Item;
import ch.usi.si.bsc.sa4.devinecodemy.model.tile.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.util.Pair;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("The board")
public class BoardTests {

    private Board board;
    private static TeleportTile teleport1;
    private static TeleportTile teleport2;

    public static Stream<Arguments> getItemAtTestsArgumentProvider() {
        return Stream.of(
                arguments(-1, -2, true, null), // test when both are negative
                arguments(34, 333, true, null), // test when both are out of bounds
                arguments(-1, 0, true, null), // test when x is negative
                arguments(0, -5, true, null), // test when y is negative
                arguments(20, 0, true, null), // test when x is out of bounds
                arguments(0, 29, true, null), // test when y is out of bounds
                arguments(3, 4, false, null), // test when x and y are both within bounds but no item (x does not match)
                arguments(7, 4, false, null), // test when x and y are both within bounds but no item (x matches)
                arguments(4, 7, false, new CoinItem(4, 7)) // test when x and y are both within bounds and yes item
        );
    }

    @ParameterizedTest(name = "getting the item at x {0} and y {1} should {2} throw and return item {3}")
    @MethodSource("getItemAtTestsArgumentProvider")
    void getItemAtTest(int x, int y, boolean shouldThrow, Item expectedItem) {
        if (shouldThrow) {
            assertThrows(Exception.class, () -> board.getItemAt(x, y), "method should throw");
        } else {
            assertDoesNotThrow(() -> board.getItemAt(x, y), "method should not throw");
            final Item actualItem = board.getItemAt(x, y);
            if (expectedItem == null) {
                assertNull(actualItem);
            } else {
                assertEquals(expectedItem.getPosX(), actualItem.getPosX(), "item does not have correct x");
                assertEquals(expectedItem.getPosY(), actualItem.getPosY(), "item does not have correct y");
            }
        }
    }

    public static Stream<Arguments> getTileAtTestsArgumentProvider() {
        return Stream.of(
                arguments(-1, -2, true, Pair.of(0, 0)), // test when both are negative
                arguments(34, 333, true, Pair.of(0, 0)), // test when both are out of bounds
                arguments(-1, 0, true, Pair.of(0, 0)), // test when x is negative
                arguments(0, -5, true, Pair.of(0, 0)), // test when y is negative
                arguments(20, 0, true, Pair.of(0, 0)), // test when x is out of bounds
                arguments(0, 29, true, Pair.of(0, 0)), // test when y is out of bounds
                arguments(3, 4, false, Pair.of(3, 4)), // test when x and y are both within bounds
                arguments(5, 8, false, null) // test when x and y are both within bounds but no tile
        );
    }

    @ParameterizedTest(name = "getting the tile at x {0} and y {1} should {2} throw and return tile with xy {3}")
    @MethodSource("getTileAtTestsArgumentProvider")
    void getTileAtTest(int x, int y, boolean shouldThrow, Pair<Integer, Integer> xy) {
        if (shouldThrow) {
            assertThrows(Exception.class, () -> board.getTileAt(x, y), "method should throw");
        } else {
            assertDoesNotThrow(() -> board.getTileAt(x, y), "method should not throw");
            final Tile actualTile = board.getTileAt(x, y);
            if (xy == null) {
                assertNull(actualTile, "tile should be null");
            } else {
                assertEquals(xy.getFirst(), actualTile.getPosX(), "tile does not have correct x");
                assertEquals(xy.getSecond(), actualTile.getPosY(), "tile does not have correct y");
            }
        }
    }

    public static Stream<Arguments> getNextTileTestsArgumentProvider() {
        return Stream.of(
                arguments(-1, -2, null, true, 0, 0), // test when both are negative
                arguments(34, 333, null, true, 0, 0), // test when both are out of bounds
                arguments(-1, 0, null, true, 0, 0), // test when x is negative
                arguments(0, -5, null, true, 0, 0), // test when y is negative
                arguments(20, 0, null, true, 0, 0), // test when x is out of bounds
                arguments(0, 29, null, true, 0, 0), // test when y is out of bounds
                arguments(3, 4, EOrientation.UP, false, 3, 3), // test when yes next up
                arguments(3, 4, EOrientation.DOWN, false, 3, 5), // test when yes next down
                arguments(3, 4, EOrientation.LEFT, false, 2, 4), // test when yes next left
                arguments(3, 4, EOrientation.RIGHT, false, 4, 4), // test when yes next right
                arguments(0, 0, EOrientation.LEFT, true, 0, 0), // test when no next left
                arguments(0, 0, EOrientation.UP, true, 0, 0), // test when no next up
                arguments(9, 11, EOrientation.DOWN, true, 0, 0), // test when no next down
                arguments(9, 11, EOrientation.RIGHT, true, 0, 0) // test when no next right
        );
    }

    @ParameterizedTest(name = "the next tile of x {0}, y {1}, and orientation {2} should {3} throw or return tile with x {4} and y {5}")
    @MethodSource("getNextTileTestsArgumentProvider")
    void getNextTileTest(int x, int y, EOrientation orientation, boolean shouldThrow, int nx, int ny) {
        if (shouldThrow) {
            assertThrows(Exception.class, () -> board.getNextTileFromPositionAndDirection(x, y, orientation), "method should throw");
        } else {
            assertDoesNotThrow(() -> board.getNextTileFromPositionAndDirection(x, y, orientation), "method should not throw");
            final Tile actualTile = board.getNextTileFromPositionAndDirection(x, y, orientation);
            assertEquals(nx, actualTile.getPosX(), "next tile does not have correct x");
            assertEquals(ny, actualTile.getPosY(), "next tile does not have correct y");
        }
    }

    public static Stream<Arguments> canStepTestsArgumentProvider() {
        return Stream.of(
                arguments(-1, -2, EOrientation.UP, false), // test when both are negative
                arguments(34, 333, EOrientation.UP, false), // test when both are out of bounds
                arguments(-1, 0, EOrientation.UP, false), // test when x is negative
                arguments(0, -5, EOrientation.UP, false), // test when y is negative
                arguments(20, 0, EOrientation.UP, false), // test when x is out of bounds
                arguments(0, 29, EOrientation.UP, false), // test when y is out of bounds
                arguments(3, 4, EOrientation.UP, false), // test when yes next up but start on water
                arguments(4, 3, EOrientation.LEFT, false), // test when yes next left and cannot step due to not step-able
                arguments(4, 3, EOrientation.RIGHT, false), // test when yes next right and cannot step due to high delta z
                arguments(4, 3, EOrientation.DOWN, true) // test when yes next down and can step
        );
    }

    @ParameterizedTest(name = "checking can step from x {0} and y {1} in direction {2} should return {3}")
    @MethodSource("canStepTestsArgumentProvider")
    void canStepTest(int x, int y, EOrientation direction, boolean expected) {
        final boolean actual = board.canStep(x, y, direction);
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> containsItemAtTestsArgumentProvider() {
        return Stream.of(
                arguments(-1, -2, false), // test when both are negative
                arguments(34, 333, false), // test when both are out of bounds
                arguments(-1, 0, false), // test when x is negative
                arguments(0, -5, false), // test when y is negative
                arguments(20, 0, false), // test when x is out of bounds
                arguments(0, 29, false), // test when y is out of bounds
                arguments(3, 4, false), // test when x and y are both within bounds but no item
                arguments(4, 7, true) // test when x and y are both within bounds and yes item
        );
    }

    @ParameterizedTest(name = "checking if contains item at x {0} and y {1} should return {2}")
    @MethodSource("containsItemAtTestsArgumentProvider")
    void containsItemAtTest(int x, int y, boolean expected) {
        final boolean actual = board.containsItemAt(x, y);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "should return {0} when checking if contains teleport at x {1} and y {2}")
    @MethodSource("testContainsTeleportAtArgumentProvider")
    void testContainsTeleport(boolean result, int x, int y) {
        if (result) {
            assertTrue(board.containsTeleportAt(x,y));
        } else {
            assertFalse(board.containsTeleportAt(x,y));
        }
    }

    public static Stream<Arguments> testContainsTeleportAtArgumentProvider() {
        return Stream.of(
            arguments(false, 0, 0),
            arguments(false, 0, 2),
            arguments(false, 0, 0),
            arguments(true, 8, 9),
            arguments(false, 10, 10)
        );
    }

    @ParameterizedTest(name = "should return {0} when checking if contains teleport at x {1} and y {2}")
    @MethodSource("testGetTeleportAtArgumentProvider")
    void testGetTeleportAt(TeleportTile expected, int x, int y) {
        var result = board.getTeleportAt(x,y);
        if (expected == null) {
            assertNull(result);
        } else {
            assertEquals(expected,result);
        }
    }

    public static Stream<Arguments> testGetTeleportAtArgumentProvider() {
        return Stream.of(
                arguments(null, 0, 0),
                arguments(null, 0, 2),
                arguments(null, 10, 10),
                arguments(teleport1, 8, 9),
                arguments(teleport2, 9, 9)
        );
    }

    @BeforeAll
    void setup() {
        teleport1 = new TeleportTile(8, 9, 0, true, 9, 9, 0,0);
        teleport2 = new TeleportTile(9, 9, 0, true, 8, 9, 0, 0);
        var grid = Arrays.asList(
                new WaterTile(0, 0, 0),
                new WaterTile(1, 0, 0),
                new WaterTile(2, 0, 0),
                new WaterTile(3, 0, 0),
                new WaterTile(4, 0, 0),
                new WaterTile(5, 0, 0),
                new WaterTile(6, 0, 0),
                new WaterTile(7, 0, 0),
                new WaterTile(8, 0, 0),
                new WaterTile(9, 0, 0),

                new WaterTile(0, 1, 0),
                new WaterTile(1, 1, 0),
                new WaterTile(2, 1, 0),
                new WaterTile(3, 1, 0),
                new WaterTile(4, 1, 0),
                new WaterTile(5, 1, 0),
                new WaterTile(6, 1, 0),
                new WaterTile(7, 1, 0),
                new WaterTile(8, 1, 0),
                new WaterTile(9, 1, 0),

                new GrassTile(0, 2, 0),
                new WaterTile(1, 2, 0),
                new WaterTile(2, 2, 0),
                new WaterTile(3, 2, 0),
                new WaterTile(4, 2, 0),
                new WaterTile(5, 2, 0),
                new WaterTile(6, 2, 0),
                new WaterTile(7, 2, 0),
                new WaterTile(8, 2, 0),
                new GrassTile(9, 2, 0),

                new GrassTile(0, 3, 0),
                new WaterTile(1, 3, 0),
                new WaterTile(2, 3, 0),
                new WaterTile(3, 3, 0),
                new ConcreteTile(4, 3, 0),
                new ConcreteTile(5, 3, 2),
                new WaterTile(6, 3, 0),
                new WaterTile(7, 3, 0),
                new WaterTile(8, 3, 0),
                new GrassTile(9, 3, 0),

                new GrassTile(0, 4, 0),
                new WaterTile(1, 4, 0),
                new WaterTile(2, 4, 0),
                new WaterTile(3, 4, 0),
                new ConcreteTile(4, 4, 0),
                new WaterTile(5, 4, 0),
                new WaterTile(6, 4, 0),
                new WaterTile(7, 4, 0),
                new WaterTile(8, 4, 0),
                new GrassTile(9, 4, 0),

                new GrassTile(0, 5, 0),
                new WaterTile(1, 5, 0),
                new WaterTile(2, 5, 0),
                new WaterTile(3, 5, 0),
                new ConcreteTile(4, 5, 0),
                new WaterTile(5, 5, 0),
                new WaterTile(6, 5, 0),
                new WaterTile(7, 5, 0),
                new WaterTile(8, 5, 0),
                new GrassTile(9, 5, 0),

                new WaterTile(0, 6, 0),
                new WaterTile(1, 6, 0),
                new WaterTile(2, 6, 0),
                new WaterTile(3, 6, 0),
                new ConcreteTile(4, 6, 0),
                new WaterTile(5, 6, 0),
                new WaterTile(6, 6, 0),
                new WaterTile(7, 6, 0),
                new WaterTile(8, 6, 0),
                new GrassTile(9, 6, 0),

                new WaterTile(0, 7, 0),
                new WaterTile(1, 7, 0),
                new SandTile(2, 7, 0),
                new GrassTile(3, 7, 0),
                new GrassTile(4, 7, 0),
                new GrassTile(5, 7, 0),
                new GrassTile(6, 7, 0),
                new GrassTile(7, 7, 0),
                new GrassTile(8, 7, 0),
                new GrassTile(9, 7, 0),

                new WaterTile(0, 8, 0),
                new WaterTile(1, 8, 0),
                new WaterTile(2, 8, 0),
                new WaterTile(3, 8, 0),
                new WaterTile(4, 8, 0),
//                new WaterTile(5, 8, 0), // intentional
                new WaterTile(6, 8, 0),
                new WaterTile(7, 8, 0),
                new WaterTile(8, 8, 0),
                new GrassTile(9, 8, 0),

                new WaterTile(0, 9, 0),
                new WaterTile(1, 9, 0),
                new WaterTile(2, 9, 0),
                new WaterTile(3, 9, 0),
                new WaterTile(4, 9, 0),
                new WaterTile(5, 9, 0),
                new WaterTile(6, 9, 0),
                new LeverTile(7, 9, 0,8,9,0, false),
                new TeleportTile(8, 9, 0, true, 9, 9, 0,0),
                new TeleportTile(9, 9, 0, true, 8, 9, 0, 0)
        );
        List<Item> items = List.of(
                new CoinItem(4, 7),
                new CoinItem(7, 7)
        );
        board = new Board(grid, items, 2);
    }

    @DisplayName("after creation")
    @Nested
    class AfterCreationTests {

        @Test
        @DisplayName("has the same dimX provided in the constructor")
        void testGetDimX() {
            var actualDimX = board.getDimX();
            var expectedDimX = 10;
            assertEquals(expectedDimX, actualDimX, "dimX is not the one provided in the constructor");
        }

        @Test
        @DisplayName("has the same dimY provided in the constructor")
        void testGetDimY() {
            var actualDimY = board.getDimY();
            var expectedDimY = 10;
            assertEquals(expectedDimY, actualDimY, "dimY is not the one provided in the constructor");
        }

        @Test
        @DisplayName("has the same grid provided in the constructor")
        void testGetGrid() {
            var actualGrid = board.getGrid();
            var expectedGrid = Arrays.asList(
                    new WaterTile(0, 0, 0),
                    new WaterTile(1, 0, 0),
                    new WaterTile(2, 0, 0),
                    new WaterTile(3, 0, 0),
                    new WaterTile(4, 0, 0),
                    new WaterTile(5, 0, 0),
                    new WaterTile(6, 0, 0),
                    new WaterTile(7, 0, 0),
                    new WaterTile(8, 0, 0),
                    new WaterTile(9, 0, 0),

                    new WaterTile(0, 1, 0),
                    new WaterTile(1, 1, 0),
                    new WaterTile(2, 1, 0),
                    new WaterTile(3, 1, 0),
                    new WaterTile(4, 1, 0),
                    new WaterTile(5, 1, 0),
                    new WaterTile(6, 1, 0),
                    new WaterTile(7, 1, 0),
                    new WaterTile(8, 1, 0),
                    new WaterTile(9, 1, 0),

                    new GrassTile(0, 2, 0),
                    new WaterTile(1, 2, 0),
                    new WaterTile(2, 2, 0),
                    new WaterTile(3, 2, 0),
                    new WaterTile(4, 2, 0),
                    new WaterTile(5, 2, 0),
                    new WaterTile(6, 2, 0),
                    new WaterTile(7, 2, 0),
                    new WaterTile(8, 2, 0),
                    new GrassTile(9, 2, 0),

                    new GrassTile(0, 3, 0),
                    new WaterTile(1, 3, 0),
                    new WaterTile(2, 3, 0),
                    new WaterTile(3, 3, 0),
                    new ConcreteTile(4, 3, 0),
                    new ConcreteTile(5, 3, 2),
                    new WaterTile(6, 3, 0),
                    new WaterTile(7, 3, 0),
                    new WaterTile(8, 3, 0),
                    new GrassTile(9, 3, 0),

                    new GrassTile(0, 4, 0),
                    new WaterTile(1, 4, 0),
                    new WaterTile(2, 4, 0),
                    new WaterTile(3, 4, 0),
                    new ConcreteTile(4, 4, 0),
                    new WaterTile(5, 4, 0),
                    new WaterTile(6, 4, 0),
                    new WaterTile(7, 4, 0),
                    new WaterTile(8, 4, 0),
                    new GrassTile(9, 4, 0),

                    new GrassTile(0, 5, 0),
                    new WaterTile(1, 5, 0),
                    new WaterTile(2, 5, 0),
                    new WaterTile(3, 5, 0),
                    new ConcreteTile(4, 5, 0),
                    new WaterTile(5, 5, 0),
                    new WaterTile(6, 5, 0),
                    new WaterTile(7, 5, 0),
                    new WaterTile(8, 5, 0),
                    new GrassTile(9, 5, 0),

                    new WaterTile(0, 6, 0),
                    new WaterTile(1, 6, 0),
                    new WaterTile(2, 6, 0),
                    new WaterTile(3, 6, 0),
                    new ConcreteTile(4, 6, 0),
                    new WaterTile(5, 6, 0),
                    new WaterTile(6, 6, 0),
                    new WaterTile(7, 6, 0),
                    new WaterTile(8, 6, 0),
                    new GrassTile(9, 6, 0),

                    new WaterTile(0, 7, 0),
                    new WaterTile(1, 7, 0),
                    new SandTile(2, 7, 0),
                    new GrassTile(3, 7, 0),
                    new GrassTile(4, 7, 0),
                    new GrassTile(5, 7, 0),
                    new GrassTile(6, 7, 0),
                    new GrassTile(7, 7, 0),
                    new GrassTile(8, 7, 0),
                    new GrassTile(9, 7, 0),

                    new WaterTile(0, 8, 0),
                    new WaterTile(1, 8, 0),
                    new WaterTile(2, 8, 0),
                    new WaterTile(3, 8, 0),
                    new WaterTile(4, 8, 0),
//                    new WaterTile(5, 8, 0), // intentional
                    new WaterTile(6, 8, 0),
                    new WaterTile(7, 8, 0),
                    new WaterTile(8, 8, 0),
                    new GrassTile(9, 8, 0),

                    new WaterTile(0, 9, 0),
                    new WaterTile(1, 9, 0),
                    new WaterTile(2, 9, 0),
                    new WaterTile(3, 9, 0),
                    new WaterTile(4, 9, 0),
                    new WaterTile(5, 9, 0),
                    new WaterTile(6, 9, 0),
                    new LeverTile(7, 9, 0,8,9,0, false),
                    new TeleportTile(8, 9, 0, false, 9, 9, 0, 0),
                    new TeleportTile(9, 9, 0, false, 8, 9, 0, 0)
            );
            assertEquals(expectedGrid, actualGrid, "grid is not the one provided in the constructor");
            assertEquals(board, board,"board is not equal to itself");
            assertNotEquals(board, board.toBoardDTO(),"board is equal when compared to another class");
            Board board1 = new Board(expectedGrid,List.of(),0);
            assertNotEquals(board, board1,"board is equal when compared to board with different grid");
            Board board2 = new Board(List.of(new GrassTile(2,2,0)),List.of(),0);
            assertNotEquals(board, board2,"board is equal when compared to board with different dimension x");
            board1 = new Board(List.of(
                    new GrassTile(0,1,0),
                    new GrassTile(1,2,0),
                    new GrassTile(2,1,0)),List.of(),0);
            assertNotEquals(board1, board2,"board is equal when compared to board with " +
                    "same dimension x,y,nCoins but different grid");
            board1 = new Board(List.of(new GrassTile(2,2,0)),
                    List.of(new CoinItem(0,0)),0);
            assertNotEquals(board1, board2,"board is equal when compared to board with " +
                    "same dimension x,y,nCoins,grid, but different items");
            board1 = new Board(List.of(new GrassTile(2,1,0)),
                    List.of(new CoinItem(0,0)),0);
            assertNotEquals(board1, board2,"board is equal when compared to board with " +
                    "same dimension x, but different dimension y");
        }

        @Test
        @DisplayName("should be able to get a lever tile")
        void testGetTileAtLever() {
            var lever = new LeverTile(7,9,0,8,9,0, false);
            var actual = board.getTileAt(7,9);
            assertTrue(actual instanceof LeverTile,
                    "the created board does not contain a lever tile where supposed");
            var actualLever = (LeverTile) actual;
            assertEquals(lever.getPosX(),actualLever.getPosX(),
                    "x position of a lever is not equal after creation");
            assertEquals(lever.getPosY(),actualLever.getPosY(),
                    "y position of a lever is not equal after creation");
            assertEquals(lever.getPosZ(),actualLever.getPosZ(),
                    "z position of a lever is not equal after creation");
            assertEquals(lever.getTeleportX(),actualLever.getTeleportX(),
                    "target x of a lever is not equal after creation");
            assertEquals(lever.getTeleportY(),actualLever.getTeleportY(),
                    "target y of a lever is not equal after creation");
            assertEquals(lever.getTeleportZ(),actualLever.getTeleportZ(),
                    "target z of a lever is not equal after creation");
        }

        @Test
        @DisplayName("has the same items provided in the constructor")
        void testGetItems() {
            var actualItems = board.getItems();
            var expectedItems = List.of(
                    new CoinItem(4, 7),
                    new CoinItem(7, 7)
            );
            assertEquals(expectedItems, actualItems, "items is not the one provided in the constructor");
        }

        @Test
        @DisplayName("has the same number of coins provided in the constructor")
        void testGetNumberOfCoins() {
            var actualNumberOfCoins = board.getCoinsNumber();
            var expectedNumberOfCoins = 2;
            assertEquals(expectedNumberOfCoins, actualNumberOfCoins, "number of coins is not the one provided in the constructor");
        }

        @Test
        @DisplayName("can return the correct boardDTO")
        void testToBoardDTO() {
            var actualBoardDTO = board.toBoardDTO();
            assertEquals(board.getDimX(), actualBoardDTO.getDimX(), "boardDTO does not have the same dimX as its board");
            assertEquals(board.getDimY(), actualBoardDTO.getDimY(), "boardDTO does not have the same dimY as its board");
            assertNotNull(actualBoardDTO.getGrid(), "boardDTO grid is null");
            assertNotNull(actualBoardDTO.getItems(), "boardDTO items is null");
        }

        @Test
        @DisplayName("should be able to get the dto of a teleport tile")
        void testToTeleportDTO() {
            var expected = new TeleportTile(0,0,0, false,0,0,0,0);
            var actual = expected.toTeleportTileDTO();
            assertEquals(expected.getPosX(),actual.getPosX(),
                    "x of the teleport should be equal to the original object");
            assertEquals(expected.getPosX(),actual.getPosY(),
                    "y of the teleport should be equal to the original object");
            assertEquals(expected.getPosZ(),actual.getPosZ(),
                    "z of the teleport should be equal to the original object");
            assertEquals(expected.getTargetX(),actual.getTargetX(),
                    "x of the target teleport should be equal to the original object");
            assertEquals(expected.getTargetY(),actual.getTargetY(),
                    "y of the target teleport should be equal to the original object");
            assertEquals(expected.getTargetZ(),actual.getTargetZ(),
                    "z of the target teleport should be equal to the original object");
            assertEquals(expected.isActive(),actual.isActive(),
                    "active state of the teleport should be equal to the original object");
            assertEquals(expected.getType().name(),actual.getType(),
                    "type of the teleport tile should be equal to the original teleport object");
        }

        @Test
        @DisplayName("can return the correct string of itself")
        void testToString() {
            var actualString = board.toString();
            var expectedString = "WWGGGGWWWW\n" +
                    "WWWWWWWWWW\n" +
                    "WWWWWWWSWW\n" +
                    "WWWWWWWGWW\n" +
                    "WWWCCCC*WW\n" +
                    "WWWCWWWG W\n" +
                    "WWWWWWWGWW\n" +
                    "WWWWWWW*WL\n" +
                    "WWWWWWWGWT\n" +
                    "WWGGGGGGGT\n";
            assertEquals(expectedString, actualString, "strings don't match");
        }

    }

    @ParameterizedTest(name = "can compare the tile with another tile object")
    @MethodSource("tileEqualsArguments")
    public void testTileEquals(boolean equals, Tile tile1, String same, String difference) {
        var tile = board.getTileAt(0,0);
        if (equals) {
            assertEquals(tile,tile1,
                    "tile is not equal when compared to " + same);
        } else {
            assertNotEquals(tile,tile1,
                    "tile is equal when compared to a level with same " + same +
                            " but different " + difference);
        }
    }

    public static Stream<Arguments> tileEqualsArguments() {
        var tile1 = new WaterTile(0,0,0);
        tile1.setWalkable(true);
        return Stream.of(
                arguments(false,new WaterTile(1,0,0),
                        "","x"),
                arguments(false,new WaterTile(0,1,0),
                        "x","y"),
                arguments(false,new WaterTile(0,0,1),
                        "x,y","z"),
                arguments(false,tile1,
                        "x,y,z","walkable status"),
                arguments(false,new LavaTile(0,0,0),
                        "x,y,z,walkable status","type"),
                arguments(true,new WaterTile(0,0,0),
                        "another tile with equal values","")
        );
    }

    @Test
    @DisplayName("can compare a tile with itself")
    public void testTileEqualsSpecialCases() {
        var tile = board.getTileAt(0,0);
        assertEquals(tile,tile,"tile is not equal when compared to itself");
        assertNotEquals(tile,tile.toTileDTO(),
                "tile is equal when compared to an object of another class");
    }

    @ParameterizedTest(name = "can compare an item with another item object")
    @MethodSource("itemEqualsArguments")
    public void testItemEquals(boolean equals, Item item1, String same, String difference) {
        var item = board.getItemAt(4,7);
        if (equals) {
            assertEquals(item,item1,
                    "item is not equal when compared to " + same);
        } else {
            assertNotEquals(item,item1,
                    "item is equal when compared to a level with same " + same +
                            " but different " + difference);
        }
    }

    public static Stream<Arguments> itemEqualsArguments() {
        var tile1 = new WaterTile(0,0,0);
        tile1.setWalkable(true);
        return Stream.of(
                arguments(false,new CoinItem(5,7),
                        "","x"),
                arguments(false,new CoinItem(4,8),
                        "x","y"),
                arguments(true,new CoinItem(4,7),
                        "another item with equal values","")
        );
    }

    @Test
    @DisplayName("can compare an item with itself")
    public void testItemEqualsSpecialCases() {
        var item = board.getItemAt(4,7);
        assertEquals(item,item,"item is not equal when compared to itself");
        assertNotEquals(item,item.toItemDTO(),
                "item is equal when compared to an object of another class");
    }
}
