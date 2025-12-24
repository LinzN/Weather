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

package de.linzn.weather.engine;

import org.json.JSONObject;

import java.util.Date;

public class ForeCastDay {
    private final JSONObject data;

    public ForeCastDay(JSONObject jsonObject) {
        data = jsonObject;
    }

    public double getMinTemp() {
        return data.getJSONObject("temp").getDouble("min");
    }

    public double getMaxTemp() {
        return data.getJSONObject("temp").getDouble("max");
    }

    public Date getDate() {
        return new Date(data.getLong("dt") * 1000);
    }

    public String getDescription() {
        return data.getJSONArray("weather").getJSONObject(0).getString("description");
    }

    public String getMain() {
        return data.getJSONArray("weather").getJSONObject(0).getString("main");
    }

    public double getClouds() {
        return data.getDouble("clouds");
    }

    public double getSpeed() {
        return data.getDouble("speed");
    }
}
