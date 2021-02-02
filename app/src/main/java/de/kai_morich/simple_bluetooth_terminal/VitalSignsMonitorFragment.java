package de.kai_morich.simple_bluetooth_terminal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.kai_morich.simple_bluetooth_terminal.ui.main.SectionsPagerAdapter;

public class VitalSignsMonitorFragment extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs_monitor);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager monitorPager = findViewById(R.id.monitor_pager);
        monitorPager.setAdapter(sectionsPagerAdapter);
        TabLayout monitorTab = findViewById(R.id.monitor_tab);
        monitorTab.setupWithViewPager(monitorPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
        else
            onBackStackChanged();


        GraphView ECGgraph = (GraphView) findViewById(R.id.ECGgraph);
        GraphView PPGgraph = (GraphView) findViewById(R.id.PPGgraph);
        ECGgraph.addSeries(VitalSignsMonitor.GetECGPlotData());
        PPGgraph.addSeries(VitalSignsMonitor.GetPPGPlotData());



    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }
}