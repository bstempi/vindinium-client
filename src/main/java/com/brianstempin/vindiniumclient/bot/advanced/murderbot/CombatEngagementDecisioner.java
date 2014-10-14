package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger(CombatEngagementDecisioner.class);

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
        Map<GameState.Position, GameState.Hero> heroesByPosition = context.getGameState().getHeroesByPosition();
        Map<GameState.Position, Vertex> boardGraph = context.getGameState().getBoardGraph();

        for(Vertex neighboringVertex : boardGraph.get(me.getPos()).getAdjacentVertices()) {
            // Is there a neighbor in this vertex
            GameState.Position neighboringPosition = neighboringVertex.getPosition();
            if(heroesByPosition.containsKey(neighboringPosition)) {
                logger.info("Bot is currently engaged in combat.");
                return yesDecision.makeDecision(context);
            }
        }

        // Welp, no one was close, so its false.
        logger.info("Bot is currently not engaged in combat.");
        return noDecision.makeDecision(context);
    }
}
