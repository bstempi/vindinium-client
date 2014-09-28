package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.bot.advanced.AdvancedBot;
import com.brianstempin.vindiniumclient.bot.advanced.Mine;
import com.brianstempin.vindiniumclient.bot.advanced.Pub;
import com.brianstempin.vindiniumclient.bot.advanced.Vertex;
import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by bstempi on 9/28/14.
 */
public class AdvancedBotRunner implements Runnable {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

    private final ApiKey apiKey;
    private final Class<? extends AdvancedBot> botClass;
    private final GenericUrl gameUrl;
    private final AdvancedBot bot;

    // Semi-mutable.  Once in the map, the objects stay there.  THe state of the map values may change.
    // TODO mines and pubs don't change position, so they should be immutable.  Consider using Guava
    private Map<GameState.Position, Mine> mines;
    private Map<GameState.Position, Pub> pubs;
    private Map<GameState.Position, GameState.Hero> herosByPosition;

    // Effectively immutable.  They are mutable but not designed to be.
    // TODO The map itself should be immutable.  Consider using Guava
    private Map<Integer, GameState.Hero> heroById;
    private Map<GameState.Position, Vertex> immutableBoardGraph;

    public AdvancedBotRunner(ApiKey apiKey, Class<? extends AdvancedBot> botClass, GenericUrl gameUrl) throws
            IllegalAccessException, InstantiationException {
        this.apiKey = apiKey;
        this.botClass = botClass;
        this.gameUrl = gameUrl;
        this.bot = this.botClass.newInstance();
    }

    @Override
    public void run() {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState;

        try {
            // Initial request
            content = new UrlEncodedContent(apiKey);
            request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
            request.setReadTimeout(0); // Wait forever to be assigned to a game
            response = request.execute();
            gameState = response.parseAs(GameState.class);

            this.initialParse(gameState);

            // Game loop
            while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {
                BotMove direction = bot.move(gameState.getHero(),
                        this.mines,
                        this.pubs,
                        this.herosByPosition,
                        this.immutableBoardGraph);
                Move move = new Move(apiKey.getKey(), direction.toString());


                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);

                // Update the internal state of the mine ownership and hero locations
                updateMines(gameState.getGame().getBoard());
                updateHeroes(gameState.getGame().getBoard());
            }

        } catch (Exception e) {

        }
    }

    /**
     * Updates the mines map to reflect changes in ownership
     */
    private void updateMines(GameState.Board board) {
        // TODO Impl me
    }

    /**
     * Updates the heroesByPosition map to reflect changes in position
     */
    private void updateHeroes(GameState.Board board) {
        // TODO Impl me
    }

    /**
     * Parser that gets used after the initial gamestate is received
     * <p/>
     * This parser sets up a bunch of the immutable structures so that the rest of the turns can use a simpler, faster
     * parser.
     *
     * @param gameState
     */
    private void initialParse(GameState gameState) {
        // Build the graph sans edges
        GameState.Board board = gameState.getGame().getBoard();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                GameState.Position pos = new GameState.Position();
                pos.setX(row);
                pos.setY(col);

                Vertex v = new Vertex(pos, new LinkedList<Vertex>());
                int tileStart = row * board.getSize() * 2 + (col * 2);
                String tileValue = board.getTiles().substring(tileStart, tileStart + 1 + 1);

                // We do nothing with tiles that are barriers
                if (tileValue.equals("##"))
                    continue;

                this.immutableBoardGraph.put(v.getPosition(), v);

                // If its a mine or tavern, we treat it differently
                if (tileValue.startsWith("$")) {
                    String owner = tileValue.substring(1);
                    Mine mine;
                    if (owner.equals(" ")) {
                        mine = new Mine(pos, null);
                    } else {
                        int ownerId = Integer.parseInt(owner);
                        mine = new Mine(pos, this.heroById.get(ownerId));
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
        for (Vertex currentVertex : this.immutableBoardGraph.values()) {
            GameState.Position currentVertexPosition = currentVertex.getPosition();

            // Pubs and mines can't be passed through
            if(this.mines.containsKey(currentVertexPosition) || this.pubs.containsKey(currentVertexPosition))
                continue;

            for (int xDelta = -1; xDelta <= 1; xDelta += 2) {
                for (int yDelta = -1; yDelta <= 1; yDelta += 2) {
                    GameState.Position adjacentPosition = new GameState.Position();
                    adjacentPosition.setX(currentVertexPosition.getX() + xDelta);
                    adjacentPosition.setY(currentVertexPosition.getY() + yDelta);

                    Vertex adjacentVertex = this.immutableBoardGraph.get(adjacentPosition);
                    if (adjacentVertex != null)
                        currentVertex.getAdjacentVertices().add(adjacentVertex);
                }
            }
        }
    }
}
