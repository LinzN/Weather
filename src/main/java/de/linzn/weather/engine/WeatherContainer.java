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

import de.linzn.weather.dataObjects.SensorData;
import org.json.JSONObject;

import java.util.Date;

public class WeatherContainer {

    private final Date date;

    private final JSONObject jsonObject;

    private ForecastContainer forecastContainer;


    public WeatherContainer(JSONObject jsonObject) {
        this.date = new Date();
        this.jsonObject = jsonObject;
    }

    public ForecastContainer getForecast() {
        return forecastContainer;
    }

    public void setForecast(JSONObject jsonObject) {
        forecastContainer = new ForecastContainer(jsonObject);
    }

    public boolean hasForecast() {
        return forecastContainer != null;
    }


    public String getLocation() {
        if (SensorData.getLastSensorData() != null && SensorData.getLastSensorData().isUpToDate()) {
            return SensorData.getLastSensorData().getLocation();
        }
        return jsonObject.getString("name");
    }

    public String getWeatherDescription() {
        return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
    }

    public String getWeatherMain() {
        return jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
    }

    public double getTemp() {
        if (SensorData.getLastSensorData() != null && SensorData.getLastSensorData().isUpToDate()) {
            return SensorData.getLastSensorData().getTemperature();
        }
        return jsonObject.getJSONObject("main").getDouble("temp");
    }

    public double getTemp_min() {
        return jsonObject.getJSONObject("main").getDouble("temp_min");
    }

    public double getTemp_max() {
        return jsonObject.getJSONObject("main").getDouble("temp_max");
    }

    public double getPressure() {
        if (SensorData.getLastSensorData() != null && SensorData.getLastSensorData().isUpToDate()) {
            return SensorData.getLastSensorData().getPressure();
        }
        return jsonObject.getJSONObject("main").getDouble("pressure");
    }

    public double getHumidity() {
        if (SensorData.getLastSensorData() != null && SensorData.getLastSensorData().isUpToDate()) {
            return SensorData.getLastSensorData().getHumidity();
        }
        return jsonObject.getJSONObject("main").getDouble("humidity");
    }

    public double getWind_speed() {
        return jsonObject.getJSONObject("wind").getDouble("speed");
    }

    public double getClouds() {
        return jsonObject.getJSONObject("clouds").getDouble("all");
    }

    public int getICON() {
        String weatherMain = getWeatherMain();
        int weatherIcon = -1;
        /* Weather icon */
        if (weatherMain.equalsIgnoreCase("clear")) {
            weatherIcon = 0;
        } else if (weatherMain.equalsIgnoreCase("clouds")) {
            weatherIcon = 1;
        } else if (weatherMain.equalsIgnoreCase("drizzle")) {
            weatherIcon = 2;
        } else if (weatherMain.equalsIgnoreCase("rain")) {
            weatherIcon = 3;
        } else if (weatherMain.equalsIgnoreCase("thunderstorm")) {
            weatherIcon = 4;
        } else if (weatherMain.equalsIgnoreCase("snow")) {
            weatherIcon = 5;
        } else if (weatherMain.equalsIgnoreCase("mist")) {
            weatherIcon = 6;
        } else if (weatherMain.equalsIgnoreCase("fog")) {
            weatherIcon = 6;
        }

        return weatherIcon;
    }

    public String printData() {
        return "";
    }

    public Date getDate() {
        if (SensorData.getLastSensorData() != null && SensorData.getLastSensorData().isUpToDate()) {
            return SensorData.getLastSensorData().getDate();
        }
        return date;
    }
}
