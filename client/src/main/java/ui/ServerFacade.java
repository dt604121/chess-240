package ui;

import com.google.gson.Gson;
import exception.ResponseException;

import java.io.*;
import java.net.*;


import exception.ResponseException;
import model.GameData;
import model.UserData;
import server.Server;

import java.net.URI;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public UserData registerUser(UserData user) throws ResponseException{
        var path = "/user";
        return this.makeRequest("POST", path, user, UserData.class);
    }

    public UserData loginUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, UserData.class);
    }

    public UserData logoutUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, user, UserData.class);
    }

    public GameData listGames(GameData game) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, game, GameData.class);
    }

    public GameData createGames(GameData game) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class);
    }

    public GameData joinGame(GameData game) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, game, GameData.class);
    }

    public static void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
