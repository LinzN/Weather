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

package de.linzn.weather;


import de.linzn.stem.STEMApp;
import de.linzn.stem.modules.pluginModule.STEMPlugin;
import de.linzn.weather.callbacks.ESPSensorCallback;
import de.linzn.weather.data.WeatherCallback;
import de.linzn.weather.data.WeatherCommand;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.webapi.WebApiHandler;


public class WeatherPlugin extends STEMPlugin {

    public static WeatherPlugin weatherPlugin;
    private WeatherCallback weatherCallback;
    private WebApiHandler webApiHandler;

    public WeatherPlugin() {
        weatherPlugin = this;
        weatherCallback = new WeatherCallback();
    }

    @Override
    public void onEnable() {
        this.getDefaultConfig().get("weather.apiKey", "xxxxxxxxxxxxxxxxx");
        this.getDefaultConfig().get("weather.defaultLocation", "Blieskastel");
        this.getDefaultConfig().get("espMCU.sensor.use", false);
        this.getDefaultConfig().get("espMCU.sensor.address", "10.40.0.52");
        this.getDefaultConfig().save();
        STEMApp.getInstance().getCommandModule().registerCommand("weather", new WeatherCommand());
        STEMApp.getInstance().getCallBackService().registerCallbackListener(weatherCallback, this);
        if (this.getDefaultConfig().getBoolean("espMCU.sensor.use")) {
            STEMApp.getInstance().getCallBackService().registerCallbackListener(new ESPSensorCallback(), this);
        }
        this.webApiHandler = new WebApiHandler(this);
    }

    @Override
    public void onDisable() {
        STEMApp.getInstance().getCommandModule().unregisterCommand("wetter");
    }

    public WeatherContainer getWeatherData() {
        return this.weatherCallback.getWeatherContainer();
    }
}
