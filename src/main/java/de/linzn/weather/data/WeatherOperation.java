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

package de.linzn.weather.data;


import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.engine.WeatherEngine;
import de.stem.stemSystem.taskManagment.operations.AbstractOperation;
import de.stem.stemSystem.taskManagment.operations.OperationOutput;
import org.json.JSONObject;

public class WeatherOperation extends AbstractOperation {
    private String location;

    @Override
    public OperationOutput runOperation() {
        OperationOutput operationOutput = new OperationOutput(this);
        String key = WeatherPlugin.weatherPlugin.getDefaultConfig().getString("weather.apiKey");
        JSONObject weatherData = new JSONObject();
        weatherData.put("current", new WeatherEngine(key).parseWeather(location));
        weatherData.put("forecast", new WeatherEngine(key).parseForecastWeather(location, 5));
        operationOutput.setData(weatherData);
        operationOutput.setExit(0);
        return operationOutput;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
