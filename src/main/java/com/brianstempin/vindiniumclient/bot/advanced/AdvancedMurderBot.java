package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * An improvement upon com.brianstempin.vindiniumClient.bot.simple.MurderBot
 */
public class AdvancedMurderBot implements AdvancedBot {

    /**
     * Represents the result of a Dijkstra search for a given position
     */
    public static class DijkstraResult {
        private int distance;
        private GameState.Position previous;

        public DijkstraResult(int distance, GameState.Position previous) {
            this.distance = distance;
            this.previous = previous;
        }

        public int getDistance() {
            return distance;
        }

        public GameState.Position getPrevious() {
            return previous;
        }
    }

    public static Map<GameState.Position, DijkstraResult> dijkstraSearch(AdvancedGameState gameState) {
        Map<GameState.Position, DijkstraResult> result = new HashMap<>();

        DijkstraResult startingResult = new DijkstraResult(0, null);
        Queue<GameState.Position> queue = new ArrayBlockingQueue<>(gameState.getBoardGraph().size());
        queue.add(gameState.getMe().getPos());
        result.put(gameState.getMe().getPos(), startingResult);

        while(!queue.isEmpty()) {
            GameState.Position currentPosition = queue.poll();
            DijkstraResult currentResult = result.get(currentPosition);
            Vertex currentVertext = gameState.getBoardGraph().get(currentPosition);
            int distance = currentResult.getDistance() + 1;

            for(Vertex neighbor : currentVertext.getAdjacentVertices()) {
                DijkstraResult neighborResult = result.get(neighbor.getPosition());
                if(neighborResult == null) {
                    neighborResult = new DijkstraResult(distance, currentPosition);
                    result.put(neighbor.getPosition(), neighborResult);
                    queue.remove(neighbor.getPosition());
                    queue.add(neighbor.getPosition());
                } else if(neighborResult.distance > distance) {
                    DijkstraResult newNeighborResult = new DijkstraResult(distance, currentPosition);
                    result.put(neighbor.getPosition(), newNeighborResult);
                    queue.remove(neighbor.getPosition());
                    queue.add(neighbor.getPosition());
                }
            }
        }

        return result;
    }

    @Override
    public BotMove move(AdvancedGameState gameState) {

        Map<GameState.Position, DijkstraResult> dijkstraResultMap = dijkstraSearch(gameState);



        return null;

    }

    @Override
    public void setup() {
        // No-op
    }

    @Override
    public void shutdown() {
        // No-op
    }
}
