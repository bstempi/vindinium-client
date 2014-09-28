package com.brianstempin.vindiniumclient.bot;

/**
 * This is the output of a SimpleBot.
 * <p/>
 * Because the SimpleBot does not have enough information to create a full Move, it will return a BotMove instead,
 * allowing the framework to generate a Move response with it.
 */
public enum BotMove {

    STAY("Stay"), WEST("West"), EAST("East"), NORTH("North"), SOUTH("South");

    private final String direction;

    BotMove(String moveName) {
        this.direction = moveName;
    }

    public String toString() {
        return direction;
    }
}
