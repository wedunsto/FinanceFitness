package com.example.financefitness;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Context mContext;
    private static final int permissionRequest = 99;//requests permission to users location

    private TextView totalDistanceTraveled;//Used to display total distance traveled
    private TextView currentDistanceTraveled;//Used to display immediate distance traveled
    private TextView workOutTime;//Used to display total time working out
    private TextView fundsGenerated;//Used to display total funds generated working out and running

    private Button startRunningButton;//Used to augment start running button
    private Button stopRunningButton;//Used to augment stop running button
    private Button startWorkout;//Used to augment start workout button
    private Button stopWorkout;//Used to augment stop workout button

    private WorkOutTimer workOutTimer;//Used to gain access to class variables and methods
    private CalculateBudget calculateBudget;//Used to gain access to class variables and methods

    private LatLng startLocation;//save the coordinates of starting location
    private LatLng stopLocation;//save the coordinates of the stopping location
    private Location lastLocation;//get last location requested
    private FusedLocationProviderClient getStartLocation;//get starting location
    private FusedLocationProviderClient getStopLocation;//get stopping location
    private GoogleMap mMap;//Google maps object to place on map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionRequest);//request permission for location
        mapFragment.getMapAsync(this);//synchronize the map with this activity

        totalDistanceTraveled = findViewById(R.id.distanceTraveled);
        workOutTime = findViewById(R.id.workOutTime);
        fundsGenerated = findViewById(R.id.fundsGenerated);
        startRunningButton = findViewById(R.id.startButton);
        stopRunningButton = findViewById(R.id.stopButton);
        startWorkout = findViewById(R.id.startWorkOut);
        stopWorkout = findViewById(R.id.stopWorkOut);

        workOutTimer = new  WorkOutTimer();
        calculateBudget = new CalculateBudget();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {//sets to google map to the desired map
        mMap = googleMap;
    }

    public void setStartLocation(View view){//Used to set the start location on the google map
        startRunningButton.setText("Lets start running!");
        getStartLocation = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task<Location> task = getStartLocation.getLastLocation();
            task.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                   lastLocation = task.getResult();//gets location
                   startLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());//gets lat and lng from location variable
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,15));
                   mMap.addMarker(new MarkerOptions().position(startLocation));//adds a marker at start location
                }
            });
        }
        catch (SecurityException ex){
            totalDistanceTraveled.setText("Unable to find start location");
        }
    }

    public void setStopLocation(View view){//Used to set the stop location  on the google map
        startRunningButton.setText("Start Traveling");
        getStopLocation = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task<Location> task = getStopLocation.getLastLocation();
            task.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    lastLocation = task.getResult();//gets location
                    stopLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());//gets lat and lng from location variable
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stopLocation,15));
                    mMap.addMarker(new MarkerOptions().position(stopLocation));//adds a marker at stop location
                    drawTravel();
                    calculateBudget.calculateDistanceTraveled(startLocation,stopLocation);//calculates funds generated running
                    notifyMe("Running Update"
                            ,"Ran: ~"+String.format("%.0f",calculateBudget.getDistanceTraveled())+" miles"
                            ,"Displays how far you ran"
                            ,"2"
                            ,R.drawable.run);
                    totalDistanceTraveled.setText("Ran: ~"+String.format("%.0f",calculateBudget.getDistanceTraveled())+" miles"+"\nMoney earned: $"+String.format("%.2f",calculateBudget.getFundsGeneratedRunning()));//sets the text to the distance traveled
                    calculateBudget.displayFundsGenerated(fundsGenerated);//updates the text to the total funds generated
                    startLocation = stopLocation;//updates the start location to the stop location
                }
            });
        }
        catch (SecurityException ex){
            totalDistanceTraveled.setText("Unable to find stop location");
        }
    }

    public void drawTravel(){//draws a line between start and stop locations
        PolylineOptions distance = new PolylineOptions();
        distance.add(startLocation);
        distance.add(stopLocation);
        distance.color(Color.RED);
        distance.width(5);
        mMap.addPolyline(distance);//adds drawn line to map
    }

    public void startTimer(View view){//Used to start the workout time
        startWorkout.setText("Lets start working out!");
        workOutTimer.startWorkoutTimer(workOutTime);
    }

    public void stopTimer(View view){//Used to stop the workout timer
        startWorkout.setText("Start Workout");
        notifyMe("Workout Update"
                ,"Workout Duration: "+workOutTimer.getTotalTimeWorkedOut()+" Minutes"
                ,"Displays how long you worked out"
                ,"1"
                ,R.drawable.lift);
        calculateBudget.calculateTimeWorkedOut(workOutTimer);
        calculateBudget.displayFundsGenerated(fundsGenerated);//displays total funds earned
        workOutTime.setText("Worked out for: "+workOutTimer.getTotalTimeWorkedOut()+"\nMoney earned: $"+String.format("%.2f",calculateBudget.getFundsGeneratedWorkingOut()));
        workOutTimer.resetWorkoutTimer();//resets the workout timer
    }

    public void notifyMe(String title, String text,String description,String id, int icon){//generate notification for time worked out or running distance
        NotificationCompat.Builder notify = new NotificationCompat.Builder(mContext,id);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        NotificationChannel channel = new NotificationChannel(id,title, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notify.setSmallIcon(icon);
        notify.setContentTitle(title);
        notify.setContentText(text);
        notify.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
        notificationManagerCompat.notify(Integer.parseInt(id),notify.build());
    }

}
