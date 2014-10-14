package com.brianstempin.vindiniumclient.dto;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * This is a DTO to represent the game-state as its returned by the game server
 * <p/>
 * This class does nothing but to represent a literal JSON to Java representation of the response.
 * <p/>
 * For further processing, other classes are needed.
 */
public class GameState {

    @Key
    private volatile Game game;

    @Key
    private volatile Hero hero;

    @Key
    private volatile String token;

    @Key
    private volatile String viewUrl;

    @Key
    private volatile String playUrl;

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

    public static class Game {

        @Key
        private volatile String id;

        @Key
        private volatile int turn;

        @Key
        private volatile int maxTurns;

        @Key
        private volatile List<Hero> heroes;

        @Key
        private volatile Board board;

        @Key
        private volatile boolean finished;

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

    public static class Hero {

        @Key
        private volatile int id;

        @Key
        private volatile String name;

        @Key
        private volatile String userId;

        @Key
        private volatile int elo;

        @Key
        private volatile Position pos;

        @Key
        private volatile int life;

        @Key
        private volatile int gold;

        @Key
        private volatile int mineCount;

        @Key
        private volatile Position spawnPos;

        @Key
        private volatile boolean crashed;

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

    public static class Board {

        @Key
        private volatile String tiles;

        @Key
        private volatile int size;

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

    public static class Position {

        @Key
        private volatile int x;

        @Key
        private volatile int y;

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
