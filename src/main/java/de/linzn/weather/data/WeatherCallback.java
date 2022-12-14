/*
 * Copyright (C) 2020. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.weather.data;


import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.engine.WeatherEngine;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.taskManagment.AbstractCallback;
import de.stem.stemSystem.taskManagment.CallbackTime;
import de.stem.stemSystem.taskManagment.operations.OperationOutput;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class WeatherCallback extends AbstractCallback {

    private WeatherContainer weatherContainer = null;

    @Override
    public void operation() {
        String location = WeatherPlugin.weatherPlugin.getDefaultConfig().getString("weather.defaultLocation");
        WeatherOperation weatherOperation = new WeatherOperation();
        weatherOperation.setLocation(location);
        addOperationData(weatherOperation);

    }

    @Override
    public void callback(OperationOutput operationOutput) {
        JSONObject weatherObject = (JSONObject) operationOutput.getData();
        JSONObject weatherCurrent = weatherObject.getJSONObject("current");
        JSONObject weatherForecast = weatherObject.getJSONObject("forecast");
        weatherContainer = WeatherEngine.getWeatherByJSON(weatherCurrent);
        weatherContainer.setForecast(weatherForecast);
        STEMSystemApp.LOGGER.DEBUG("Weather pull complete");
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(1, 15, TimeUnit.MINUTES);
    }

    public WeatherContainer getWeatherContainer() {
        return weatherContainer;
    }
}
