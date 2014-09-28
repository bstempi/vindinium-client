package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Represents a mine on the map
 */
public class Mine {
    private final GameState.Position position;
    private final GameState.Hero owner;

    public Mine(GameState.Position position, GameState.Hero owner) {
        this.position = position;
        this.owner = owner;
    }

    public GameState.Position getPosition() {
        return position;
    }

    public GameState.Hero getOwner() {
        return owner;
    }
}
