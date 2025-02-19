package model;

import chess.ChessGame;

public record ListGamesResult(
        int gameId,
        String whiteUsername,
        String blackUsername,
        String gameName,
        ChessGame game) {
}
