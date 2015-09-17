package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BotUtils {

    /**
     * Given a current position and some position directly adjacent to the current position, this method will calculate
     * the direction of the move.
     *
     * This method is NOT safe to use if the target position is more than 1 move away from the current position.
     * @param currentLocation
     * @param target some position adjacent to the current position
     * @return
     */
    public static BotMove directionTowards(GameState.Position currentLocation, GameState.Position target) {
        if (target.getX() < currentLocation.getX()) {
            return BotMove.NORTH;
        } else if (target.getX() > currentLocation.getX()) {
            return BotMove.SOUTH;
        } else if (target.getY() < currentLocation.getY()) {
            return BotMove.WEST;
        } else if (target.getY() > currentLocation.getY()) {
            return BotMove.EAST;
        } else {
            return BotMove.STAY;
        }
    }

    /**
     * Returns a list of the enemies with radius squares of your hero
     * @param gameState
     * @param searchResults
     * @param radius
     * @return
     */
    public static List<GameState.Hero> getHeroesAround(AdvancedGameState gameState,
                                                           Map<GameState.Position, AdvancedMurderBot.DijkstraResult> searchResults,
                                                       int radius) {
        List<GameState.Hero> heroes = new LinkedList<>();

        for(GameState.Hero currentHero : gameState.getHeroesByPosition().values()) {
            GameState.Position currentHeroPosition = currentHero.getPos();
            if(searchResults.get(currentHeroPosition).getDistance() <= radius
                    && currentHero.getId() != gameState.getMe().getId())
                heroes.add(currentHero);
        }

        return heroes;
    }
}
