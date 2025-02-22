package model;

import chess.ChessGame;

import java.util.Collection;
import java.util.List;

public record ListGamesResult(
        Collection<GameData> games) {
}
