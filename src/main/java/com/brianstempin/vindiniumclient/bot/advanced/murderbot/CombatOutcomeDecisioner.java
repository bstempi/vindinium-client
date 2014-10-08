package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Decides if a battle is winnable and what to do about it.
 *
 * This decisioner will try to predict a battle's outcome and will decide wether or not to keep fighting.
 *
 * On the Maslow Hierarchy, this falls under safety.
 */
public class CombatOutcomeDecisioner implements Decision<AdvancedGameState, BotMove> {
    private final Decision<AdvancedGameState, BotMove> winningDecisioner;
    private final Decision<AdvancedGameState, BotMove> losingDecisioner;

    public CombatOutcomeDecisioner(Decision<AdvancedGameState, BotMove> winningDecisioner, Decision
            <AdvancedGameState, BotMove> losingDecisioner) {
        this.winningDecisioner = winningDecisioner;
        this.losingDecisioner = losingDecisioner;
    }

    @Override
    public BotMove makeDecision(AdvancedGameState state) {
        // Who am I fighting?
        GameState.Hero opponent = null;
        Vertex myVertext = state.getBoardGraph().get(state.getMe().getPos());
        for(Vertex vertex : myVertext.getAdjacentVertices()) {
            if(state.getHeroesByPosition().containsKey(vertex.getPosition())) {
                opponent = state.getHeroesByPosition().get(vertex.getPosition());
                break;
            }
        }

        // If we have more health than them or they only can take one more hit
        if(opponent.getLife() <= 20 || state.getMe().getLife() >= 30) {
            return winningDecisioner.makeDecision(state);
        }  else {
            return losingDecisioner.makeDecision(state);
        }
    }
}
