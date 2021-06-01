package org.lobo.census.graph;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Set;

public class DrawGraph extends Application {
    private static CountryData[] countryData;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Population Data");

        // Define the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Year");

        // Create the chart
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Countries: ");

        Scene scene  = new Scene(lineChart,1600,900);

        // Each country is a new series
        XYChart.Series[] countrySeries = new XYChart.Series[countryData.length];
        for (int i = 0; i < countryData.length; i++) {
            countrySeries[i] = new XYChart.Series();
            countrySeries[i].setName(countryData[i].getName());

            // Populate the series with population data :-)
            Set<String> keySet = countryData[i].getPopulationMap().keySet();
            for (String key : keySet) {
                countrySeries[i].getData().add(new XYChart.Data(key,
                        countryData[i].getPopulationMap().get(key)));
            }

            lineChart.getData().add(countrySeries[i]);
        }

        stage.setScene(scene);
        stage.show();
    }

    public static void draw(CountryData[] data) {
        countryData = data;
        launch();
    }
}
