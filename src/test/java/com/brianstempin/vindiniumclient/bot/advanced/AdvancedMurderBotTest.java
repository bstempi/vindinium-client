package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class AdvancedMurderBotTest {

    private static final String KNOWN_GOOD_GAME_STATE = "/known-good-game-state.json";
    private static final Gson gson = new Gson();

    private AdvancedGameState gameState;
    private AdvancedMurderBot testObj;

    @Before
    public void setup() throws FileNotFoundException {
        this.testObj = new AdvancedMurderBot();
        File knownGoodStateFile = new File(this.getClass().getResource(KNOWN_GOOD_GAME_STATE).getFile());
        gameState = new AdvancedGameState(gson.fromJson(new FileReader(knownGoodStateFile), GameState.class));
    }

    @Test
    public void goodHealthMoveToClosestEligiblePlayer() {
        BotMove move = testObj.move(gameState);

        Assert.assertEquals(BotMove.SOUTH, move);
    }
}
