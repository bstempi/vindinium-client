package com.brianstempin.vindiniumclient.bot.simple;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by bstempi on 9/22/14.
 */
public class MurderBot implements SimpleBot {

    private Logger logger;

    public MurderBot() {
        logger = Logger.getLogger("murderbot");
    }

    /**
     * Does Dijkstra and returns the a 1d structure that can be treated as 2d
     *
     * @param board
     * @param hero
     * @return
     */
    private static List<Vertex> doDijkstra(GameState.Board board, GameState.Hero hero) {
        List<Vertex> vertexes = new LinkedList<Vertex>();
        Vertex me = null;

        // Build the graph sans edges

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Vertex v = new Vertex();
                GameState.Position pos = new GameState.Position(row, col);
                int tileStart = row * board.getSize() * 2 + (col * 2);
                v.setTileType(board.getTiles().substring(tileStart, tileStart + 1 + 1));
                v.setPosition(pos);
                vertexes.add(v);
            }
        }

        // Add in the edges
        for (int i = 0; i < board.getSize() * board.getSize(); i++) {
            int row = i % (board.getSize());
            int col = i / board.getSize();

            Vertex v = vertexes.get(i);

            // Check:  Is this us?
            if (v.getPosition().getX() == hero.getPos().getX() && v.getPosition().getY() == hero.getPos().getY())
                me = v;
            if (v.getTileType().equals("##") || v.getTileType().equals("[]") ||
                    v.getTileType().startsWith("$"))
                // Impassable tiles link to nowhere.
                continue;

            // Make sure not to link to impassable blocks that don't have a function
            // Make sure you don't link impassable blocks to other blocks (you can't go from the pub to somewhere else)
            for (int j = col - 1; j <= col + 1; j += 2) {
                if (j >= 0 && j < board.getSize()) {
                    Vertex adjacentV = vertexes.get(j * board.getSize() + row);
                    v.getAdjacencies().add(adjacentV);
                }
            }
            for (int j = row - 1; j <= row + 1; j += 2) {
                if (j >= 0 && j < board.getSize()) {
                    Vertex adjacentV = vertexes.get(col * board.getSize() + j);
                    v.getAdjacencies().add(adjacentV);
                }
            }
        }

        // Zok, we have a graph constructed.  Traverse.
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(me);
        me.setMinDistance(0);

        while (!vertexQueue.isEmpty()) {
            Vertex v = vertexQueue.poll();
            double distance = v.getMinDistance() + 1;

            for (Vertex neighbor : v.getAdjacencies()) {
                if (distance < neighbor.getMinDistance()) {
                    neighbor.setMinDistance(distance);
                    neighbor.setPrevious(v);
                    vertexQueue.remove(neighbor);
                    vertexQueue.add(neighbor);
                }
            }
        }

        return vertexes;
    }

    private static List<Vertex> getPath(Vertex target) {
        List<Vertex> path = new LinkedList<Vertex>();

        path.add(target);
        Vertex next = target;
        while (next.getPrevious().getMinDistance() != 0) {
            path.add(next.getPrevious());
            next = next.getPrevious();
        }

        Collections.reverse(path);
        return path;
    }

    private boolean runAwayMode = false;

    @Override
    public BotMove move(GameState gameState) {
        logger.info("Starting move");

        // Step zero:  Do some path-finding
        List<Vertex> vertexes = doDijkstra(gameState.getGame().getBoard(), gameState.getHero());

        Vertex closestPub = null;
        Vertex closestPlayer = null;

        for (Vertex v : vertexes) {
            if (v.getTileType().startsWith("@") && v.getMinDistance() != 0 && v.getMinDistance() != Double
                    .POSITIVE_INFINITY && (closestPlayer == null || closestPlayer
                    .getMinDistance() > v.getMinDistance()))
                closestPlayer = v;
            else if (v.getTileType().equals("[]") && v.getMinDistance() != Double.POSITIVE_INFINITY && (closestPub ==
                    null || closestPub.getMinDistance() > v
                    .getMinDistance()))
                closestPub = v;
        }


        // Step one:  Do I need HP?
        if (gameState.getHero().getGold() >= 2 && gameState.getHero().getLife() <= 30) {
            runAwayMode = true;
            Vertex move = getPath(closestPub).get(0);
            logger.info("Getting beer");
            return BotUtils.directionTowards(gameState.getHero().getPos(), move.getPosition());
        }

        if (runAwayMode == true && gameState.getHero().getGold() >= 2 && gameState.getHero().getLife() <= 80) {
            Vertex move = getPath(closestPub).get(0);
            logger.info("Getting beer");
            return BotUtils.directionTowards(gameState.getHero().getPos(), move.getPosition());
        } else {
            runAwayMode = false;
        }

        // Step two:  Shank someone.
        Vertex move = getPath(closestPlayer).get(0);
        logger.info("Going after player");
        return BotUtils.directionTowards(gameState.getHero().getPos(), move.getPosition());

    }

    @Override
    public void setup() {
        // No-op
    }

    @Override
    public void shutdown() {
        // No-op
    }

    private static class Vertex implements Comparable<Vertex> {
        private String tileType;
        private List<Vertex> adjacencies;
        private double minDistance;
        private Vertex previous;
        private GameState.Position position;

        private Vertex() {
            this.minDistance = Double.POSITIVE_INFINITY;

            // Safe default size...we want to avoid resizing
            this.adjacencies = new ArrayList<Vertex>(50 * 50);
        }

        public GameState.Position getPosition() {
            return position;
        }

        public void setPosition(GameState.Position position) {
            this.position = position;
        }

        public String getTileType() {
            return tileType;
        }

        public void setTileType(String tileType) {
            this.tileType = tileType;
        }

        public List<Vertex> getAdjacencies() {
            return adjacencies;
        }

        public void setAdjacencies(List<Vertex> adjacencies) {
            this.adjacencies = adjacencies;
        }

        public double getMinDistance() {
            return minDistance;
        }

        public void setMinDistance(double minDistance) {
            this.minDistance = minDistance;
        }

        public Vertex getPrevious() {
            return previous;
        }

        public void setPrevious(Vertex previous) {
            this.previous = previous;
        }

        @Override
        public int compareTo(Vertex o) {
            return Double.compare(getMinDistance(), o.getMinDistance());
        }
    }
}
