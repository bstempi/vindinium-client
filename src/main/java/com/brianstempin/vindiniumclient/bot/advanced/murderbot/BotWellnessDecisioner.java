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
public class BotWellnessDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {

    private final Decision<AdvancedMurderBot.GameContext, BotMove> yesDecisioner;
    private final Decision<AdvancedMurderBot.GameContext, BotMove> noDecisioner;

    public BotWellnessDecisioner(Decision<AdvancedMurderBot.GameContext, BotMove> yesDecisioner,
                                 Decision<AdvancedMurderBot.GameContext, BotMove> noDecisioner) {
        this.yesDecisioner = yesDecisioner;
        this.noDecisioner = noDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {
        if(context.getGameState().getMe().getLife() < 30)
            return noDecisioner.makeDecision(context);
        else
            return yesDecisioner.makeDecision(context);
    }
}
