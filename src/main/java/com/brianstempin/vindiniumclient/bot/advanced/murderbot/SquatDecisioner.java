package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.bot.advanced.Pub;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.Map;

/**
 * Decides if we should be lame and squat.  Also known as, "turtling."
 *
 * If we're in a good spot in the game, it might make sense to just stay put and waste time.  This decisioner decides
 * how to best do that.
 *
 * If we're here, we've left Maslov behind...we've become self-aware and have left the hierarchy.
 */
public class SquatDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {
    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {
        GameState.Hero me = context.getGameState().getMe();
        Map<GameState.Position, AdvancedMurderBot.DijkstraResult> dijkstraResultMap = context.getDijkstraResultMap();

        // The way to squat is to get next to a tavern.  Don't walk into it unless we need health.
        Pub nearestPub = null;
        AdvancedMurderBot.DijkstraResult nearestPubDijkstraResult = null;
        for(Pub pub : context.getGameState().getPubs().values()) {
            if(nearestPub == null) {
                nearestPub = pub;
                nearestPubDijkstraResult = dijkstraResultMap.get(pub.getPosition());
                continue;
            }

            if(dijkstraResultMap.get(nearestPub.getPosition()).getDistance()
                    > dijkstraResultMap.get(pub.getPosition()).getDistance()) {
                nearestPub = pub;
                nearestPubDijkstraResult = dijkstraResultMap.get(pub.getPosition());
            }
        }

        // Do we need to move to get there?
        if(nearestPubDijkstraResult.getDistance() > 1) {
            AdvancedMurderBot.DijkstraResult currentResult = nearestPubDijkstraResult;
            GameState.Position currentPosition = nearestPub.getPosition();

            while(currentResult.getDistance() > 1) {
                currentPosition = currentResult.getPrevious();
                currentResult = dijkstraResultMap.get(currentPosition);
            }

            return BotUtils.directionTowards(me.getPos(), currentPosition);
        }

        // Ok, we must be there.  Do we need health?
        if(me.getLife() < 50) {
            return BotUtils.directionTowards(me.getPos(), nearestPub.getPosition());
        }

        // Nothing to do...squat!
        return BotMove.STAY;
    }
}
