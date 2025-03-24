package ui;

import com.google.gson.Gson;
import exception.ResponseException;

import java.io.*;
import java.net.*;

import model.*;

import java.net.URI;

public class ServerFacade {
    private final String serverUrl;
    private static String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public RegisterResult registerUser(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, RegisterResult.class);
    }

    public LoginResult loginUser(LoginRequest user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, LoginResult.class);
    }

    public void logoutUser(UserData user) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, user, UserData.class);
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public CreateGameResult createGames(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public GameData joinGame(JoinGamesRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, request, GameData.class);
    }

    public GameData observeGame(int gameId) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, gameId, GameData.class);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(http);
            writeBody(request, http);

            http.connect();

            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    // this is where we use the results from the headers and actually save stuff e.g. the authTokens
    private static void writeHeader(HttpURLConnection http) throws IOException {
        String authToken = http.getHeaderField("Authorization");

        if (authToken != null) {
            ServerFacade.setAuthToken(authToken);
        }

    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
