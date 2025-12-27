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


import de.linzn.stem.STEMApp;
import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.OperationOutput;
import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.engine.WeatherEngine;
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
        STEMApp.LOGGER.DEBUG("Weather pull complete");
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(1, 15, TimeUnit.MINUTES);
    }

    public WeatherContainer getWeatherContainer() {
        return weatherContainer;
    }
}
