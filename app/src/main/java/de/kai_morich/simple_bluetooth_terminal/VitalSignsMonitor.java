package de.kai_morich.simple_bluetooth_terminal;


//ploteo 5 segundos

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class VitalSignsMonitor {
    //DEFINEs
    private static final int TIME_TO_PLOT = 5; //in seconds
    private static final int FS = 200; //Hz

    private static Integer[] ECG_all_samples;
    private static Integer[] PPG_all_samples;

    private Integer[] ECG_new_samples;
    private Integer[] PPG_new_samples;

    private Integer HeartBeat;
    private Integer Oxygen;
    private Double Temp;

    public static Integer[] GetECGAllData() {

        return ECG_all_samples;
    }

    public static Integer[] getPPGAllData() {

        return PPG_all_samples;
    }

    public static LineGraphSeries<DataPoint> GetECGPlotData() {
        LineGraphSeries<DataPoint> ECG_new_samples = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        return ECG_new_samples;
    }

    public static LineGraphSeries<DataPoint> GetPPGPlotData() {
        LineGraphSeries<DataPoint> PPG_new_samples = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        return PPG_new_samples;
    }
}


