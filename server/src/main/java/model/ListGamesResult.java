package model;

// TODO: how to show as a list of games?
public record ListGamesResult(
        String gameId,
        String whiteUsername,
        String blackUsername,
        String gameName) {
}
