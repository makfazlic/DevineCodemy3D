package ch.usi.si.bsc.sa4.devinecodemy.model.language;

import ch.usi.si.bsc.sa4.devinecodemy.model.ECategory;
import ch.usi.si.bsc.sa4.devinecodemy.model.animation.ECoordinatesAnimation;
import ch.usi.si.bsc.sa4.devinecodemy.model.animation.ERobotAnimation;
import ch.usi.si.bsc.sa4.devinecodemy.model.EOrientation;
import ch.usi.si.bsc.sa4.devinecodemy.model.EWorld;
import ch.usi.si.bsc.sa4.devinecodemy.model.item.CoinItem;
import ch.usi.si.bsc.sa4.devinecodemy.model.item.Item;
import ch.usi.si.bsc.sa4.devinecodemy.model.level.Board;
import ch.usi.si.bsc.sa4.devinecodemy.model.level.Level;
import ch.usi.si.bsc.sa4.devinecodemy.model.level.Robot;
import ch.usi.si.bsc.sa4.devinecodemy.model.levelvalidation.LevelValidation;
import ch.usi.si.bsc.sa4.devinecodemy.model.tile.*;
import ch.usi.si.bsc.sa4.devinecodemy.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("The Language")
public class LanguageTests {

    Context context;

    Board board;
    Robot robot;
    Level level;
    LevelValidation levelValidation;

    @MockBean
    StatisticsRepository statisticsRepository;

    @BeforeEach
    void setup() {
        robot = new Robot(0,0,EOrientation.DOWN);

        List<Tile> grid = List.of(
                new GrassTile(0,0,0),
                new GrassTile(0,1,0),
                new ConcreteTile(0,2,0),
                new GrassTile(1,0,0),
                new GrassTile(1,1,0),
                new GrassTile(1,2,0),
                new WaterTile(2,0,0),
                new WaterTile(2,1,0),
                new WaterTile(2,2,0),
                new TeleportTile(3,0,0, false, 3, 1, 0, 1),
                new TeleportTile(3,1,0, false, 3, 0, 0, 1)
        );

        List<Item> items = List.of(new CoinItem(0,2),
                new CoinItem(0,1));
        board = new Board(grid,items,2);

        List<ECategory> allowedCommands = List.of(ECategory.BASIC_COMMANDS, ECategory.CONDITIONS, ECategory.LOGIC);
        level = new Level("Level 1", "the first level",1, EWorld.PURGATORY,
                4,board,robot,allowedCommands,"../../assets/thumbnail.jpg");
        levelValidation = new LevelValidation();
        context = new Context(board, robot, 4, levelValidation);
    }

    @Test
    @DisplayName("should be able to return its board")
    public void testGetBoard() {
        var thisBoard = context.getBoard();
        assertEquals(board , thisBoard, "The board should be the same");
    }

    @Test
    @DisplayName("should be able to get the robot")
    public void testGetRobot() {
        var thisRobot = context.getRobot();
        assertEquals(robot, thisRobot, "The robot should be the same");
    }

    @Test
    @DisplayName("should be able to get the level validation")
    public void testGetLevelValidation() {
        var thisLevelValidation = context.getLevelValidation();
        assertEquals(levelValidation, thisLevelValidation, "The level validation should be the same");
    }

    @Test
    @DisplayName("should be able to get the function table")
    public void testGetFunctionTable() {
        var thisFunctionTable = context.getFunctionTable();
        assertNotNull(thisFunctionTable, "The function table should not be null");
    }

    @Test
    @DisplayName("should be able to set the function table")
    public void testSetFunctionTable() {
        Map<String, Action> thisFunctionTable = new HashMap<>();
        thisFunctionTable.put("test", new ActionMoveForward(null));
        assertNotNull(thisFunctionTable, "The function table should not be null");
        assertTrue(thisFunctionTable.containsKey("test"), "The function table should contain the key test");
        assertNotNull(thisFunctionTable.get("test"), "The function table should return the action test");
        assertFalse(thisFunctionTable.containsKey("test2"), "The function table should not contain the key test2");
    }

