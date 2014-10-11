package com.brianstempin.vindiniumclient.bot.advanced.advancedmurderbot;

import com.brianstempin.vindiniumclient.bot.BotTestingUtils;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Map;

public class AdvancedMurderBotTest {

    private static final String KNOWN_GOOD_GAME_STATE = "/known-good-game-state.json";
    private static final Gson gson = new Gson();

    private AdvancedGameState gameState;
    private AdvancedMurderBot testObj;

    @Before
    public void setup() throws FileNotFoundException {
        this.testObj = new AdvancedMurderBot();
        gameState = BotTestingUtils.getAdvancedGameState(KNOWN_GOOD_GAME_STATE);
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
