package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ReportViewer extends Application 
{

    private final TableView<Row> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("CMSC 451 - Sort Report (FX)");

        Button openBtn = new Button("Open .dat file…");
        openBtn.setOnAction(e -> openFile(stage));

        BorderPane root = new BorderPane(table);
        root.setTop(openBtn);
        BorderPane.setMargin(openBtn, new Insets(8));

        Scene scene = new Scene(root, 820, 400);
        stage.setScene(scene);
        initTable();
        stage.show();
    }

    private void openFile(Stage stage) 
    {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Data files", "*.dat", "*.*")
        );
        File f = fc.showOpenDialog(stage);
        if (f != null) {
            try {
                table.setItems(loadRows(f));
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        }
    }

    private ObservableList<Row> loadRows(File file) throws IOException 
    {
        List<Row> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) 
        {
            String ln;
            while ((ln = br.readLine()) != null) 
            {
                StringTokenizer st = new StringTokenizer(ln);
                int n = Integer.parseInt(st.nextToken());
                double avgCnt = Double.parseDouble(st.nextToken());
                double cvCnt = Double.parseDouble(st.nextToken().replace("%", ""));
                double avgTime = Double.parseDouble(st.nextToken());
                double cvTime = Double.parseDouble(st.nextToken().replace("%", ""));
            
                list.add(new Row(n, avgCnt, cvCnt, avgTime, cvTime));
            }
            
        }
        return FXCollections.observableArrayList(list);
    }

    private static double avg(double[] a) 
    {
        double s = 0;
        for (double v : a) s += v;
        return s / a.length;
    }

    private static double cv(double[] a) 
    {
        double m = avg(a), ss = 0;
        for (double v : a) ss += (v - m) * (v - m);
        return Math.sqrt(ss / a.length) / m * 100.0;
    }

    private static double round(double value) 
    {
        return Math.round(value * 100.0) / 100.0;
    }

    private void initTable() 
    {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
                intCol("Size", Row::nProperty),
                dblCol("Avg Count", Row::avgCntProperty),
                dblCol("Coef Count %", Row::cvCntProperty),
                dblCol("Avg Time", Row::avgTimeProperty),
                dblCol("Coef Time %", Row::cvTimeProperty)
        );
    }

    private TableColumn<Row, Number> intCol(String name,
        javafx.util.Callback<Row, SimpleIntegerProperty> prop) 
        {
        TableColumn<Row, Number> c = new TableColumn<>(name);
        c.setCellValueFactory(data -> prop.call(data.getValue()));
        return c;
    }

    private TableColumn<Row, Number> dblCol(String name,
        javafx.util.Callback<Row, SimpleDoubleProperty> prop) 
        {
        TableColumn<Row, Number> c = new TableColumn<>(name);
        c.setCellValueFactory(data -> prop.call(data.getValue()));
        return c;
    }

    public static class Row 
    {
        private final SimpleIntegerProperty n;
        private final SimpleDoubleProperty avgCnt, cvCnt, avgTime, cvTime;

        Row(int n, double a1, double c1, double a2, double c2) 
        {
            this.n = new SimpleIntegerProperty(n);
            this.avgCnt = new SimpleDoubleProperty(a1);
            this.cvCnt = new SimpleDoubleProperty(c1);
            this.avgTime = new SimpleDoubleProperty(a2);
            this.cvTime = new SimpleDoubleProperty(c2);
        }

        public SimpleIntegerProperty nProperty() 
        {
            return n;
        }

        public SimpleDoubleProperty avgCntProperty() 
        {
            return avgCnt;
        }

        public SimpleDoubleProperty cvCntProperty() 
        {
            return cvCnt;
        }

        public SimpleDoubleProperty avgTimeProperty() 
        {
            return avgTime;
        }

        public SimpleDoubleProperty cvTimeProperty() 
        {
            return cvTime;
        }
    }
}
