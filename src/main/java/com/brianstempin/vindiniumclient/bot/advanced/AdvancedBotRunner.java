package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.Main;
import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot;
import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(AdvancedBotRunner.class);
    private static final Logger gameStateLogger = LogManager.getLogger("gameStateLogger");

    private final ApiKey apiKey;
    private final Class<? extends AdvancedBot> botClass;
    private final GenericUrl gameUrl;
    private final AdvancedBot bot;

    // TODO REMOVE THIS -- this is not meant to be perm
    public static void main(String[] args) throws Exception {
        Class<AdvancedMurderBot> botClass = AdvancedMurderBot.class;
        // TODO Burn this key and never use it again.
        ApiKey apiKey = new ApiKey("ch76chvi");
        AdvancedBotRunner runner = new AdvancedBotRunner(apiKey, botClass, Main.VindiniumUrl.getTrainingUrl());
        runner.run();
    }

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
            logger.info("Sending initial request...");
            content = new UrlEncodedContent(apiKey);
            request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
            request.setReadTimeout(0); // Wait forever to be assigned to a game
            response = request.execute();
            gameState = response.parseAs(GameState.class);
            gameStateLogger.info(gson.toJson(gameState));
            logger.info(gameState.getViewUrl());

            AdvancedGameState advancedGameState = new AdvancedGameState(gameState);

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
                gameStateLogger.info(gson.toJson(gameState));
            }

        } catch (Exception e) {
            logger.error("Error during game play", e);
        }

        logger.info("Game over");
    }
}
