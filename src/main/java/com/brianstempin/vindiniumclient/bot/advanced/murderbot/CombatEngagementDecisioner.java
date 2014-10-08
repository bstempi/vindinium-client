package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides if the bot is in combat and acts accordingly.
 *
 * This decisioner will determine if the bot is engaged in combat.  "Combat" is defined as being within striking
 * distance of another player.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class CombatEngagementDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
