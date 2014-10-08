package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides if we should be lame and squat.  Also known as, "turtling."
 *
 * If we're in a good spot in the game, it might make sense to just stay put and waste time.  This decisioner decides
 * how to best do that.
 *
 * If we're here, we've left Maslov behind...we've become self-aware and have left the hierarchy.
 */
public class SquatDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
