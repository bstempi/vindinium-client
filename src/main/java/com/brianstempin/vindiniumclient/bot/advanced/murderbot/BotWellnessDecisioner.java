package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;

import java.util.logging.Logger;

/**
 * Decides if the bot is "well" (healthy) and acts accordingly.
 *
 * This decisioner will check to make sure the bot is healthy enough to play on and act accordingly.
 *
 * On Maslow's Hierarchy of needs, this one services psychological and safety needs.
 */
public class BotWellnessDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {

    private static final Logger logger = Logger.getLogger("BotWellnessDecisioner");

    private final Decision<AdvancedMurderBot.GameContext, BotMove> yesDecisioner;
    private final Decision<AdvancedMurderBot.GameContext, BotMove> noDecisioner;

    public BotWellnessDecisioner(Decision<AdvancedMurderBot.GameContext, BotMove> yesDecisioner,
                                 Decision<AdvancedMurderBot.GameContext, BotMove> noDecisioner) {
        this.yesDecisioner = yesDecisioner;
        this.noDecisioner = noDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {

        // Is the bot well?
        if(context.getGameState().getMe().getLife() >= 30) {
            logger.info("Bot is healthy.");
            return yesDecisioner.makeDecision(context);
        }
        else {
            logger.info("Bot is damaged.");
            return noDecisioner.makeDecision(context);
        }
    }
}
