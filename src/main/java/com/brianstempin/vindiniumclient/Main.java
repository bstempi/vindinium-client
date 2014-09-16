package com.brianstempin.vindiniumclient;

import com.brianstempin.vindiniumclient.dto.ApiKey;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

/**
 * CLI program for launching a bot
 */
public class Main {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();

    /**
     * Represents the endpoint URL
     */
    public static class VindiniumUrl extends GenericUrl {
        private final static String TRAINING_URL = "http://vindinium.org/api/training";
        private final static String COMPETITION_URL = "http://vindinium.org/api/arena";

        public static VindiniumUrl getCompetitionUrl() {
            return new VindiniumUrl(COMPETITION_URL);
        }

        public static VindiniumUrl getTrainingUrl() {
            return new VindiniumUrl(TRAINING_URL);
        }

        public VindiniumUrl(String encodedUrl) {
            super(encodedUrl);
        }
    }

    public static void main(String args[]) {

        final String key = args[0];
        final String arena = args[1];
        final String games = args[2];

        final GenericUrl gameUrl;

        if("TRAINING".equals(arena))
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
        final GameState initialGameState;

        try {
            initialRequest = requestFactory.buildPostRequest(VindiniumUrl.getTrainingUrl(), content);
            initialResponse = initialRequest.execute();
            initialGameState = initialResponse.parseAs(GameState.class);

            System.out.println(initialGameState.getViewUrl());
        } catch(Exception e) {
            System.out.println("Error generating request:");
            e.printStackTrace();
        }
    }
}
