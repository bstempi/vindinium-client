package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides where the nearest mine is and how to get there.
 *
 * This decisioner (aren't you tired of that phrase?) will figure out where the nearest mine is and issue a move towards
 * there.
 *
 * On Maslov's Hierarchy, this is self actualization.
 */
public class NearestMineDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
