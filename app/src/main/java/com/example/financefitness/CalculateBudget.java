package com.example.financefitness;

import android.widget.TextView;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;

public class CalculateBudget {
    private double fundsPerMinute;//Used to calculate money earned working out
    private double fundsPerMile;//Used to calculate money earned running
    private double distanceTraveled;//Used to hold distance travelled
    private double timeWorkedOut;//Used to hold workout time duration
    private double fundsGeneratedWorkingOut;//Used to hold total funds generated working out
    private double fundsGeneratedRunning;//Used to hold total funds generated running
    private double totalFundsGenerated;//Used to hold total funds generated working out and running

    public CalculateBudget(){
        fundsPerMinute = 0.84;
        fundsPerMile = 10.0;
        fundsGeneratedWorkingOut = 0;
        fundsGeneratedRunning = 0;
        totalFundsGenerated = 0;
    }

    public void calculateTimeWorkedOut(WorkOutTimer workOutTimer){//Used to display additional money earned working out
        timeWorkedOut = workOutTimer.getTotalTimeWorkedOut();
        fundsGeneratedWorkingOut = (timeWorkedOut*fundsPerMinute);
        totalFundsGenerated += fundsGeneratedWorkingOut;
    }

    public void calculateDistanceTraveled(LatLng start, LatLng stop){//Used to display additional money earned running
        distanceTraveled = (SphericalUtil.computeDistanceBetween(start,stop)) * (0.000621371);//gets distance traveled walking in miles
        fundsGeneratedRunning = (distanceTraveled*fundsPerMile);
        totalFundsGenerated += fundsGeneratedRunning;
    }

    public void displayFundsGenerated(TextView textView){
        textView.setText("Total funds generated: "+"$"+String.format("%.2f",totalFundsGenerated));//sets the text view to the total funds generated
    }

    public double getDistanceTraveled(){
        return distanceTraveled;
    }

    public double getFundsGeneratedWorkingOut(){
        return fundsGeneratedWorkingOut;
    }

    public double getFundsGeneratedRunning(){
        return fundsGeneratedRunning;
    }
}