    @Test
    @DisplayName("should be able to return if the player is dead or not")
    public void testIsDead() {
        assertFalse(context.isDead(), "The player should not be dead");
        context.setDead(true);
        assertTrue(context.isDead(), "The player should be dead");
        context.setDead(false);
        assertFalse(context.isDead(), "The player should not be dead");
    }

    @Test
    @DisplayName("should be able to get the collected coins number")
    public void testGetCollectedCoins() {
        var thisContext = context;
        assertEquals(0, thisContext.getCollectedCoins(), "The collected coins should be 0");
        thisContext.incrementCollectedCoins();
        assertEquals(1, thisContext.getCollectedCoins(), "The collected coins should be 1");
    }

    @Test
    @DisplayName("should be able to execute a program with basic commands")
    public void testExecuteBasicProgram() {
        Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionTurnLeft(new ActionTurnRight(new ActionCollectCoin(null))))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertFalse(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "doesn't add death animation when the player is not dead");
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD), "adds move forward animation");
    }

    @Test
    @DisplayName("should not be able to run a program with two main blocks")
    public void testExecuteProgramWithTwoMainBlocks() {
        Program thisProgram = new Program(List.of(new ActionMoveForward(null), new ActionMoveForward(null)));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not be able to run a program without a main block")
    public void testExecuteProgramWithoutMainBlock() {
        Program thisProgram = new Program(List.of());
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not be able to run a program without a main block")
    public void testExecuteProgramWithoutMainBlock2() {
        Program thisProgram = new Program(List.of(new FunctionDefinition("pippo", new ActionMoveForward(null))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should be able to run a program with functions")
    public void testExecuteProgramWithFunctions() {
        Program thisProgram = new Program(List.of(new ActionFunctionCall("pippo", null),
                                                  new FunctionDefinition("pippo",
                                                                          new ActionMoveForward(null))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertFalse(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "doesn't add death animation when the player is not dead");
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD), "adds move forward animation");
        assertFalse(result.hasErrors(), "doesn't add error when the program can be executed");
        assertTrue(context.getFunctionTable().containsKey("pippo"), "adds function to the function table");
    }

    @Test
    @DisplayName("should not be able to define a function with the same name as a built-in function")
    public void testDefineFunctionWithBuiltInFunctionName() {
        Program thisProgram = new Program(List.of(new FunctionDefinition("moveForward", new ActionMoveForward(null))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not be able to define a function with the same name of a function defined in the same program")
    public void testDefineFunctionWithSameName() {
        Program thisProgram = new Program(List.of(new FunctionDefinition("pippo", new ActionMoveForward(null)),
                                                  new FunctionDefinition("pippo", new ActionMoveForward(null))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not be able to complete a level with an unknown function")
    public void testUnknownFunction() {
        Program thisProgram = new Program(List.of(new ActionFunctionCall("pippo", null)));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not complete a level with too many commands")
    public void testTooManyCommands() {
        Program thisProgram = new Program(List.of(new ActionMoveForward(
                                                    new ActionCollectCoin(
                                                        new ActionMoveForward(
                                                             new ActionCollectCoin(
                                                                 new ActionTurnLeft(null)))))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should not complete a level with too many commands")
    public void testTooManyCommands2() {
        Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionFunctionCall("pippo", null)),
                new FunctionDefinition("pippo",new ActionMoveForward(new ActionMoveForward(new ActionMoveForward(null))))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should not be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH),
                "adds death animation when the program cannot be executed");
        assertTrue(result.hasErrors(), "adds error when the program cannot be executed");
    }

    @Test
    @DisplayName("should be able to complete a level")
    public void testCompleteLevel() {
        Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionCollectCoin(new ActionMoveForward(new ActionCollectCoin(null))))));
        LevelValidation result = thisProgram.execute(context);

        assertTrue(this.context.getBoard().getTeleportAt(3, 0).isActive());
        assertTrue(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD), "adds move forward animation");
        assertTrue(result.getAnimations().contains(ERobotAnimation.JUMP), "adds jump animation");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DANCE),
                    "adds emote dance animation when the level is completed");
        assertEquals(2, context.getCollectedCoins(), "count collected coins");
        assertFalse(context.isDead(), "The player should not be dead");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test collect coin action")
    public void testCollectCoin() {
        Program thisProgram = new Program(List.of(new ActionCollectCoin(new ActionMoveForward(new ActionCollectCoin(null)))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.JUMP), "adds jump animation");
        assertEquals(1, context.getCollectedCoins(), "count collected coins");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test death")
    public void testDeath() {
        Program thisProgram = new Program(List.of(new ActionTurnRight(
                                                    new ActionMoveForward(
                                                            new ActionTurnLeft(
                                                                    new ActionTurnRight(null))))));
        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH), "adds death animation");
        assertTrue(context.isDead(), "The player should be dead");
        assertFalse(result.hasErrors(), "The level validation should not have errors");

        Program thisProgram2 = new Program(List.of(new ActionTurnRight(
                                                    new ActionMoveForward(
                                                        new ActionMoveForward(
                                                            new ActionCollectCoin(null))))));
        result = thisProgram2.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_DEATH), "adds death animation");
        assertTrue(context.isDead(), "The player should be dead");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test if statements with false")
    public void testIfStatementsFalse() {
        Program thisProgram = new Program(List.of(new ActionIf(
                                                    new ConditionCanStep("RIGHT"),
                                                    new ActionMoveForward(null),
                                                    null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().isEmpty(), "no animations if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test if statements with hasLever")
    public void testIfStatementsHasLever() {
        Program thisProgram = new Program(List.of(new ActionIf(
                                                    new ConditionHasLever("active"),
                                                    new ActionMoveForward(null),
                                                    null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().isEmpty(), "no animations if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test if statements with true")
    public void testIfStatementsTrue() {
        Program thisProgram = new Program(List.of(new ActionIf(
                new ConditionCanStep("FORWARD"),
                new ActionMoveForward(null),
                null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertFalse(result.getAnimations().isEmpty(), "add animations if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test if statements with invalid condition")
    public void testIfStatementsInvalid() {
        Program thisProgram = new Program(List.of(new ActionIf(
                new ConditionCanStep("ASD"),
                new ActionMoveForward(null),
                null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.hasErrors(), "The level validation should have errors");
    }

    @Test
    @DisplayName("test if-else statements with false")
    public void testIfElseStatementsFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                    new ConditionCanStep("BACKWARD"),
                                                    new ActionMoveForward(null),
                                                    new ActionTurnRight(null),
                                                    null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_RIGHT), "should turn right if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test if-else statements with true")
    public void testIfElseStatementsTrue() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionCanStep("LEFT"),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnRight(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD), "should move forward if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test while loop with false")
    public void testWhileLoopFalse() {
        Program thisProgram = new Program(List.of(new ActionWhile(
                                                        new ConditionFalse(),
                                                        new ActionMoveForward(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().isEmpty(), "no animations if the condition is false");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test while loop with true")
    public void testWhileLoopTrue() {
        Program thisProgram = new Program(List.of(new ActionWhile(
                                                        new ConditionCanStep("FORWARD"),
                                                        new ActionMoveForward(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD), "should move forward if the condition is true");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }

    @Test
    @DisplayName("test infinite while loop with true")
    public void testWhileLoopTrueTimeout() {
        Program thisProgram = new Program(List.of(new ActionWhile(
                                                        new ConditionTrue(),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);

        assertFalse(result.isCompleted(), "The level should be completed");
        assertTrue(result.hasErrors(), "The level validation should have errors");
    }

    @Test
    @DisplayName("test for loop")
    public void testForLoop() {
        Program thisProgram = new Program(List.of(new ActionLoop(
                                                        4,
                                                        new ActionIfElse(
                                                                new ConditionContainsCoin(),
                                                                new ActionCollectCoin(null),
                                                                new ActionMoveForward(null),
                                                                null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);

        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
        assertFalse(result.hasErrors(), "The level validation should not have errors");
    }


    @Test
    @DisplayName("test infinite recursion")
    public void testInfiniteRecursion() {
        Program thisProgram = new Program(List.of(new FunctionDefinition("f", new ActionFunctionCall("f", null)),
                                                  new ActionFunctionCall("f", null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.hasErrors(), "The level validation should have errors");
    }

    @Test
    @DisplayName("test boolean or")
    public void testBooleanOr() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionOR(
                                                                new ConditionTrue(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean or with false")
    public void testBooleanOrFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionOR(
                                                                new ConditionFalse(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_LEFT),
                "should turn left if the condition is false");
    }

    @Test
    @DisplayName("test boolean or with false")
    public void testBooleanOrFalseTrue() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionOR(
                                                                new ConditionFalse(),
                                                                new ConditionTrue()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean not")
    public void testBooleanNot() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionNOT(
                                                                new ConditionTrue()),
                                                        new ActionTurnLeft(null),
                                                        new ActionMoveForward(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test boolean not with false")
    public void testBooleanNotFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionNOT(
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean xor")
    public void testBooleanXor() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionXOR(
                                                                new ConditionTrue(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test boolean xor with false")
    public void testBooleanXorFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionXOR(
                                                                new ConditionFalse(),
                                                                new ConditionTrue()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean xor with true")
    public void testBooleanXorTrue() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionXOR(
                                                                new ConditionTrue(),
                                                                new ConditionTrue()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_LEFT),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test boolean xor with true and false")
    public void testBooleanXorTrueFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionXOR(
                                                                new ConditionTrue(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean and")
    public void testBooleanAnd() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionAND(
                                                                new ConditionTrue(),
                                                                new ConditionTrue()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.MOVE_FORWARD),
                "should move forward if the condition is true");
    }

    @Test
    @DisplayName("test boolean and with false")
    public void testBooleanAndFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionAND(
                                                                new ConditionTrue(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_LEFT),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test boolean and with false and true")
    public void testBooleanAndFalseTrue() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionAND(
                                                                new ConditionFalse(),
                                                                new ConditionTrue()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));
        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_LEFT),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test boolean and with false and false")
    public void testBooleanAndFalseFalse() {
        Program thisProgram = new Program(List.of(new ActionIfElse(
                                                        new ConditionAND(
                                                                new ConditionFalse(),
                                                                new ConditionFalse()),
                                                        new ActionMoveForward(null),
                                                        new ActionTurnLeft(null),
                                                        null)));

        LevelValidation result = thisProgram.execute(context);
        assertTrue(result.getAnimations().contains(ERobotAnimation.TURN_LEFT),
                "should turn left if the condition is true");
    }

    @Test
    @DisplayName("test teleport")
    public void testTeleport() {
        // create a context playable with teleports
        robot = new Robot(0,0,EOrientation.DOWN);

        List<Tile> grid = List.of(
                new GrassTile(0,0,0),
                new TeleportTile(0,1,0, false, 3, 1, 0, 1),
                new TeleportTile(3,1,0, false, 3, 0, 0, 1)
        );

        List<Item> items = List.of(new CoinItem(0,0),
                new CoinItem(3,1));
        board = new Board(grid,items,2);

        List<ECategory> allowedCommands = List.of(ECategory.BASIC_COMMANDS, ECategory.CONDITIONS, ECategory.LOGIC);
        level = new Level("Level 1", "the first level",1, EWorld.PURGATORY,
                4,board,robot,allowedCommands,"../../assets/thumbnail.jpg");
        levelValidation = new LevelValidation();

        Context thisContext = new Context(board, robot, 4, levelValidation);
        Program thisProgram = new Program(List.of(new ActionCollectCoin(new ActionMoveForward(new ActionCollectCoin(null)))));

        LevelValidation result = thisProgram.execute(thisContext);

        assertFalse(result.hasErrors(), "The level validation should not have errors");
        assertTrue(result.isCompleted(), "The level should be completed");
    }

    @Nested
    public class testAfterTeleportCreation {

        private Context context;

        @BeforeEach
        void setup() {
            // create a context playable with teleports
            LanguageTests.this.robot = new Robot(0,0,EOrientation.DOWN);
            List<Tile> grid = List.of(
                    new LeverTile(0,0,0, 0, 3, 0, false),
                    new LeverTile(0,1,0, 0, 0, 0, false),
                    new LeverTile(0,2,0, 3, 1, 0, false),
                    new LeverTile(1,1,0, 2, 1, 0, false),
                    new LeverTile(1,0,0, 2, 2, 0, false),
                    new TeleportTile(0,3,0, false, 3, 1, 0, 1),
                    new TeleportTile(2,1,0, false, 4, 2, 0, 1),
                    new TeleportTile(2,2,0, false, 2, 1, 0, 0),
                    new GrassTile(4,2,0),
                    new TeleportTile(3,1,0, true, 3, 0, 0, 1)
            );

            List<Item> items = List.of(new CoinItem(3,1));
            Board board = new Board(grid,items,1);
            List<ECategory> allowedCommands = List.of(ECategory.BASIC_COMMANDS, ECategory.CONDITIONS, ECategory.LOGIC);
            LanguageTests.this.level = new Level("Level 1", "the first level",1, EWorld.PURGATORY,
                    6, LanguageTests.this.board,robot,allowedCommands,"../../assets/thumbnail.jpg");
            levelValidation = new LevelValidation();
            context = new Context(board, robot, level.getMaxCommandsNumber(), levelValidation);
        }

        @Test
        @DisplayName("test active teleport with lever")
        public void testActiveTeleportWithLever() {

            Program thisProgram = new Program(List.of(new ActionActivateLever(new ActionMoveForward(
                    new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null)))))));

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());

            LevelValidation result = thisProgram.execute(context);

            assertTrue(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertTrue(result.isCompleted(), "The level should be completed");
        }

        @Test
        @DisplayName("test hasLever")
        public void testHasLever() {

            Program thisProgram = new Program(List.of(new ActionIf(new ConditionHasLever(""),
                    new ActionActivateLever(new ActionMoveForward(
                    new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null))))), null)));

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());

            LevelValidation result = thisProgram.execute(context);

            assertTrue(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertTrue(result.isCompleted(), "The level should be completed");
        }

        @Test
        @DisplayName("test hasLever inactive")
        public void testHasLeverInactive() {

            Program program1 = new Program(List.of(new ActionIf(new ConditionHasLever("inactive"),
                    new ActionActivateLever(new ActionMoveForward(
                            new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null))))), null)));

            LevelValidation result1 = program1.execute(context);

            assertTrue(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result1.hasErrors(), "The level validation should not have errors");
            assertTrue(result1.isCompleted(), "The level should be completed");
        }

        @Test
        @DisplayName("test hasLever inactive")
        public void testHasLeverInactive2() {
            Program program2 = new Program(List.of(new ActionActivateLever(new ActionIf(new ConditionHasLever("inactive"),
                    new ActionActivateLever(new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null)))), null))));

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());

            LevelValidation result2 = program2.execute(context);

            assertTrue(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result2.hasErrors(), "The level validation should not have errors");
        }

        @Test
        @DisplayName("test hasLever active")
        public void testHasLeverActive() {

            Program thisProgram = new Program(List.of(new ActionActivateLever(new ActionIf(new ConditionHasLever("active"),
                    new ActionMoveForward(new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null)))), null))));

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());

            LevelValidation result = thisProgram.execute(context);

            assertTrue(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result.hasErrors(), "The level validation should not have errors");
        }

        @Test
        @DisplayName("test hasLever with non existing lever")
        public void testHasLeverWithNonExistingLever() {

            Program program1 = new Program(List.of(new ActionMoveForward(new ActionActivateLever(new ActionIf(new ConditionHasLever("inactive"),
                    new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null))), null)))));
            Program program2 = new Program(List.of(new ActionActivateLever(new ActionIf(new ConditionHasLever("active"),
                    new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null))), null))));
            Program program3 = new Program(List.of(new ActionMoveForward(new ActionActivateLever(new ActionIf(new ConditionHasLever("sdfa"),
                    new ActionMoveForward(new ActionMoveForward(new ActionCollectCoin(null))), null)))));


            LevelValidation result1 = program1.execute(context);
            LevelValidation result2 = program2.execute(context);
            LevelValidation result3 = program3.execute(context);

            assertFalse(result1.hasErrors(), "The level validation should not have errors");
            assertFalse(result2.hasErrors(), "The level validation should not have errors");
            assertFalse(result3.hasErrors(), "The level validation should not have errors");
        }

        @Test
        @DisplayName("test deactivate teleport with lever")
        public void testDeactivateTeleportWithLever() {

            Program thisProgram = new Program(List.of(new ActionActivateLever(
                    new ActionActivateLever(new ActionMoveForward(new ActionCollectCoin(null))))));

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());

            LevelValidation result = thisProgram.execute(context);

            assertFalse(context.getBoard().getTeleportAt(0, 3).isActive());
            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");

            var animations = result.getAnimations();

            assertTrue(animations.get(0).toString().startsWith(ECoordinatesAnimation.ACTIVATE_LEVER.getName()),
                    "First animation does not match the expected one after lever pulled");
            assertTrue(animations.get(1).toString().startsWith(ECoordinatesAnimation.ACTIVATE_TELEPORT_AT.getName()),
                    "Second animation does not match the expected one after lever pulled");
            assertTrue(animations.get(2).toString().startsWith(ECoordinatesAnimation.ACTIVATE_TELEPORT_AT.getName()),
                    "Third animation does not match the expected one after lever pulled");
            assertTrue(animations.get(3).toString().startsWith(ECoordinatesAnimation.DEACTIVATE_LEVER.getName()),
                    "Fourth animation does not match the expected one after lever pulled");
            assertTrue(animations.get(4).toString().startsWith(ECoordinatesAnimation.DEACTIVATE_TELEPORT_AT.getName()),
                    "Fifth animation does not match the expected one after lever pulled");
            assertTrue(animations.get(5).toString().startsWith(ECoordinatesAnimation.DEACTIVATE_TELEPORT_AT.getName()),
                    "Sixth animation does not match the expected one after lever pulled");
        }

        @Test
        @DisplayName("test activate teleport with lever pointing to a not teleport tile")
        public void testActivateTeleportWithLeverWrong() {
            Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionActivateLever(null))));

            LevelValidation result = thisProgram.execute(context);

            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
        }

        @Test
        @DisplayName("should not activate the teleport and give error when pulling a lever on a tile that is not a lever")
        public void testActivateLeverOnNotLeverTile() {
            Program thisProgram = new Program(List.of(new ActionMoveForward(
                    new ActionMoveForward(new ActionMoveForward(new ActionActivateLever(null))))));

            LevelValidation result = thisProgram.execute(context);

            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
            assertTrue(result.getAnimations().contains(ERobotAnimation.EMOTE_NO),
                    "the level validation should contain an emote_no animation");
        }

        @Test
        @DisplayName("should not activate teleport with lever pointing to a teleport pointing to a not teleport tile")
        public void testActivateTeleportWithLeverToWrongTeleport() {
            Program thisProgram = new Program(List.of(new ActionMoveForward(
                    new ActionTurnLeft(new ActionMoveForward(new ActionActivateLever(
                            new ActionMoveForward(null)))))));

            LevelValidation result = thisProgram.execute(context);

            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
        }

        @Test
        @DisplayName("should activate teleport with lever pointing to a teleport pointing to a not teleport tile")
        public void testActivateLeverAfterError() {
            Program thisProgram = new Program(List.of(new ActionTurnLeft(new ActionMoveForward(
                    new ActionMoveForward(new ActionMoveForward(
                            new ActionActivateLever(null)))))));

            LevelValidation result = thisProgram.execute(context);

            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
        }

        @Test
        @DisplayName("should not activate teleport if collecting a coin but teleport needs lever")
        public void testActivateTeleportAfterCollectCoin() {
            Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionMoveForward(new ActionTurnLeft(
                    new ActionMoveForward(new ActionMoveForward(
                            new ActionCollectCoin(null))))))));

            LevelValidation result = thisProgram.execute(context);

            var tile = (TeleportTile) context.getBoard().getTileAt(2,2);

            assertFalse(tile.isActive(),"the teleport should still be not active after collecting a coin");
            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
        }

        @Test
        @DisplayName("should not activate teleport if collecting a coin but teleport needs lever")
        public void testActivateTeleport() {
            Program thisProgram = new Program(List.of(new ActionMoveForward(new ActionMoveForward(new ActionTurnLeft(
                    new ActionMoveForward(new ActionMoveForward(
                            new ActionCollectCoin(null))))))));

            LevelValidation result = thisProgram.execute(context);
            var tile = (TeleportTile) context.getBoard().getTileAt(2,2);

            assertFalse(tile.isActive(),"the teleport should still be not active after collecting a coin");
            assertFalse(result.hasErrors(), "The level validation should not have errors");
            assertFalse(result.isCompleted(), "The level should not be completed");
        }
    }

}
