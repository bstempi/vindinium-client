package com.brianstempin.vindiniumclient.dto;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.File;
import java.io.FileReader;

/**
 * Test that games states are correctly deserialized
 */
public class GameStateTest {

    private Gson gson;
    private File jsonFile;

    @Before
    public void setup() {
        gson = new Gson();
        jsonFile = new File(this.getClass().getResource("/known-good-game-state.json").getFile());
    }

    @Test
    public void testParsingKnownGoodTestState() throws Exception {
        GameState testObj;

        testObj = gson.fromJson(new FileReader(jsonFile), GameState.class);
        ReflectionAssert.assertPropertiesNotNull("Something in the GameState was null.  This test is designed to set " +
                "every field", testObj);
    }
}
