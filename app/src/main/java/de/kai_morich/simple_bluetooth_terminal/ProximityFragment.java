package de.kai_morich.simple_bluetooth_terminal;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProximityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProximityFragment extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SensorManager sensorManager;
    RadioButton[] buttons = new RadioButton[7];
    BluetoothAdapter bluetoothAdapter;

    public ProximityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProximityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProximityFragment newInstance(String param1, String param2) {
        ProximityFragment fragment = new ProximityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proximity, container, false);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        onResume();

        buttons[0] = (RadioButton) getView().findViewById(R.id.radioButton1);
        buttons[1] = (RadioButton) getView().findViewById(R.id.radioButton2);
        buttons[2] = (RadioButton) getView().findViewById(R.id.radioButton3);
        buttons[3] = (RadioButton) getView().findViewById(R.id.radioButton4);
        buttons[4] = (RadioButton) getView().findViewById(R.id.radioButton5);
        buttons[5] = (RadioButton) getView().findViewById(R.id.radioButton6);
        buttons[6] = (RadioButton) getView().findViewById(R.id.radioButton7);
    }

    public void refresh() {
        if(bluetoothAdapter == null) {
            setEmptyText("<bluetooth not supported>");
        }
        else if(!bluetoothAdapter.isEnabled()) {
            setEmptyText("<bluetooth disabled>");
        }
//        else if(permissions.getOrDefault(Manifest.permission.BLUETOOTH, false) {
        else if(permissionMissing) {
            setEmptyText("<permission missing, use REFRESH>");
        }

        if (permissionMissing) {
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices) {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    listItems.add(device);
                }
                Collections.sort(listItems, BluetoothUtil::compareTo);
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < 5.0f) {
                buttons[0].setChecked(true);
                buttons[1].setChecked(true);
                buttons[2].setChecked(true);
                buttons[3].setChecked(true);
                buttons[4].setChecked(true);
                buttons[5].setChecked(true);
                buttons[6].setChecked(true);
            } else {
                buttons[0].setChecked(false);
                buttons[1].setChecked(false);
                buttons[2].setChecked(false);
                buttons[3].setChecked(false);
                buttons[4].setChecked(false);
                buttons[5].setChecked(false);
                buttons[6].setChecked(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}