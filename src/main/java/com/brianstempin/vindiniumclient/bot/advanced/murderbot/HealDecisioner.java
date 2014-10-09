package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.bot.advanced.Pub;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Decides the best way to get healed.
 *
 * This decisioner will do its best to steer the bot towards a tavern without confrontation.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class HealDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {

    private static final Logger logger = Logger.getLogger("HealDecisioner");

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {
        logger.info("Running to nearest pub.");

        Map<GameState.Position, AdvancedMurderBot.DijkstraResult> dijkstraResultMap = context.getDijkstraResultMap();

        // Run to the nearest pub
        Pub nearestPub = null;
        AdvancedMurderBot.DijkstraResult nearestPubDijkstraResult = null;
        for(Pub pub : context.getGameState().getPubs().values()) {
            if(nearestPub == null || nearestPubDijkstraResult.getDistance() >
                    dijkstraResultMap.get(pub.getPosition()).getDistance()) {
                nearestPub = pub;
                nearestPubDijkstraResult = dijkstraResultMap.get(pub.getPosition());
            }
        }

        GameState.Position nextMove = nearestPub.getPosition();
        while(nearestPubDijkstraResult.getDistance() > 1) {
            nextMove = nearestPubDijkstraResult.getPrevious();
            nearestPubDijkstraResult = dijkstraResultMap.get(nextMove);
        }

        return BotUtils.directionTowards(nearestPubDijkstraResult.getPrevious(), nextMove);
    }
}
