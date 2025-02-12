package model;

import chess.ChessGame;

// TODO: how to show as a list of games?
public record ListGamesResult(
        int gameId,
        String whiteUsername,
        String blackUsername,
        String gameName,
        ChessGame game) {
}
