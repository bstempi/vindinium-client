package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by bstempi on 9/23/14.
 */
public class MurderBotTest {

    private static final String KNOWN_GOOD_GAME_STATE = "/known-good-game-state.json";
    private static final Gson gson = new Gson();

    private GameState gameState;
    private MurderBot testObj;

    @Before
    public void setup() {
        testObj = new MurderBot();
    }

    @Test
    public void goodHealthMoveToClosestPlayer() throws FileNotFoundException {
        File jsonFile = new File(this.getClass().getResource(KNOWN_GOOD_GAME_STATE).getFile());
        gameState = gson.fromJson(new FileReader(jsonFile), GameState.class);

        BotMove move = testObj.move(gameState);
        Assert.assertEquals(BotMove.SOUTH, move);
    }

    // TODO Have different game state for this test
    @Test
    public void badHealthMoveToClosestTavern() throws FileNotFoundException {
        File jsonFile = new File(this.getClass().getResource(KNOWN_GOOD_GAME_STATE).getFile());
        gameState = gson.fromJson(new FileReader(jsonFile), GameState.class);

        BotMove move = testObj.move(gameState);
        Assert.assertEquals(BotMove.SOUTH, move);
    }

    // TODO Have different game state for this test
    @Test
    public void shankPlayer() throws FileNotFoundException {
        File jsonFile = new File(this.getClass().getResource(KNOWN_GOOD_GAME_STATE).getFile());
        gameState = gson.fromJson(new FileReader(jsonFile), GameState.class);

        BotMove move = testObj.move(gameState);
        Assert.assertEquals(BotMove.SOUTH, move);
    }
}
