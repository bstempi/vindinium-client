package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Decides if the bot is in combat and acts accordingly.
 *
 * This decisioner will determine if the bot is engaged in combat.  "Combat" is defined as being within striking
 * distance of another player.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class CombatEngagementDecisioner implements Decision<AdvancedGameState, BotMove> {
    private final Decision<AdvancedGameState, BotMove> yesDecision;
    private final Decision<AdvancedGameState, BotMove> noDecision;

    public CombatEngagementDecisioner(Decision<AdvancedGameState, BotMove> yesDecision, Decision<AdvancedGameState,
            BotMove> noDecision) {
        this.yesDecision = yesDecision;
        this.noDecision = noDecision;
    }

    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        for(Vertex neighboringVertex : state.getBoardGraph().get(state.getMe().getPos()).getAdjacentVertices()) {
            // Is there a neighbor in this vertex
            if(state.getHeroesByPosition().containsKey(neighboringVertex))
                return yesDecision.makeDecision(state);
        }

        // Welp, no one was close, so its false.
        return noDecision.makeDecision(state);
    }
}
