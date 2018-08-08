package app.boundservice.com.boundservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class BoundService extends Service {
    
    private static final String TAG = BoundService.class.getSimpleName();
    private IBinder binder = new BoundServiceBinder();
    private boolean isRandomNumberGeneratorOn = false;
    private int randomValue = 0;
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand thread id: " + Thread.currentThread().getId());
        isRandomNumberGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "OnUnBind service called");
        return super.onUnbind(intent);
    }
    
    @Override
    public void onDestroy() {
        stopRandomNumberGenerator();
        Log.e(TAG, "Service destroyed");
        super.onDestroy();
    }
    
    public void startRandomNumberGenerator() {
        while (isRandomNumberGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (isRandomNumberGeneratorOn) {
                    randomValue = new Random().nextInt(100);
                    Log.e(TAG, "Generated random number: " + randomValue + " on thread with ID: " + Thread.currentThread().getId());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false;
    }
    
    public int getRandomNumber() {
        return randomValue;
    }
    
    public class BoundServiceBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }
}
