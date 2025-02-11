package model;

public record JoinGamesRequest(
        String playerColor,
        int gameID,
        String authToken) {
}
