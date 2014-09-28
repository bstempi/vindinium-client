package com.brianstempin.vindiniumclient;

import com.brianstempin.vindiniumclient.bot.Bot;
import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.GsonBuilder;

/**
 * CLI program for launching a bot
 */
public class Main {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();

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
        Bot bot;

        try {
            Class<?> clazz = Class.forName(botClass);
            Class<? extends Bot> botClazz = clazz.asSubclass(Bot.class);
            bot = botClazz.newInstance();

            initialRequest = requestFactory.buildPostRequest(gameUrl, content);
            initialRequest.setReadTimeout(30 * 1000);
            initialResponse = initialRequest.execute();
            gameState = initialResponse.parseAs(GameState.class);

            System.out.println("Game view URL:");
            System.out.println(gameState.getViewUrl());

            while(!gameState.getGame().isFinished()) {
                BotMove direction = bot.move(gameState);
                Move move = new Move(key, direction.toString());

                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = requestFactory.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);
            }

            System.out.println("Done");
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(gameState));
        } catch (Exception e) {
            System.out.println("Error generating request:");
            e.printStackTrace();
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
