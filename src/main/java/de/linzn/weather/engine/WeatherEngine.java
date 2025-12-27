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

import de.linzn.stem.STEMApp;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class WeatherEngine {


    private String apiKey;

    public WeatherEngine(String apiKey) {
        this.apiKey = apiKey;
    }

    public static WeatherContainer getWeatherByJSON(JSONObject jsonObject) {
        return new WeatherContainer(jsonObject);
    }

    public static JSONObject readJsonFromUrl(String url) throws JSONException {
        try {
            URLConnection con = new URL(url).openConnection();

            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (Exception exception) {
            STEMApp.LOGGER.ERROR(exception);
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public WeatherContainer getCurrentWeather(String location) {
        return new WeatherContainer(parseWeather(location));
    }

    public JSONObject parseWeather(String location) {
        String apiLink = "https://api.openweathermap.org/data/2.5/weather?q=" + location + ",de&appid=" + this.apiKey + "&lang=de&units=metric";
        return readJsonFromUrl(apiLink);
    }

    public JSONObject parseForecastWeather(String location, int forecastDays) {
        String apiLink = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + location + ",de&cnt=" + forecastDays + "&appid=" + this.apiKey + "&lang=de&units=metric";
        return readJsonFromUrl(apiLink);
    }

}
