package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;

/**
 * Most basic interface for a bot
 * <p/>
 * The Bot gets a GameState and is expected to return a BotMove.  The response to the server is a Move,
 * but since a Bot does not know its API key, it returns a BotMove to indicate the direction and allows the framework
 * to take care of building a Move response.
 */
public interface Bot {
    public BotMove move(GameState gameState);
}
