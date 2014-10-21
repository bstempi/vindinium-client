package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.Main;
import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot;
import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class AdvancedBotRunner implements Callable<GameState> {
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
    private static final Logger logger = LogManager.getLogger(AdvancedBotRunner.class);

    private final ApiKey apiKey;
    private final GenericUrl gameUrl;
    private final AdvancedBot bot;

    public AdvancedBotRunner(ApiKey apiKey, GenericUrl gameUrl, AdvancedBot bot) {
        this.apiKey = apiKey;
        this.gameUrl = gameUrl;
        this.bot = bot;
    }

    @Override
    public GameState call() throws Exception {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState = null;
        AdvancedGameState advancedGameState;

        try {
            // Initial request
            logger.info("Sending initial request...");
            content = new UrlEncodedContent(apiKey);
            request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
            request.setReadTimeout(0); // Wait forever to be assigned to a game
            response = request.execute();
            gameState = response.parseAs(GameState.class);
            logger.info("Game URL: {}", gameState.getViewUrl());

            advancedGameState = new AdvancedGameState(gameState);

            // Game loop
            while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {
                logger.info("Taking turn " + gameState.getGame().getTurn());
                BotMove direction = bot.move(advancedGameState);
                Move move = new Move(apiKey.getKey(), direction.toString());


                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);
                advancedGameState = new AdvancedGameState(advancedGameState, gameState);
            }

        } catch (Exception e) {
            logger.error("Error during game play", e);
        }

        logger.info("Game over");
        return gameState;
    }
}
