package com.kaya.bluetoothtransform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final int BLUETOOTH_MSG_STATE = 0;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private ImageButton btnBluetoothEnable;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket socket;

    private ConnectedThread connectedThread;

    private String rcStatus = "n";
    Handler bluetoothHandler;

    // SPP UUID service
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onResume() {
        super.onResume();

        if(bluetoothAdapter.isEnabled()){
            btnBluetoothEnable.setImageResource(R.mipmap.bluetooth_enable_true);
        }else{
            btnBluetoothEnable.setImageResource(R.mipmap.bluetooth_enable);
        }
    }
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main00);
        checkPermission(Manifest.permission.BLUETOOTH, 1001);
        ImageButton btnStop = (ImageButton) findViewById(R.id.btnStop);
        ImageButton btnUp = (ImageButton) findViewById(R.id.btnUp);
        ImageButton btnDown = (ImageButton) findViewById(R.id.btnDown);
        ImageButton btnLeft = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton btnRight = (ImageButton) findViewById(R.id.btnRight);
        btnBluetoothEnable = (ImageButton) findViewById(R.id.btnBluetoothEnable);
        ImageButton btnBluetoothConnect = (ImageButton) findViewById(R.id.btnBluetoothConnect);
        ImageButton btnBluetoothDisconnect = (ImageButton) findViewById(R.id.btnBluetoothDisconnect);

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rcStatus = "r";
                        return  true;
                    case MotionEvent.ACTION_UP:
                        rcStatus = "n";
                        return  true;
                }
                return false;
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rcStatus = "l";
                        return  true;
                    case MotionEvent.ACTION_UP:
                        rcStatus = "n";
                        return  true;
                }
                return false;
            }
        });

        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rcStatus = "u";
                        return  true;
                    case MotionEvent.ACTION_UP:
                        rcStatus = "n";
                        return  true;
                }
                return false;
            }
        });

        btnStop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rcStatus = "s";
                        return  true;
                    case MotionEvent.ACTION_UP:
                        rcStatus = "n";
                        return  true;
                }
                return false;
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rcStatus = "d";
                        return  true;
                    case MotionEvent.ACTION_UP:
                        rcStatus = "n";
                        return  true;
                }
                return false;
            }
        });

        bluetoothHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == BLUETOOTH_MSG_STATE){
                    String message = (String) msg.obj;

                    //Handle message
                }
            }
        };

        Thread sendData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (socket.isConnected()) {
                            connectedThread.write(rcStatus);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                Log.wtf("I E", "thread");
                            }
                        }
                    }catch(NullPointerException e){
                        //If socket is not created yet
                    }
                }
            }

        });

        sendData.start();
        bluetoothManager = (BluetoothManager) (this.getSystemService(Context.BLUETOOTH_SERVICE));
        bluetoothAdapter = bluetoothManager.getAdapter();

        btnBluetoothEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bluetoothAdapter.isEnabled()){
                    Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enable, 0);
                }else{
                    Toast.makeText(getApplicationContext(), "Already enabled.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBluetoothDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(socket.isConnected()) {
                        socket.close();
                        Toast.makeText(getApplicationContext(), "Disconnected.", Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    Log.wtf("IO E", "Disconnect");
                }catch (NullPointerException e){
                    Log.wtf("Null E", "Disconnect");
                }
            }
        });

        btnBluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(socket.isConnected()) {
                        Toast.makeText(getApplicationContext(), "Already Connected. Disconnect before reconnect.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (NullPointerException e){
                    Log.wtf("Null", "Connect");
                }

                pairedDevices = bluetoothAdapter.getBondedDevices();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setIcon(R.mipmap.bluetooth_connect);
                dialogBuilder.setTitle("选择设备");

                final ArrayList<String> deviceMACs = new ArrayList<>();

                ArrayAdapter<String> deviceDetails = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

                for(BluetoothDevice device : pairedDevices){
                    deviceDetails.add(device.getName() + " " + device.getAddress());
                    deviceMACs.add(device.getAddress());
                }

                dialogBuilder.setAdapter(deviceDetails, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceMACs.get(which));
                        bluetoothAdapter.cancelDiscovery();
                        try {
                            Log.wtf("ReadToConnectDevices:", device.toString());
                            socket = createBluetoothSocket(device);
                        }catch (IOException e){
                            Log.wtf("IO E", "create socket");
                        }

                        try {
                            Log.wtf("ReadToConnectDevices:", socket.toString());
                            socket.connect();
                            Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            Log.wtf("IO E", "Connect");
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            try {
                                socket.close();
                            } catch (IOException er) {
                                Log.wtf("IO E", "Close");
                            }
                        }

                        connectedThread = new ConnectedThread(socket);
                        connectedThread.start();
                    }
                });

                dialogBuilder.show();
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    private class ConnectedThread extends Thread{
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket){
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            }catch (IOException e){
                Log.wtf("IO E", "Construct");
            }

            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    bluetoothHandler.obtainMessage(BLUETOOTH_MSG_STATE, bytes, -1, message).sendToTarget();
                }catch (IOException e){
                    Log.wtf("IO E", "run");
                    break;
                }
            }
        }

        public void write(String input){
            byte[] buffer = input.getBytes();
            try{
                outputStream.write(buffer);
            }catch (IOException e){
                Log.wtf("IO E", "write");
            }
        }
    }
}
