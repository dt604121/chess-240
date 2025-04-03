package ui;

import com.google.gson.Gson;
import exception.ResponseException;

import java.io.*;
import java.net.*;

import model.*;

import java.net.URI;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public RegisterResult registerUser(UserData user) throws ResponseException {
        var path = "/user";

        try {
            RegisterResult registerResult = this.makeRequest("POST", path, user, RegisterResult.class);
            if (user.username().isEmpty() || user.email().isEmpty() || user.password().isEmpty()) {
                throw new ResponseException("Registration failed: Username, email, and password are required.");
            }
            authToken = registerResult.authToken();
            return registerResult;
        } catch (ResponseException e) {
            if (e.getMessage().contains("already taken")) {
                throw new ResponseException("Registration failed: Error: already taken");
            }
            throw e;
        }
    }

    public LoginResult loginUser(LoginRequest user) throws ResponseException {
        var path = "/session";
        LoginResult loginResult = this.makeRequest("POST", path, user, LoginResult.class);
        authToken = loginResult.authToken();
        return loginResult;
    }

    public void logoutUser(UserData user) throws ResponseException {
        var path = "/session";

        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException("You must sign in.");
        }

        this.makeRequest("DELETE", path, user, UserData.class);
        authToken = null;
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException("You must sign in.");
        }
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public CreateGameResult createGames(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        if (request.gameName().isEmpty()) {
            throw new ResponseException("Invalid game name. Cannot be left blank.");
        }
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException("You must sign in.");
        }
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public GameData joinGame(JoinGamesRequest request) throws ResponseException {
        var path = "/game";
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException("You must sign in.");
        }
        return this.makeRequest("PUT", path, request, GameData.class);
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
            throw new ResponseException(ex.getMessage());
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
    private void writeHeader(HttpURLConnection http) {
        if (authToken != null && !authToken.isEmpty()) {
            http.addRequestProperty("Authorization", authToken);
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

            throw new ResponseException("other failure: ");
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
