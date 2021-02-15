package de.kai_morich.simple_bluetooth_terminal;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.kai_morich.simple_bluetooth_terminal.ui.main.SectionsPagerAdapter;

public class VitalSignsMonitorFragment extends Fragment {

    private enum Graph_t {
        ECG,
        PPG
    }

    public static TextView text_spo2;
    public static TextView text_temp;
    public static TextView text_bpm;
    public static GraphView ECGgraph;
    public static GraphView PPGgraph;
    public static LineGraphSeries<DataPoint> ECGDataPoints;
    public static LineGraphSeries<DataPoint> PPGDataPoints;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.vital_signs_monitor_layout, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Bundle args = getArguments();

        onCreateViewGraphs(view, Graph_t.ECG);
        onCreateViewGraphs(view, Graph_t.PPG);



        ECGDataPoints = VitalSignsMonitor.GetECGInitialData();
        ECGgraph.addSeries(ECGDataPoints);

        PPGDataPoints = VitalSignsMonitor.GetPPGInitialData();
        PPGgraph.addSeries(PPGDataPoints);

        //ECGgraph.getViewport().setMinX(-5.1);
        ECGgraph.getViewport().setMaxX(1.0);
        ECGgraph.getViewport().setMinY(ECGDataPoints.getLowestValueY()*1.1);
        ECGgraph.getViewport().setMaxY(ECGDataPoints.getHighestValueY()*1.1);
        ECGgraph.getViewport().setYAxisBoundsManual(true);
        ECGgraph.getViewport().setXAxisBoundsManual(true);

        PPGgraph.getViewport().setMaxX(1.0);
        PPGgraph.getViewport().setMinY(PPGDataPoints.getLowestValueY()*1.1);
        PPGgraph.getViewport().setMaxY(PPGDataPoints.getHighestValueY()*1.1);
        PPGgraph.getViewport().setYAxisBoundsManual(true);
        PPGgraph.getViewport().setXAxisBoundsManual(true);


        text_spo2 = (TextView) view.findViewById(R.id.text_SpO2);
        text_spo2.setText("- %");
        text_temp = (TextView) view.findViewById(R.id.text_temp);
        text_temp.setText("- °C");
        text_bpm = (TextView) view.findViewById(R.id.text_bpm);
        text_bpm.setText("- BPM");
    }

    private void onCreateViewGraphs(View view, Graph_t type){
        if (type == Graph_t.ECG) {
            ECGgraph = (GraphView) view.findViewById(R.id.ECGgraph);
            GridLabelRenderer ECGgridLabel = ECGgraph.getGridLabelRenderer();
            ECGgridLabel.setHorizontalAxisTitle("Time [s]");
            ECGgridLabel.setVerticalAxisTitle("ECG [V]");

        }
        else if(type == Graph_t.PPG){
            PPGgraph = (GraphView) view.findViewById(R.id.PPGgraph);
            GridLabelRenderer PPGgridLabel = PPGgraph.getGridLabelRenderer();
            PPGgridLabel.setHorizontalAxisTitle("Time [s]");
            PPGgridLabel.setVerticalAxisTitle("SpO2 [mA]");
        }

    }
}




