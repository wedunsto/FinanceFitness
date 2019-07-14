package com.example.financefitness;

import android.widget.TextView;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;

public class CalculateBudget {
    double fundsPerMinute;//Used to calculate money earned working out
    double fundsPerMile;//Used to calculate money earned running
    double distanceTraveled;//Used to hold distance travelled
    double timeWorkedOut;//Used to hold workout time duration
    double fundsGeneratedWorkingOut;//Used to hold total funds generated working out
    double fundsGeneratedRunning;//Used to hold total funds generated running
    double totalFundsGenerated;//Used to hold total funds generated working out and running

    public CalculateBudget(){
        fundsPerMinute = 0.84;
        fundsPerMile = 10.0;
        fundsGeneratedWorkingOut = 0;
        fundsGeneratedRunning = 0;
        totalFundsGenerated = 0;
    }

    public void calculateTimeWorkedOut(WorkOutTimer workOutTimer){//Used to display additional money earned working out
        timeWorkedOut = workOutTimer.getTotalTimeWorkedOut();
        fundsGeneratedRunning += (timeWorkedOut*fundsGeneratedWorkingOut);
    }

    public void calculateDistanceTraveled(LatLng start, LatLng stop){//Used to display additional money earned running
        distanceTraveled = (SphericalUtil.computeDistanceBetween(start,stop)) * (0.000621371);//gets distance traveled walking in miles
        fundsGeneratedRunning = distanceTraveled*fundsPerMile;
    }

    public void displayFundsGenerated(TextView textView){
        totalFundsGenerated = fundsGeneratedWorkingOut + fundsGeneratedRunning;
        textView.setText("$"+String.format("%.2f",totalFundsGenerated));
    }
}
