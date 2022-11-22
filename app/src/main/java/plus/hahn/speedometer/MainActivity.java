package plus.hahn.speedometer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private BluetoothAdapter bluetoothAdapter;
    private final ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private boolean failed;
    private String myDeviceAddress;
    private Bundle args = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        failed = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        // check if bluetooth is available and working
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            args.putString("error", "<bluetooth not supported>");
            failed = true;
        } else if(!bluetoothAdapter.isEnabled()) {
            args.putString("error", "<bluetooth is disabled>");
            failed = true;
        }

        // bluetooth can be used
        if (failed == false) {
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    if (device.getName().compareTo("ESP32speedometer") == 0) {
                        if (myDeviceAddress != null) {
                            args.putString("error", "<multiple ESP32speedometers found>");
                            failed = true;
                        }
                        myDeviceAddress = device.getAddress();
                    }
                }
            }

            if (savedInstanceState == null) {
                args.putString("device", myDeviceAddress);
                Fragment fragment = new TerminalFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment, "terminal").commit();
            } else
                onBackStackChanged();
        }
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
