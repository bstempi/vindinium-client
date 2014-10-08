package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides how to best commit suicide.
 *
 * Fortunately, bots don't go to hell.  Instead of letting an enemy win its mines, it can attempt to suicide on a mine
 * that doesn't belong to them.  This decisioner attempts to "opt out," as they say in The Walking Dead.
 *
 * On Maslov's Hierarchy, this falls under "self actualization."  Kind of depressing that suicide is the big goal, eh?
 */
public class SuicideDecisioner implements Decision<AdvancedGameState, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        return null;
    }
}
