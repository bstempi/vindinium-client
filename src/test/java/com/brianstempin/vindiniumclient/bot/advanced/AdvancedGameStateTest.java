package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.BotTestingUtils;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdvancedGameStateTest {
    private static final String KNOWN_GOOD_GAME_STATE = "/known-good-game-state.json";
    private static final String KNOWN_GOOD_GAME_STATE_2 = "/known-good-game-state-2.json";
    private static final Gson gson = new Gson();

    @Before
    public void setup() {

    }

    @Test
    public void createFromGameState() throws IOException {
        GameState gameState = BotTestingUtils.getGameState(KNOWN_GOOD_GAME_STATE);

        AdvancedGameState testObj = new AdvancedGameState(gameState);

        // Make sure nothing is null
        Assert.assertNotNull(testObj.getBoardGraph());
        Assert.assertNotNull(testObj.getHeroesById());
        Assert.assertNotNull(testObj.getHeroesByPosition());
        Assert.assertNotNull(testObj.getMe());
        Assert.assertNotNull(testObj.getMines());
        Assert.assertNotNull(testObj.getPubs());

        // Test a mine with an owner
        Assert.assertEquals(testObj.getMines().get(new GameState.Position(3, 7)).getOwner().getId(), 4);
        // Test a mine without an owner
        Assert.assertNull(testObj.getMines().get(new GameState.Position(14, 7)).getOwner());
    }

    @Test
    public void updateState() throws FileNotFoundException {
        GameState firstGameState = BotTestingUtils.getGameState(KNOWN_GOOD_GAME_STATE);
        GameState secondGameState = BotTestingUtils.getGameState(KNOWN_GOOD_GAME_STATE_2);

        AdvancedGameState originalGameState = new AdvancedGameState(firstGameState);
        AdvancedGameState testObj = new AdvancedGameState(originalGameState, secondGameState);

        Assert.assertEquals(testObj.getHeroesById().get(1).getLife(), 100);
        Assert.assertNull(testObj.getMines().get(new GameState.Position(3, 7)).getOwner());
    }

    @Test
    public void boardGraph() throws FileNotFoundException {
        GameState gameState = BotTestingUtils.getGameState(KNOWN_GOOD_GAME_STATE);

        AdvancedGameState testObj = new AdvancedGameState(gameState);
        Vertex heroVertex = testObj.getBoardGraph().get(testObj.getMe().getPos());

        // Make sure there's a vertex for our hero
        Assert.assertNotNull(heroVertex);

        // Make sure there's the correct number of traversable positions in our graph
        Assert.assertEquals(88, testObj.getBoardGraph().size());

        // Make sure that no position on the board returns null for adjacentVertices
        for (Map.Entry<GameState.Position, Vertex> entry : testObj.getBoardGraph().entrySet()) {
            List<Vertex> adjacentVertices = entry.getValue().getAdjacentVertices();

            Assert.assertNotNull("adjacentVertices() returned a null for position "
                    + "x=" + entry.getKey().getX()
                    + ", y=" + entry.getKey().getY(), adjacentVertices);
        }
    }
}
