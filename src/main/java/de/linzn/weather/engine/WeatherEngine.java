/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.weather.engine;

import de.linzn.weather.WeatherPlugin;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * OpenWeatherMap client. Requests use timeouts and never leave connections open.
 */
public class WeatherEngine {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final String apiKey;

    public WeatherEngine(String apiKey) {
        this.apiKey = apiKey;
    }

    public static WeatherContainer getWeatherByJSON(JSONObject jsonObject) {
        return new WeatherContainer(jsonObject);
    }

    /**
     * @return the parsed JSON response or null if the request failed
     */
    public static JSONObject readJsonFromUrl(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(20)).GET().build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                WeatherPlugin.weatherPlugin.getPluginLogger().warning("Weather request failed with HTTP " + response.statusCode());
                return null;
            }
            return new JSONObject(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception exception) {
            /* the URL may contain the API key, so only the error is logged */
            WeatherPlugin.weatherPlugin.getPluginLogger().warning("Weather request failed: " + exception);
        }
        return null;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    /**
     * @return current weather or null if it could not be loaded
     */
    public WeatherContainer getCurrentWeather(String location) {
        JSONObject json = parseWeather(location);
        return json == null ? null : new WeatherContainer(json);
    }

    public JSONObject parseWeather(String location) {
        String apiLink = "https://api.openweathermap.org/data/2.5/weather?q=" + encode(location) + ",de&appid=" + encode(this.apiKey) + "&lang=de&units=metric";
        return readJsonFromUrl(apiLink);
    }

    public JSONObject parseForecastWeather(String location, int forecastDays) {
        String apiLink = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + encode(location) + ",de&cnt=" + forecastDays + "&appid=" + encode(this.apiKey) + "&lang=de&units=metric";
        return readJsonFromUrl(apiLink);
    }
}
