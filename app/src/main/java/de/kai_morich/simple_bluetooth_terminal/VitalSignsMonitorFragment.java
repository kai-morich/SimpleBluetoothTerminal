package de.kai_morich.simple_bluetooth_terminal;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;

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
        GraphView PPGgraph = (GraphView) view.findViewById(R.id.PPGgraph);

        ECGgraph.addSeries(VitalSignsMonitor.GetECGPlotData());
        PPGgraph.addSeries(VitalSignsMonitor.GetPPGPlotData());

        text_spo2 = (TextView) view.findViewById(R.id.text_SpO2);
        text_spo2.setText("220");
        text_temp = (TextView) view.findViewById(R.id.text_temp);
        text_temp.setText("36.7");
        text_bpm = (TextView) view.findViewById(R.id.text_bpm);
        //text_bpm.setText("180");
    }
}




