![MirraNET Logo](https://raw.githubusercontent.com/LinzN/Stem/master/stem-core/src/main/resources/header-logo.png)

# weather

> Weather data and forecast for [STEM](https://github.com/LinzN/Stem) from OpenWeatherMap.

weather loads the current weather and a five day forecast for a default location every 15 minutes and
offers it through [web-api](https://github.com/LinzN/web-api). Optionally it also reads a local ESP sensor.
Other plugins, for example [mirra](https://github.com/LinzN/mirra), use it to look up the weather of any town.

---

## Features

- Current weather and 5 day forecast for the default location, updated every 15 minutes
- Weather of any location on demand (console command and API)
- Optional ESP sensor (temperature, pressure, humidity), read every 60 seconds
- Failed requests keep the previous data

Requests use `lang=de`, `units=metric` and search locations in Germany (`,de`).

---

## Requirements

| Requirement | Version                                                     |
|-------------|-------------------------------------------------------------|
| STEM        | 1.2.0 or newer (`stem-api`)                                 |
| Plugins     | [web-api](https://github.com/LinzN/web-api)                  |
| API key     | [OpenWeatherMap](https://openweathermap.org/) key with access to the daily forecast |

---

## Installation

1. Build the plugin with `mvn clean package` (or take the build from Jenkins).
2. Copy `target/weather.jar` into the `plugins` folder of STEM.
3. Start STEM once to create `plugins/weather/config.yml`.
4. Enter your API key and location, then restart STEM (plugins are only loaded at startup).

---

## Configuration (`config.yml`)

| Key                       | Default             | Description                                  |
|---------------------------|---------------------|----------------------------------------------|
| `weather.apiKey`          | `xxxxxxxxxxxxxxxxx` | OpenWeatherMap API key                       |
| `weather.defaultLocation` | `Blieskastel`       | Location for the regular update              |
| `espMCU.sensor.use`       | `false`             | Read the ESP sensor                          |
| `espMCU.sensor.address`   | `10.40.0.52`        | Address of the ESP sensor (`http://<address>`) |

The ESP sensor must answer with JSON containing `temperature`, `pressure` and `humidity`.

---

## Commands

| Command              | Description                                   |
|----------------------|-----------------------------------------------|
| `weather <location>` | Current weather of a location                 |
| `weather`            | Time since the last data of the ESP sensor    |

---

## Web API

`/weather/current` returns the data of the default location:

`weatherID` (icon), `description`, `currentTemp`, `minTemp`, `maxTemp`, `location`, `pressure`,
`humidity` and, if available, `forecast` with one entry per day (`minTemp`, `maxTemp`, `speed`,
`clouds`, `main`, `description`, `date`, `dayText`).

---

## API for other plugins

```java
WeatherContainer current = WeatherPlugin.weatherPlugin.getWeatherData();      // default location, may be null

String key = WeatherPlugin.weatherPlugin.getDefaultConfig().getString("weather.apiKey");
WeatherContainer other = new WeatherEngine(key).getCurrentWeather("Berlin");  // null if not available
```

Both methods return `null` when no data is available, so always check the result.

---

## License

Copyright (c) MirraNET, Niklas Linz. Licensed under the
[GNU Lesser General Public License v3.0](https://www.gnu.org/licenses/lgpl-3.0.html).
