package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides to go after an unclaimed mine far, far away.
 *
 * This decisioner decides if any mines are "easy," despite being out of the way.
 *
 * According to Maslov's Hierarchy, this is boredom.
 */
public class UnattendedMineDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
