package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;

/**
 * Decides if the bot is "well" (healthy) and acts accordingly.
 *
 * This decisioner will check to make sure the bot is healthy enough to play on and act accordingly.
 *
 * On Maslow's Hierarchy of needs, this one services psychological and safety needs.
 */
public class BotWellnessDecisioner implements Decision<AdvancedGameState, BotMove> {

    private final Decision<AdvancedGameState, BotMove> yesDecisioner;
    private final Decision<AdvancedGameState, BotMove> noDecisioner;

    public BotWellnessDecisioner(Decision<AdvancedGameState, BotMove> yesDecisioner, Decision<AdvancedGameState,
            BotMove> noDecisioner) {
        this.yesDecisioner = yesDecisioner;
        this.noDecisioner = noDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        if(state.getMe().getLife() < 30)
            return noDecisioner.makeDecision(state);
        else
            return yesDecisioner.makeDecision(state);
    }
}
