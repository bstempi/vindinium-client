package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.Map;

/**
 * An improvement upon com.brianstempin.vindiniumClient.bot.simple.MurderBot
 */
public class AdvancedMurderBot implements AdvancedBot {

    @Override
    public BotMove move(GameState.Hero me, Map<GameState.Position, Mine> mines, Map<GameState.Position, Pub> pubs,
                        Map<GameState.Position, GameState.Hero> heroes, Map<GameState.Position, Vertex> boardGraph) {
        return null;
    }

    @Override
    public void setup() {
        // No-op
    }

    @Override
    public void shutdown() {
        // No-op
    }
}
