package de.kai_morich.simple_bluetooth_terminal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.kai_morich.simple_bluetooth_terminal.ui.main.SectionsPagerAdapter;

public class VitalSignsMonitorFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs_monitor);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        GraphView ECGgraph = (GraphView) findViewById(R.id.ECGgraph);
        GraphView PPGgraph = (GraphView) findViewById(R.id.PPGgraph);
        ECGgraph.addSeries(VitalSignsMonitor.GetECGPlotData());
        PPGgraph.addSeries(VitalSignsMonitor.GetPPGPlotData());



    }
}