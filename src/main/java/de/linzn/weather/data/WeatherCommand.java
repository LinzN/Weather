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

import de.linzn.stem.api.StemLogger;
import de.linzn.stem.modules.commandModule.ICommand;
import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.dataObjects.SensorData;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.engine.WeatherEngine;

public class WeatherCommand implements ICommand {

    @Override
    public boolean executeTerminal(String[] strings) {
        StemLogger logger = WeatherPlugin.weatherPlugin.getPluginLogger();
        if (strings.length >= 1) {
            String location = String.join(" ", strings);
            String key = WeatherPlugin.weatherPlugin.getDefaultConfig().getString("weather.apiKey");
            WeatherContainer weatherContainer = new WeatherEngine(key).getCurrentWeather(location);
            if (weatherContainer == null) {
                logger.info("No weather data for " + location);
            } else {
                logger.info(weatherContainer.getLocation() + ": " + weatherContainer.getTemp() + "°C, " + weatherContainer.getWeatherDescription());
            }
        } else {
            SensorData sensorData = SensorData.getLastSensorData();
            if (sensorData != null) {
                logger.info("Last sync from esp sensor: " + sensorData.getSecondsSinceSync() + " seconds ago!");
            } else {
                logger.info("No data since last stem restart!");
            }
        }
        return true;
    }
}
