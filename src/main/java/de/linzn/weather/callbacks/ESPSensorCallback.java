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

package de.linzn.weather.callbacks;

import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.OperationOutput;
import de.linzn.weather.WeatherPlugin;
import de.linzn.weather.dataObjects.SensorData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ESPSensorCallback extends AbstractCallback {

    @Override
    public void operation() {
        addOperationData(new ESPSensorRequestOperation());
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        if (operationOutput.getExit() != 0 || !(operationOutput.getData() instanceof JSONObject jsonObject)) {
            WeatherPlugin.weatherPlugin.getPluginLogger().warning("ESP sensor request failed with exit " + operationOutput.getExit());
            return;
        }
        try {
            SensorData.setLastSensorData(new SensorData(jsonObject));
            WeatherPlugin.weatherPlugin.getPluginLogger().debug("ESP sensor data: " + jsonObject);
        } catch (JSONException e) {
            WeatherPlugin.weatherPlugin.getPluginLogger().warning("Invalid ESP sensor data: " + jsonObject);
        }
    }

    @Override
    public CallbackTime getTime() {
        return new CallbackTime(30, 60, TimeUnit.SECONDS);
    }
}
