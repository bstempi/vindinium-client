package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides if a battle is winnable and what to do about it.
 *
 * This decisioner will try to predict a battle's outcome and will decide wether or not to keep fighting.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class CombatOutcomeDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
