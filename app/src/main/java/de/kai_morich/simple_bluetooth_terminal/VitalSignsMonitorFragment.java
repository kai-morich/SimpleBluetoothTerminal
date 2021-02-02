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

import de.kai_morich.simple_bluetooth_terminal.ui.main.SectionsPagerAdapter;

public class VitalSignsMonitorFragment extends Fragment {
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
    }
}




