package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Mine;
import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Figures out who to shank
 *
 * This decisioner figures out which bot deserves it most (or is most vulnerable) and goes after them.
 *
 * On
 */
public class BotTargetingDecisioner implements Decision<AdvancedGameState, BotMove> {
    private final Decision<AdvancedGameState, BotMove> noTargetFoundDecisioner;

    public BotTargetingDecisioner(Decision<AdvancedGameState, BotMove> noTargetFoundDecisioner) {
        this.noTargetFoundDecisioner = noTargetFoundDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        // Is there a crashed bot with mines we can take advantage of?
        for(Mine currentMine : state.getMines().values()) {
            if(currentMine.getOwner().isCrashed()) {
                // TODO Move towards this dude and murder him
                return BotMove.STAY;
            }
        }

        // Ok, crashed bots.  How about bots that aren't squatting?
        // TODO Figure this out

        // Ok, no one worth attacking.
        return noTargetFoundDecisioner.makeDecision(state);
    }
}
