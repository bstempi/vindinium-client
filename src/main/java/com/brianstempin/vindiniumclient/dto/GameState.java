package com.brianstempin.vindiniumclient.dto;

import com.google.api.client.util.Key;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * This is a DTO to represent the game-state as its returned by the game server
 * <p/>
 * This class does nothing but to represent a literal JSON to Java representation of the response.
 * <p/>
 * For further processing, other classes are needed.
 */
@Immutable
public class GameState {

    @Key
    private Game game;

    @Key
    private Hero hero;

    @Key
    private String token;

    @Key
    private String viewUrl;

    @Key
    private String playUrl;

    /**
     * Here for Gson's use
     */
    public GameState() {

    }

    public GameState(Game game, Hero hero, String token, String viewUrl, String playUrl) {
        this.game = game;
        this.hero = hero;
        this.token = token;
        this.viewUrl = viewUrl;
        this.playUrl = playUrl;
    }

    public Game getGame() {
        return game;
    }

    public Hero getHero() {
        return hero;
    }

    public String getToken() {
        return token;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    @Immutable
    public static class Game {

        @Key
        private String id;

        @Key
        private int turn;

        @Key
        private int maxTurns;

        @Key
        private List<Hero> heroes;

        @Key
        private Board board;

        @Key
        private boolean finished;

        /**
         * Here for Gson's use
         */
        public Game() {

        }

        public Game(String id, int turn, int maxTurns, List<Hero> heroes, Board board, boolean finished) {
            this.id = id;
            this.turn = turn;
            this.maxTurns = maxTurns;
            this.heroes = heroes;
            this.board = board;
            this.finished = finished;
        }

        public String getId() {
            return id;
        }

        public int getTurn() {
            return turn;
        }

        public int getMaxTurns() {
            return maxTurns;
        }

        public List<Hero> getHeroes() {
            return heroes;
        }

        public Board getBoard() {
            return board;
        }

        public boolean isFinished() {
            return finished;
        }
    }

    @Immutable
    public static class Hero {

        @Key
        private int id;

        @Key
        private String name;

        @Key
        private String userId;

        @Key
        private int elo;

        @Key
        private Position pos;

        @Key
        private int life;

        @Key
        private int gold;

        @Key
        private int mineCount;

        @Key
        private Position spawnPos;

        @Key
        private boolean crashed;

        /**
         * Here for Gson's use
         */
        public Hero() {

        }

        public Hero(int id, String name, String userId, int elo, Position pos, int life, int gold, int mineCount,
                    Position spawnPos, boolean crashed) {
            this.id = id;
            this.name = name;
            this.userId = userId;
            this.elo = elo;
            this.pos = pos;
            this.life = life;
            this.gold = gold;
            this.mineCount = mineCount;
            this.spawnPos = spawnPos;
            this.crashed = crashed;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

        public int getElo() {
            return elo;
        }

        public Position getPos() {
            return pos;
        }

        public int getLife() {
            return life;
        }

        public int getGold() {
            return gold;
        }

        public int getMineCount() {
            return mineCount;
        }

        public Position getSpawnPos() {
            return spawnPos;
        }

        public boolean isCrashed() {
            return crashed;
        }
    }

    @Immutable
    public static class Board {

        @Key
        private String tiles;

        @Key
        private int size;

        /**
         * Here for Gson's use
         */
        public Board() {

        }

        public Board(String tiles, int size) {
            this.tiles = tiles;
            this.size = size;
        }

        public String getTiles() {
            return tiles;
        }

        public int getSize() {
            return size;
        }
    }

    @Immutable
    public static class Position {

        @Key
        private int x;

        @Key
        private int y;

        /**
         * Here for Gson's use
         */
        public Position() {

        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (x != position.x) return false;
            if (y != position.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
