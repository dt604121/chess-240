package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    // Key: ID Value: GameDataRecord
    final private HashMap<Integer, GameData> gameDataTable = new HashMap<>();

    @Override
    public ListGamesResult listGames(Object listGamesRequest) {
        return null;
    }

    @Override
    public GameData createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
                               ChessGame game) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public void addGame(GameData gameData) {
        gameDataTable.put(gameData.gameId(), gameData);
    }

    @Override
    public JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDataTable.containsKey(gameID)) {
            return gameDataTable.get(gameID);
        }
        return null;
    }

    @Override
    public GameData updateGame(GameData gameData) {
        return null;
    }

    @Override
    public void clearGameDAO(){
        gameDataTable.clear();
    }

}