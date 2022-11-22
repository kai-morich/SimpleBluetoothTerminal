package plus.hahn.speedometer;

import java.time.Instant;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class parsedDouble {

    public boolean valid;   // holds true, if value can be used
    public double value;    // double representation of input string
    public String raw;      // copy of input string

    parsedDouble() {
        valid = false;
        value = 0.0;
        raw = "";
    }
}

public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {

    private enum Connected { False, Pending, True }

    private String deviceAddress;
    private SerialService service;

    private TextView receiveText;
    private TextView speedometerText;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private String speedometerLine = "";
    private double currentSpeed = 0.0;    // m/s
    private double accumulatedDistance = 0.0;   // m
    private double timeWithMovement = 0.0;   // s
    private java.time.Instant lastTimestamp = java.time.Instant.MIN;
    private double timeElapsed;
    private Pattern sentencePattern = Pattern.compile (">[0-9a-fA-F]{6}:[VES][A-Z]#[0-9]{3}=.*<");

    // === Lifecycle ==========================================================
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        deviceAddress = getArguments().getString("device");
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(service != null)
            service.attach(this);
        else
            getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @SuppressWarnings("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try { getActivity().unbindService(this); } catch(Exception ignored) {}
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && service != null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    // === UI =================================================================
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        receiveText = view.findViewById(R.id.receive_text);                          // TextView performance decreases with number of spans
        receiveText.setTextColor(getResources().getColor(R.color.colorRecieveText)); // set as default color to reduce number of spans
        receiveText.setMovementMethod(ScrollingMovementMethod.getInstance());

        speedometerText = view.findViewById(R.id.speedometer_text);                          // TextView performance decreases with number of spans
        speedometerText.setTextColor(getResources().getColor(R.color.colorRecieveText)); // set as default color to reduce number of spans
        speedometerText.setMovementMethod(ScrollingMovementMethod.getInstance());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terminal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear) {
            receiveText.setText("");
            speedometerText.setText("");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // === Serial + UI ========================================================
    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
    }

    private void receive(byte[] data) {
        String msg = new String(data);
        receiveText.append(msg);
        speedometerLine += new String(data);
        Matcher sentenceMatcher = sentencePattern.matcher (speedometerLine);

        if (sentenceMatcher.find()) {
            String macAddress = speedometerLine.substring(1,7);
            char sensor = speedometerLine.charAt(8);
            char dataset = speedometerLine.charAt(9);
            int counter = Integer.parseInt(speedometerLine.substring(11,14));
            parsedDouble parsed = getDouble(speedometerLine.substring(15, speedometerLine.indexOf("<")));
            speedometerLine = speedometerLine.substring(speedometerLine.indexOf("<") + 2);

            switch (sensor) {
                case 'V':
                    switch (dataset) {
                        case 'F':   // frequency
                            if (parsed.valid) {
                                handleFrequency (parsed.value);
                            }
                            break;
                        case 'A':   // amplitude
                            if (parsed.valid) {
                                handleAmplitude (parsed.value);
                            }
                            break;
                        default:
                            // some kind of error handling?!
                            break;
                    }
                    break;
                case 'E':
                    switch (dataset) {
                        case 'P':   // pressure
                        case 'T':   // temperature
                        case 'H':   // humidity
                        default:
                            // some kind of error handling?!
                            break;
                    }
                    break;
                case 'S':
                    switch (dataset) {
                        case 'V':   // supply voltage
                        case 'U':   // uptime
                            if (parsed.valid) {
                                handleUptime (parsed.value);
                            }
                            break;
                        case 'B':   // build version
                            if (!parsed.valid) {
                                handleBuildVersion (parsed.raw);
                            }
                            break;
                        default:
                            // some kind of error handling?!
                            break;
                    }
                    break;
                default:
                    // some kind of error handling?!
                    break;
            }
        }
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);
    }

    // === SerialListener =====================================================
    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();
        onStop();
        onDetach();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }

    private parsedDouble getDouble (String value)
    {
        parsedDouble result = new parsedDouble();
        result.raw = value;
        try {
            result.value = Double.parseDouble(value);
            result.valid = true;
        } catch(Exception e) {
            result.valid = false;
        }
        return result;
    }


    private void handleFrequency(double frequency) {
        Instant now = java.time.Instant.now();
        if (lastTimestamp != java.time.Instant.MIN) {
            Duration te = Duration.between(lastTimestamp, now);
            timeElapsed = 0.001 * te.toMillis();
            currentSpeed = frequency * 0.16; // 1Hz = 16cm/s
            accumulatedDistance += currentSpeed * timeElapsed;
            timeWithMovement += timeElapsed;
        }
        lastTimestamp = now;
        speedometerText.append("v=" + String.format("%05.2f", currentSpeed) + " d=" + String.format("%07.2f", accumulatedDistance) + " t=" + String.format("%07.2f", timeWithMovement) + "\n");
    }


    private void handleAmplitude(double amplitude) {
        speedometerText.append("a=" + String.format("%05.2f", amplitude) + "\n");
    }


    private void handleUptime(double uptime) {
        speedometerText.append("t=" + String.format("%05.2f", uptime) + "\n");
    }


    private void handleBuildVersion(String buildVersion) {
        speedometerText.append("build: " + buildVersion + "\n");
    }
}
