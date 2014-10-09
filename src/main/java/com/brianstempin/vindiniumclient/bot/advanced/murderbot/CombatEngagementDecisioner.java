package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.Map;

/**
 * Decides if the bot is in combat and acts accordingly.
 *
 * This decisioner will determine if the bot is engaged in combat.  "Combat" is defined as being within striking
 * distance of another player.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class CombatEngagementDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {
    private final Decision<AdvancedMurderBot.GameContext, BotMove> yesDecision;
    private final Decision<AdvancedMurderBot.GameContext, BotMove> noDecision;

    public CombatEngagementDecisioner(Decision<AdvancedMurderBot.GameContext, BotMove> yesDecision,
                                      Decision<AdvancedMurderBot.GameContext, BotMove> noDecision) {
        this.yesDecision = yesDecision;
        this.noDecision = noDecision;
    }

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {
        GameState.Hero me = context.getGameState().getMe();
        Map<GameState.Position, Vertex> boardGraph = context.getGameState().getBoardGraph();

        for(Vertex neighboringVertex : boardGraph.get(me.getPos()).getAdjacentVertices()) {
            // Is there a neighbor in this vertex
            if(context.getGameState().getHeroesByPosition().containsKey(neighboringVertex))
                return yesDecision.makeDecision(context);
        }

        // Welp, no one was close, so its false.
        return noDecision.makeDecision(context);
    }
}
