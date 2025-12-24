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

package de.linzn.weather.restfulapi;


import de.linzn.weather.dataObjects.SensorData;
import de.linzn.restfulapi.api.jsonapi.IRequest;
import de.linzn.restfulapi.api.jsonapi.RequestData;
import de.stem.stemSystem.STEMSystemApp;
import org.json.JSONObject;

public class POST_WeatherSensorData implements IRequest {

    @Override
    public Object proceedRequestData(RequestData requestData) {
        JSONObject jsonObject = new JSONObject();
        String post_data = requestData.getSubChannels().get(0);

        for (String data : post_data.split("&")) {
            String name = data.split("=")[0];
            Object value = data.split("=")[1];
            jsonObject.put(name, value);
        }

        STEMSystemApp.LOGGER.DEBUG("Get ESP sensor POST-data: " + jsonObject);
        SensorData sensorData = new SensorData(jsonObject);
        SensorData.setLastSensorData(sensorData);

        return jsonObject;
    }

    @Override
    public String name() {
        return "weather-sensor-data";
    }
}
