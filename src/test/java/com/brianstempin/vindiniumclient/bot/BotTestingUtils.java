package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Static utility methods to make testing easier
 */
public class BotTestingUtils {

    private static Gson gson = new Gson();

    /**
     * Private constructor to prevent instantiation
     */
    private BotTestingUtils() {
        // Outta here!  This class is for static methods only
    }

    /**
     * Gets a GameState by reading and parsing a file from a given resource path
     *
     * @param resourcePath path to a resource within the class path
     * @return a parsed GameState
     */
    public static GameState getGameState(String resourcePath) throws FileNotFoundException {
        File file = new File(BotTestingUtils.class.getResource(resourcePath).getFile());
        GameState gameState = gson.fromJson(new FileReader(file), GameState.class);

        return gameState;
    }

    /**
     * Gets an AdvancedGameState by reading and parsing a file from a given resource path
     *
     * @param resourcePath path to a resource within the class path
     * @return a parsed AdvancedGameState
     */
    public static AdvancedGameState getAdvancedGameState(String resourcePath) throws FileNotFoundException {
        GameState gameState = getGameState(resourcePath);
        AdvancedGameState advancedGameState = new AdvancedGameState(gameState);

        return advancedGameState;
    }
}
