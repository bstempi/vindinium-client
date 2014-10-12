package com.brianstempin.vindiniumclient;

import com.brianstempin.vindiniumclient.bot.simple.SimpleBot;
import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CLI program for launching a bot
 */
public class Main {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Logger gameStateLogger = LogManager.getLogger("gameStateLogger");

    public static void main(String args[]) {

        final String key = args[0];
        final String arena = args[1];
        final String botClass = args[2];

        final GenericUrl gameUrl;

        if ("TRAINING".equals(arena))
            gameUrl = VindiniumUrl.getTrainingUrl();
        else if ("COMPETITION".equals(arena))
            gameUrl = VindiniumUrl.getCompetitionUrl();
        else
            gameUrl = new VindiniumUrl(arena);


        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        final ApiKey apiKey = new ApiKey(key);
        final HttpContent content = new UrlEncodedContent(apiKey);
        final HttpRequest initialRequest;
        final HttpResponse initialResponse;

        GameState gameState;
        SimpleBot bot;

        try {
            Class<?> clazz = Class.forName(botClass);
            Class<? extends SimpleBot> botClazz = clazz.asSubclass(SimpleBot.class);
            bot = botClazz.newInstance();

            initialRequest = requestFactory.buildPostRequest(gameUrl, content);
            initialRequest.setReadTimeout(0); // Wait forever
            initialResponse = initialRequest.execute();
            gameState = initialResponse.parseAs(GameState.class);

            gameStateLogger.info(gson.toJson(gameState));
            logger.info("Game view URL: " + gameState.getViewUrl());

            while(!gameState.getGame().isFinished()) {
                BotMove direction = bot.move(gameState);
                Move move = new Move(key, direction.toString());

                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = requestFactory.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);
                gameStateLogger.info(gson.toJson(gameState));
            }

            logger.info("Game over");
        } catch (Exception e) {
            logger.error("Error generating request.", e);
        }
    }

    /**
     * Represents the endpoint URL
     */
    public static class VindiniumUrl extends GenericUrl {
        private final static String TRAINING_URL = "http://vindinium.org/api/training";
        private final static String COMPETITION_URL = "http://vindinium.org/api/arena";

        public VindiniumUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public static VindiniumUrl getCompetitionUrl() {
            return new VindiniumUrl(COMPETITION_URL);
        }

        public static VindiniumUrl getTrainingUrl() {
            return new VindiniumUrl(TRAINING_URL);
        }
    }
}
