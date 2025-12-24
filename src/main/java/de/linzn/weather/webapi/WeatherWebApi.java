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

package de.linzn.weather.webapi;

import com.sun.net.httpserver.HttpExchange;
import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.engine.ForeCastDay;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.webapi.core.ApiResponse;
import de.linzn.webapi.core.HttpRequestClientPayload;
import de.linzn.webapi.modules.RequestInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherWebApi extends RequestInterface {
    @Override
    public Object callHttpEvent(HttpExchange httpExchange, HttpRequestClientPayload httpRequestClientPayload) throws IOException {
        ApiResponse apiResponse = new ApiResponse();

        int weatherID = -1;
        String description = "N.A";
        double current = -1;
        double minTemp = -1;
        double maxTemp = -1;
        String location = "None";
        double humidity = 0;
        double pressure = 0;
        JSONObject forecast = null;


        WeatherContainer weatherContainer = WeatherPlugin.weatherPlugin.getWeatherData();
        if (weatherContainer != null) {
            weatherID = weatherContainer.getICON();
            description = weatherContainer.getWeatherDescription();
            current = weatherContainer.getTemp();
            minTemp = weatherContainer.getTemp_min();
            maxTemp = weatherContainer.getTemp_max();
            location = weatherContainer.getLocation();
            pressure = weatherContainer.getPressure();
            humidity = weatherContainer.getHumidity();

            if (weatherContainer.hasForecast()) {
                forecast = new JSONObject();
                forecast.put("cnt", weatherContainer.getForecast().getCnt());
                forecast.put("location", weatherContainer.getForecast().getLocation());
                JSONArray days = new JSONArray();
                for (int i = 0; i < weatherContainer.getForecast().getCnt(); i++) {
                    ForeCastDay foreCastDay = weatherContainer.getForecast().getForecast(i);
                    JSONObject day = new JSONObject();
                    day.put("maxTemp", foreCastDay.getMaxTemp());
                    day.put("minTemp", foreCastDay.getMinTemp());
                    day.put("speed", foreCastDay.getSpeed());
                    day.put("clouds", foreCastDay.getClouds());
                    day.put("description", foreCastDay.getDescription());
                    day.put("main", foreCastDay.getMain());
                    day.put("date", foreCastDay.getDate());
                    day.put("dayText", new SimpleDateFormat("EEEEE", Locale.GERMAN).format(foreCastDay.getDate()));
                    days.put(day);
                }
                forecast.put("forecasts", days);

            }
        }

        apiResponse.getJSONObject().put("weatherID", weatherID);
        apiResponse.getJSONObject().put("description", description);
        apiResponse.getJSONObject().put("currentTemp", current);
        apiResponse.getJSONObject().put("minTemp", minTemp);
        apiResponse.getJSONObject().put("maxTemp", maxTemp);
        apiResponse.getJSONObject().put("location", location);
        apiResponse.getJSONObject().put("pressure", pressure);
        apiResponse.getJSONObject().put("humidity", humidity);

        if (forecast != null) {
            apiResponse.getJSONObject().put("forecast", forecast);
        }

        return apiResponse.buildResponse();
    }
}