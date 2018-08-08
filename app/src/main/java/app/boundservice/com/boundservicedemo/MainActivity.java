package app.boundservice.com.boundservicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button startBoundServiceButton, stopBoundServiceButton, bindServiceButton, getRandomNumberButton, unbindServiceButton;
    private TextView randomNumberTextView;
    private Intent serviceIntent;
    
    private BoundService boundService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startBoundServiceButton = findViewById(R.id.start_service_button_id);
        stopBoundServiceButton = findViewById(R.id.stop_service_button_id);
        randomNumberTextView = findViewById(R.id.random_number_text_view_id);
        bindServiceButton = findViewById(R.id.bind_service_button_id);
        getRandomNumberButton = findViewById(R.id.get_random_number_button_id);
        unbindServiceButton = findViewById(R.id.unbind_service_button_id);
        serviceIntent = new Intent(this, BoundService.class);
        
        //bound service will be started on click of this button
        startBoundServiceButton.setOnClickListener(this);
        //bound service will be stopped/destroed on click of this button. However, if it is bound to activity, it will not be destroyed.
        //it will be destroyed the moment it is unbound
        stopBoundServiceButton.setOnClickListener(this);
        //click of this button will bind service to activity
        bindServiceButton.setOnClickListener(this);
        //click of this button will get random number generated from the service using serviceConnection api
        getRandomNumberButton.setOnClickListener(this);
        //click of this button will unbind the service
        unbindServiceButton.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();
        
        switch (id) {
            case R.id.start_service_button_id:
                Toast("service started");
                startService(serviceIntent);
                break;
            case R.id.stop_service_button_id:
                Toast("service stopped");
                stopService(serviceIntent);
                break;
            case R.id.bind_service_button_id:
                bindService();
                break;
            case R.id.get_random_number_button_id:
                getAndSetRandomNumber();
                break;
            case R.id.unbind_service_button_id:
                unbindService();
                break;
        }
    }
    
    private void unbindService() {
        unbindService(serviceConnection);
    }
    
    private void getAndSetRandomNumber() {
        int randomNumber = boundService.getRandomNumber();
        randomNumberTextView.setText(String.valueOf(randomNumber));
    }
    
    private void bindService() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    isServiceBound = true;
                    Log.e(TAG, "serviceConnected");
                    BoundService.BoundServiceBinder boundServiceBinder = (BoundService.BoundServiceBinder) iBinder;
                    boundService = boundServiceBinder.getService();
                }
                
                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.e(TAG, "ServiceDisconnected");
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        Log.e(TAG, "Service bound");
    }
    
    public void Toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
