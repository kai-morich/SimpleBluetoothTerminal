package de.kai_morich.simple_bluetooth_terminal;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;

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

    public static TextView text_spo2;
    public static TextView text_temp;
    public static TextView text_bpm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.vital_signs_monitor_layout, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Bundle args = getArguments();
        GraphView ECGgraph = (GraphView) view.findViewById(R.id.ECGgraph);
        GridLabelRenderer ECGgridLabel = ECGgraph.getGridLabelRenderer();
        ECGgridLabel.setHorizontalAxisTitle("Time [s]");
        ECGgridLabel.setVerticalAxisTitle("ECG [mV]");


        GraphView PPGgraph = (GraphView) view.findViewById(R.id.PPGgraph);
        GridLabelRenderer PPGgridLabel = ECGgraph.getGridLabelRenderer();
        PPGgridLabel.setHorizontalAxisTitle("Time [s]");
        PPGgridLabel.setVerticalAxisTitle("SpO2 [V]");

        ECGgraph.addSeries(VitalSignsMonitor.GetECGPlotData());
        PPGgraph.addSeries(VitalSignsMonitor.GetPPGPlotData());

        text_spo2 = (TextView) view.findViewById(R.id.text_SpO2);
        text_spo2.setText("? %");
        text_temp = (TextView) view.findViewById(R.id.text_temp);
        text_temp.setText("? °C");
        text_bpm = (TextView) view.findViewById(R.id.text_bpm);
        text_bpm.setText("? BPM");
    }
}




