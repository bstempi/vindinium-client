package com.brianstempin.vindiniumclient.bot.simple;

import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Before;

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

    // TODO Figure out how to intelligently name tests
}
