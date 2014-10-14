package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AdvancedGameState {
    private final Map<GameState.Position, Mine> mines;
    private final Map<GameState.Position, Pub> pubs;
    private final Map<GameState.Position, GameState.Hero> heroesByPosition;
    private final Map<Integer, GameState.Hero> heroesById;
    private final Map<GameState.Position, Vertex> boardGraph;
    private final GameState.Hero me;

    /**
     * Creates an AdvancedGameState from a GameState
     * @param gameState
     */
    public AdvancedGameState(GameState gameState) {
        boardGraph = new HashMap<>();
        mines = new HashMap<>();
        pubs = new HashMap<>();
        heroesById = new HashMap<>();
        heroesByPosition = new HashMap<>();

        // Hero stuffs
        for(GameState.Hero currentHero : gameState.getGame().getHeroes()) {
            this.heroesByPosition.put(currentHero.getPos(), currentHero);
            this.heroesById.put(currentHero.getId(), currentHero);
        }

        this.me = gameState.getHero();

        // Build the graph sans edges
        GameState.Board board = gameState.getGame().getBoard();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                // Yeah, Vindinium does the x and y coordinates backwards
                GameState.Position pos = new GameState.Position(row, col);
                int tileStart = row * board.getSize() * 2 + (col * 2);
                String tileValue = board.getTiles().substring(tileStart, tileStart + 1 + 1);

                // We do nothing with tiles that are barriers
                if (tileValue.equals("##"))
                    continue;

                Vertex v = new Vertex(pos, new LinkedList<Vertex>());

                this.boardGraph.put(pos, v);

                // If its a mine or tavern, we treat it differently
                // We don't care if its a hero because a separate index for those already exists
                if (tileValue.startsWith("$")) {
                    String owner = tileValue.substring(1);
                    Mine mine;
                    if (owner.equals("-")) {
                        mine = new Mine(pos, null);
                    } else {
                        int ownerId = Integer.parseInt(owner);
                        mine = new Mine(pos, this.heroesById.get(ownerId));
                    }

                    this.mines.put(pos, mine);
                } else if (tileValue.equals("[]")) {
                    Pub pub = new Pub(pos);
                    this.pubs.put(pos, pub);
                }
            }
        }

        // Add in the edges
        // This graph doesn't take into account players because they move.  That is done elsewhere.
        for (Vertex currentVertex : this.boardGraph.values()) {
            GameState.Position currentVertexPosition = currentVertex.getPosition();

            // Pubs and mines cannot be passed through
            if(this.mines.containsKey(currentVertexPosition) || this.pubs.containsKey(currentVertexPosition))
                continue;

            // Other players cannot be passed through.  However, they move, and mines/pubs don't, so its easier to make
            // the bot and path-finding deal with that.  We don't take other players into account here.

            // We can only move NSEW, so no need for a fancy set of nested loops...
            for (int xDelta = -1; xDelta <= 1; xDelta += 2) {
                int currentX = currentVertex.getPosition().getX();
                int newX = currentX + xDelta;
                if(newX >= 0 && newX < board.getSize()) {
                    GameState.Position adjacentPos = new GameState.Position(newX, currentVertex.getPosition().getY());
                    Vertex adjacentVertex = this.boardGraph.get(adjacentPos);
                    if(adjacentVertex != null)
                        currentVertex.getAdjacentVertices().add(adjacentVertex);
                }
            }
            for (int yDelta = -1; yDelta <= 1; yDelta += 2) {
                int currentY = currentVertex.getPosition().getY();
                int newY = currentY + yDelta;
                if(newY >= 0 && newY < board.getSize()) {
                    GameState.Position adjacentPos = new GameState.Position( currentVertex.getPosition().getX(), newY);
                    Vertex adjacentVertex = this.boardGraph.get(adjacentPos);
                    if(adjacentVertex != null)
                        currentVertex.getAdjacentVertices().add(adjacentVertex);
                }
            }
        }
    }

    /**
     * Creates a new AdvancedGameState by taking he previous AdvancedGameState and updating is using a new GameState
     * @param oldGameState
     * @param updatedState
     */
    public AdvancedGameState(AdvancedGameState oldGameState, GameState updatedState) {

        // Copy the stuff we can just re-use
        this.boardGraph = oldGameState.getBoardGraph();
        this.pubs = oldGameState.getPubs();

        // Re-build the hero maps
        this.heroesByPosition = new HashMap<>();
        this.heroesById = new HashMap<>();
        for(GameState.Hero currentHero : updatedState.getGame().getHeroes()) {
            this.heroesByPosition.put(currentHero.getPos(), currentHero);
            this.heroesById.put(currentHero.getId(), currentHero);
        }
        this.me = updatedState.getHero();

        // Update the mines
        this.mines = oldGameState.getMines();
        for(Mine currentMine : this.mines.values()) {
            // Vindinium does the x and y coordinates backwards
            int tileStart = currentMine.getPosition().getX()
                    * updatedState.getGame().getBoard().getSize()
                    *  2 + (currentMine.getPosition().getY() * 2);
            // We don't want the whole tile; we want the second char
            String owner = updatedState.getGame().getBoard().getTiles().substring(tileStart + 1, tileStart + 1 + 1);
            Mine mine;
            if (owner.equals("-")) {
                mine = new Mine(currentMine.getPosition(), null);
            } else {
                int ownerId = Integer.parseInt(owner);
                mine = new Mine(currentMine.getPosition(), this.heroesById.get(ownerId));
            }

            this.mines.put(mine.getPosition(), mine);
        }
    }

    public AdvancedGameState(Map<GameState.Position, Mine> mines, Map<GameState.Position, Pub> pubs,
                             Map<GameState.Position, GameState.Hero> heroesByPosition, Map<Integer,
            GameState.Hero> heroesById, Map<GameState.Position, Vertex> boardGraph, GameState.Hero me) {
        this.mines = mines;
        this.pubs = pubs;
        this.heroesByPosition = heroesByPosition;
        this.heroesById = heroesById;
        this.boardGraph = boardGraph;
        this.me = me;
    }

    public Map<GameState.Position, Mine> getMines() {
        return mines;
    }

    public Map<GameState.Position, Pub> getPubs() {
        return pubs;
    }

    public Map<GameState.Position, GameState.Hero> getHeroesByPosition() {
        return heroesByPosition;
    }

    public Map<Integer, GameState.Hero> getHeroesById() {
        return heroesById;
    }

    public Map<GameState.Position, Vertex> getBoardGraph() {
        return boardGraph;
    }

    public GameState.Hero getMe() {
        return me;
    }
}
