package model;

import chess.ChessGame;

import java.util.List;

public record ListGamesResult(
        List games,
        GameData gameData) {
}
