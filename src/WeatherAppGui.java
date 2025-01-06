import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame {
    private JLabel temperatureLabel;
    private JLabel windSpeedLabel;
    private JComboBox<String> temperatureUnitComboBox;
    private JComboBox<String> windSpeedUnitComboBox;
    private DefaultListModel<String> searchHistoryModel;
    private JList<String> searchHistoryList;

    public WeatherAppGui() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 900);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(30, 15, 340, 45);
        searchTextField.setFont(new Font("SF Pro", Font.BOLD, 18));
        add(searchTextField);

        JLabel weatherImage = new JLabel(loadImage("/assets/sunny.png", 150, 150));
        weatherImage.setBounds((getWidth() - 150) / 2, 125, 150, 150);
        add(weatherImage);

        temperatureLabel = new JLabel("25°C");
        temperatureLabel.setFont(new Font("SF Pro", Font.BOLD, 50));
        temperatureLabel.setBounds(0, 300, 450, 50);
        temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureLabel);

        JLabel weatherConditionLabel = new JLabel("Sunny");
        weatherConditionLabel.setFont(new Font("SF Pro", Font.PLAIN, 25));
        weatherConditionLabel.setBounds(0, 365, 450, 50);
        weatherConditionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionLabel);

        JLabel humidityImage = new JLabel(loadImage("/assets/humidity.png", 50, 50));
        humidityImage.setBounds(30, 450, 50, 50);
        add(humidityImage);

        JLabel humidityLabel = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityLabel.setFont(new Font("SF Pro", Font.PLAIN, 20));
        humidityLabel.setBounds(90, 450, 150, 75);
        add(humidityLabel);

        JLabel windSpeedImage = new JLabel(loadImage("/assets/windspeed.png", 50, 50));
        windSpeedImage.setBounds(240, 450, 50, 50);
        add(windSpeedImage);

        windSpeedLabel = new JLabel("<html><b>Wind Speed</b><br>10 km/h</html>");
        windSpeedLabel.setFont(new Font("SF Pro", Font.PLAIN, 20));
        windSpeedLabel.setBounds(300, 450, 180, 75);
        add(windSpeedLabel);

        // Temperature unit combo box
        String[] temperatureUnits = {"Celsius", "Fahrenheit"};
        temperatureUnitComboBox = new JComboBox<>(temperatureUnits);
        temperatureUnitComboBox.setBounds((getWidth() - 150) / 2, 350, 150, 30);
        temperatureUnitComboBox.addActionListener(e -> updateTemperatureUnit());
        add(temperatureUnitComboBox);

        // Wind speed unit combo box
        String[] windSpeedUnits = {"km/h", "mph"};
        windSpeedUnitComboBox = new JComboBox<>(windSpeedUnits);
        windSpeedUnitComboBox.setBounds(300, 520, 150, 30);
        windSpeedUnitComboBox.addActionListener(e -> updateWindSpeedUnit());
        add(windSpeedUnitComboBox);

        // Search history list
        searchHistoryModel = new DefaultListModel<>();
        searchHistoryList = new JList<>(searchHistoryModel);
        JScrollPane scrollPane = new JScrollPane(searchHistoryList);
        scrollPane.setBounds(30, 550, 400, 300);
        add(scrollPane);

        JButton searchButton = new JButton(loadImage("/assets/search.png", 36, 36));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 15, 45, 45);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);

        searchButton.addActionListener(e -> {
            String userInput = searchTextField.getText().trim();
            if (userInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a location!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSONObject weatherData = WeatherApp.getWeatherData(userInput);

            if (weatherData == null) {
                JOptionPane.showMessageDialog(this, "Could not fetch weather data. Please try again.", "API Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SwingUtilities.invokeLater(() -> {
                String weatherCondition = (String) weatherData.getOrDefault("weather_condition", "Unknown");
                switch (weatherCondition) {
                    case "Clear":
                        weatherImage.setIcon(loadImage("/assets/clear.png", 150, 150));
                        break;
                    case "Cloudy":
                        weatherImage.setIcon(loadImage("/assets/cloudy.png", 150, 150));
                        break;
                    case "Rain":
                        weatherImage.setIcon(loadImage("/assets/rain.png", 150, 150));
                        break;
                    case "Snow":
                        weatherImage.setIcon(loadImage("/assets/snow.png", 150, 150));
                        break;
                    default:
                        weatherImage.setIcon(loadImage("/assets/unknown.png", 150, 150)); // Default icon
                        break;
                }

                double temperature = (double) weatherData.getOrDefault("temperature", -1.0);
                temperatureLabel.setText(temperature + "°C");

                weatherConditionLabel.setText(weatherCondition);

                long humidity = (long) weatherData.getOrDefault("humidity", -1L);
                humidityLabel.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                double windspeed = (double) weatherData.getOrDefault("windspeed", -1.0);
                windSpeedLabel.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                searchHistoryModel.addElement(userInput + " - " + timestamp);
            });
        });

        add(searchButton);
    }

    private ImageIcon loadImage(String resourcePath, int width, int height) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            BufferedImage image = ImageIO.read(is);
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not find resource: " + resourcePath);
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }
    }

    private void updateWindSpeedUnit() {
        String selectedUnit = (String) windSpeedUnitComboBox.getSelectedItem();
        double windSpeed = 10;
        if ("mph".equals(selectedUnit)) {
            windSpeed = windSpeed * 0.621371;
            windSpeedLabel.setText(String.format("<html><b>Wind Speed</b><br>%.1f mph</html>", windSpeed));
        } else {
            windSpeedLabel.setText(String.format("<html><b>Wind Speed</b><br>%.1f km/h</html>", windSpeed));
        }
    }

    private void updateTemperatureUnit() {
        String selectedUnit = (String) temperatureUnitComboBox.getSelectedItem();
        double temperature = 25;
        if ("Fahrenheit".equals(selectedUnit)) {
            temperature = temperature * 9 / 5 + 32;
            temperatureLabel.setText(String.format("%.1f°F", temperature));
        } else {
            temperatureLabel.setText(String.format("%.1f°C", temperature));
        }
    }
}
