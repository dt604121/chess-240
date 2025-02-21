package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    // Key: ID Value: GameDataRecord
    final private HashMap<Integer, GameData> gameDataTable = new HashMap<>();

    @Override
    public ListGamesResult listGames(Object listGamesRequest) {
        // TODO: implement listGames
        return null;
    }

    @Override
    public void addGame(GameData gameData) {
        gameDataTable.put(gameData.gameId(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDataTable.containsKey(gameID)) {
            return gameDataTable.get(gameID);
        }
        return null;
    }

    // TODO: how do we update the team color?
    @Override
    public void updateGame(GameData gameData) {
        gameDataTable.put(gameData.gameId(), gameData);
    }

    @Override
    public void clearGameDAO(){
        gameDataTable.clear();
    }

}