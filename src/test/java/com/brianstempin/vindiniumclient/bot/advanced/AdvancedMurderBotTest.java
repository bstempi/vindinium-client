package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

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
    public void dijkstraTest() {
        Map<GameState.Position, AdvancedMurderBot.DijkstraResult> dijkstraResultMap =
                AdvancedMurderBot.dijkstraSearch(gameState);

        Assert.assertEquals(88, dijkstraResultMap.size());
    }

    // TODO Think of an intelligent way to name tests
    // TODO Performance test
}
