package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Mine;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Figures out who to shank
 *
 * This decisioner figures out which bot deserves it most (or is most vulnerable) and goes after them.
 *
 * On
 */
public class BotTargetingDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {
    private final Decision<AdvancedMurderBot.GameContext, BotMove> noTargetFoundDecisioner;

    public BotTargetingDecisioner(Decision<AdvancedMurderBot.GameContext, BotMove> noTargetFoundDecisioner) {
        this.noTargetFoundDecisioner = noTargetFoundDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {

        // Is there a crashed bot with mines we can take advantage of?
        for(Mine currentMine : context.getGameState().getMines().values()) {
            if(currentMine.getOwner().isCrashed()) {

                GameState.Hero target = currentMine.getOwner();
                AdvancedMurderBot.DijkstraResult currentDijkstraResult =
                        context.getDijkstraResultMap().get(target.getPos());
                GameState.Position nextPosition = target.getPos();

                while(currentDijkstraResult.getDistance() > 1) {
                    nextPosition = currentDijkstraResult.getPrevious();
                    currentDijkstraResult = context.getDijkstraResultMap().get(nextPosition);
                }

                return BotUtils.directionTowards(currentDijkstraResult.getPrevious(), nextPosition);
            }
        }

        // Ok, crashed bots.  How about bots that aren't squatting?
        GameState.Hero closestTarget = null;
        AdvancedMurderBot.DijkstraResult closestTargetDijkstraResult = null;
        for(GameState.Hero currentHero : context.getGameState().getHeroesByPosition().values()) {
            // Check the adjacent squares to see if a pub exists
            Vertex currentHeroVertext = context.getGameState().getBoardGraph().get(currentHero.getPos());
            for(Vertex currentVertext : currentHeroVertext.getAdjacentVertices()) {
                if(context.getGameState().getPubs().containsKey(currentVertext.getPosition())) {
                    break;
                }
            }

            // Ok, we got this far...it must not be squatting.  Is it closest?
            if(closestTarget == null) {
                closestTarget = currentHero;
                closestTargetDijkstraResult = context.getDijkstraResultMap().get(currentHero.getPos());
                continue;
            } else if(closestTargetDijkstraResult.getDistance() >
                    context.getDijkstraResultMap().get(currentHero.getPos()).getDistance()) {
                closestTarget = currentHero;
                closestTargetDijkstraResult = context.getDijkstraResultMap().get(closestTarget.getPos());
            }
        }

        if(closestTarget != null) {
            GameState.Position nextMove = closestTarget.getPos();
            while (closestTarget != null && closestTargetDijkstraResult.getDistance() > 1) {
                nextMove = closestTargetDijkstraResult.getPrevious();
                closestTargetDijkstraResult = context.getDijkstraResultMap().get(nextMove);
            }

            return BotUtils.directionTowards(closestTargetDijkstraResult.getPrevious(), nextMove);
        }

        // Ok, no one worth attacking.
        return noTargetFoundDecisioner.makeDecision(context);
    }
}
