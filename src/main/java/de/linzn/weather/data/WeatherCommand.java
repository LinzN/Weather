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
import de.linzn.weather.dataObjects.SensorData;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.engine.WeatherEngine;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.commandModule.ICommand;

public class WeatherCommand implements ICommand {
    @Override
    public boolean executeTerminal(String[] strings) {
        if (strings.length >= 1) {
            String location = strings[0];
            String key = WeatherPlugin.weatherPlugin.getDefaultConfig().getString("weather.apiKey");
            WeatherContainer weatherContainer = new WeatherEngine(key).getCurrentWeather(location);
            STEMSystemApp.LOGGER.LIVE(weatherContainer.printData());
        } else {
            if (SensorData.getLastSensorData() != null) {
                long s = SensorData.getLastSensorData().getSecondsSinceSync();
                STEMSystemApp.LOGGER.LIVE("Last sync from esp sensor: ");
                STEMSystemApp.LOGGER.LIVE(s + " seconds ago!");
            } else {
                STEMSystemApp.LOGGER.LIVE("No data since last stem restart!");
            }
        }

        return true;
    }
}
