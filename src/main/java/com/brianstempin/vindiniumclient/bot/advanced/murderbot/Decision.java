package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

/**
 * Really, really simple starter for a behavior tree.
 *
 * @param <S> state used to make a decision
 * @param <R> result of decision
 */
public interface Decision<S, R> {

    /**
     * Given a state, return a result
     * @param state
     * @return
     */
    public R makeDecision(S state);
}
