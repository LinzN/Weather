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

package de.linzn.weather;


import de.linzn.weather.callbacks.ESPSensorCallback;
import de.linzn.weather.data.WeatherCallback;
import de.linzn.weather.data.WeatherCommand;
import de.linzn.weather.engine.WeatherContainer;
import de.linzn.weather.restfulapi.GET_Weather;
import de.linzn.weather.restfulapi.POST_WeatherSensorData;
import de.linzn.weather.webapi.WebApiHandler;
import de.linzn.restfulapi.RestFulApiPlugin;
import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.modules.pluginModule.STEMPlugin;


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
        STEMSystemApp.getInstance().getCommandModule().registerCommand("weather", new WeatherCommand());
        STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(weatherCallback, this);
        if (this.getDefaultConfig().getBoolean("espMCU.sensor.use")) {
            STEMSystemApp.getInstance().getCallBackService().registerCallbackListener(new ESPSensorCallback(), this);
        }
        RestFulApiPlugin.restFulApiPlugin.registerIGetJSONClass(new GET_Weather(this));
        RestFulApiPlugin.restFulApiPlugin.registerIPostJSONClass(new POST_WeatherSensorData());
        this.webApiHandler = new WebApiHandler(this);
    }

    @Override
    public void onDisable() {
        STEMSystemApp.getInstance().getCommandModule().unregisterCommand("wetter");
    }

    public WeatherContainer getWeatherData() {
        return this.weatherCallback.getWeatherContainer();
    }
}
