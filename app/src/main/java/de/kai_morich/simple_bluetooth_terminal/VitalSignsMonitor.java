package de.kai_morich.simple_bluetooth_terminal;


//ploteo 5 segundos

import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VitalSignsMonitor {
    //DEFINEs
    private static final int TIME_TO_PLOT = 5; //in seconds
    private static final int ECG_FS = 50; //Hz
    public static final int PPG_FS = 50; //Hz

    private static List<Double> ECG_samples = new ArrayList<Double>(){{add(0.0);}};
    private static List<Double> PPG_samples = new ArrayList<Double>(){{add(0.0);}};

    private static double X = -5.0; //increment counter for plotting
    //public static Integer GetSpO2(){return SpO2;}
    //public static Integer[] GetECGAllData() {return ECG_all_samples;}
    //public static Integer[] getPPGAllData() {return PPG_all_samples;}



    public static LineGraphSeries<DataPoint> GetECGInitialData() {
        LineGraphSeries<DataPoint> ECG_new_samples = new LineGraphSeries<>(new DataPoint[]{new DataPoint(-5.0, 0.0)});
        ECG_new_samples.setThickness(5);

        return ECG_new_samples;
    }

    public static LineGraphSeries<DataPoint> GetPPGInitialData() {
        LineGraphSeries<DataPoint> PPG_new_samples = new LineGraphSeries<>(new DataPoint[]{new DataPoint(-5.0, 0.0)});
        return PPG_new_samples;
    }

    public static void HeartBeat(byte heart_rate) {
        int bpm = (int) heart_rate & 0xFF;
        VitalSignsMonitorFragment.text_bpm.setText(String.valueOf(bpm)+" BPM");
    }

    public static void Oxygen(byte oxygen) {
        int spO2 = (int) oxygen & 0xFF;
        VitalSignsMonitorFragment.text_spo2.setText(String.valueOf(spO2)+"%");
    }

    public static void Temperature(byte part_entera, byte part_decimal) {
        double temp = (int) part_entera & 0xff;
        temp +=  part_decimal/10.0;
        VitalSignsMonitorFragment.text_temp.setText(String.valueOf(temp) + "°C");
    }

    public static void UpdateECGGraph(byte[] bytes, int len) {
        for (int i =0; i<len/2;i++) {
            byte b1 = bytes[i];
            byte b2 = bytes[i+1];
            int i1 = 0xFF & b1; // Consider b1 as int, not the same as "(int) b1"
            int i2 = 0xFF & b2; // Consider b2 as int, not the same as "(int) b2"
            Double curr_sample = i2 * 256.0 + i1;


            //Roto la lista de samples, remove el ultimo elemento(que es mas viejo ahora) y agrego la muestra actual
            if(ECG_samples.size()>0){
                //Collections.rotate(ECG_samples, 1);
                ECG_samples.add(ECG_samples.size()-1, curr_sample);
                if(ECG_samples.size()==2 && ECG_samples.get(0) == 0)
                    ECG_samples.remove(1);
                if(ECG_samples.size()>ECG_FS)
                    ECG_samples.remove(0);
            }

            //preparo los datapoints para plotear
            DataPoint[] dataPoints = new DataPoint[ECG_samples.size()]; // declare an array of DataPoint objects with the same size as your list
            double step = 1.0/(ECG_FS);
            //double time = -5.0;
            for (int j = 0; j < ECG_samples.size(); j++) {
                // add new DataPoint object to the array for each of your list entries
                double sample = ECG_samples.get(j);
                double time = -5.0+step*j;
                dataPoints[j] = new DataPoint(time, ECG_samples.get(j)); // not sure but I think the second argument should be of type double
                //time = -5.0 + step*j ;
            }

            VitalSignsMonitorFragment.ECGDataPoints.resetData(dataPoints);
            //VitalSignsMonitorFragment.ECGDataPoints = new LineGraphSeries<DataPoint>(dataPoints);
            VitalSignsMonitorFragment.ECGgraph.addSeries(VitalSignsMonitorFragment.ECGDataPoints);




            //todo: pasar a doubleECGgraph.getViewport().setMinX(-5.1);
            VitalSignsMonitorFragment.ECGgraph.getViewport().setMaxX(0.5);
            VitalSignsMonitorFragment.ECGgraph.getViewport().setMinY(VitalSignsMonitorFragment.ECGDataPoints.getLowestValueY()*1.1);
            VitalSignsMonitorFragment.ECGgraph.getViewport().setMaxY(VitalSignsMonitorFragment.ECGDataPoints.getHighestValueY()*1.1);
            VitalSignsMonitorFragment.ECGgraph.getViewport().setYAxisBoundsManual(true);
            VitalSignsMonitorFragment.ECGgraph.getViewport().setXAxisBoundsManual(true);



            //VitalSignsMonitorFragment.ECGDataPoints.appendData(new DataPoint(X, n), true, ECG_FS, true);
            //VitalSignsMonitorFragment.ECGgraph.addSeries(VitalSignsMonitorFragment.ECGDataPoints);
        }
    }

    public static void UpdatePPGGraph(byte[] copyOfRange) {
    }
}


