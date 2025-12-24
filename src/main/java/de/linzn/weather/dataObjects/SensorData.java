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

package de.linzn.weather.dataObjects;


import org.json.JSONObject;

import java.util.Date;

public class SensorData {
    private static final int upToDateMinutes = 10;
    private static SensorData sensorData;
    private Date date;
    private double temperature;
    private double pressure;
    private double humidity;
    private String location;

    public SensorData(JSONObject jsonObject) {
        this.date = new Date();
        this.temperature = round(jsonObject.getDouble("temperature"), 2);
        this.pressure = round(jsonObject.getDouble("pressure"), 2);
        this.humidity = round(jsonObject.getDouble("humidity"), 2);
        this.location = "Wetterstation";
    }

    public static SensorData getLastSensorData() {
        return sensorData;
    }

    public static void setLastSensorData(SensorData data) {
        sensorData = data;
    }

    public Date getDate() {
        return this.date;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public double getPressure() {
        return this.pressure;
    }

    public double getHumidity() {
        return this.humidity;
    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }

    public boolean isUpToDate() {
        return new Date().getTime() <= this.date.getTime() + (upToDateMinutes * 60 * 1000);
    }

    public long getSecondsSinceSync() {
        long range = new Date().getTime() - this.date.getTime();
        return range / 1000;
    }

    public String getLocation() {
        return location;
    }
}
