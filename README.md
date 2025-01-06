# WeatherApp

WeatherApp is a simple Java application that fetches and displays weather data for a given location. It uses the **Open-Meteo API** to retrieve weather information and presents it in a graphical user interface (GUI) built with **Swing**.

---

## Project Structure

```
├── .gitignore
├── .idea/
├── lib/
├── src/
│   ├── AppLauncher.class
│   ├── AppLauncher.java
│   ├── assets/
│   ├── WeatherApp.java
│   ├── WeatherAppGui.class
│   ├── WeatherAppGui.java
├── WeatherApp.iml
```

---

## Prerequisites

- **Java Development Kit (JDK) 18** 
- An active **internet connection** to fetch weather data from the Open-Meteo API

---

## Running the Application

### 1. Compile the Java Source Files
Run the following command to compile the source files:
```
javac -cp lib/json-simple-1.1.1.jar src/*.java
```

### 2. Run the Application
Execute the application with:
```
java -cp lib/json-simple-1.1.1.jar:src AppLauncher
```

---

## Usage

1. Enter a location name in the search bar and click the **Search** button.
2. The application will fetch and display the current weather data for the specified location, including:
   - Temperature
   - Weather condition
   - Humidity
   - Wind speed
3. Use the combo boxes to:
   - Switch between **Celsius** and **Fahrenheit** for temperature.
   - Switch between **km/h** and **mph** for wind speed.
4. The search history is displayed at the bottom of the window.

---

## Project Files

- **`WeatherApp.java`**: Contains the logic to fetch weather data from the Open-Meteo API.
- **`AppLauncher.java`**: Launches the GUI application.
- **`WeatherAppGui.java`**: Implements the graphical user interface for the application.
- **`json-simple-1.1.1.jar`**: JSON parsing library used to parse API responses.

---

## Acknowledgements

- [Open-Meteo API](https://open-meteo.com/) for providing weather data.
- [JSON.simple](https://code.google.com/archive/p/json-simple/) for JSON parsing.

---
