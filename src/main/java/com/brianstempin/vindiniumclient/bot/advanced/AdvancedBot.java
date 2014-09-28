package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.List;
import java.util.Map;

/**
 * Created by bstempi on 9/28/14.
 */
public interface AdvancedBot {

    public BotMove move(GameState.Hero me,
                        Map<GameState.Position, Mine> mines,
                        Map<GameState.Position, Pub> pubs,
                        Map<GameState.Position, GameState.Hero> heroes,
                        Map<GameState.Position, Vertex> boardGraph);

    /**
     * Called before the game is started
     */
    public void setup();

    /**
     * Called after the game
     */
    public void shutdown();

}
