package com.example.financefitness;

import android.os.CountDownTimer;
import android.widget.TextView;

public class WorkOutTimer {
    private CountDownTimer countdown;//incrementing timer
    private int secondsPassed;//int variable to keep track of the seconds in a minute
    private long totalTimeWorkedOut;//converts milliseconds to minutes

    public void startWorkoutTimer(final TextView textView){//method to start the workout timer
        secondsPassed = 0;
        countdown = new CountDownTimer(3_600_000,1000){//displays time spent working out
            @Override
            public void onTick(long millisUntilFinished) {
                totalTimeWorkedOut = (59-((millisUntilFinished/60000)));
                secondsPassed +=1;
                if(secondsPassed>59){//when seconds left equals 0 reset to 59
                    secondsPassed=0;
                }
                textView.setText(totalTimeWorkedOut+" : "+secondsPassed);//sets the formatted time to text view
            }

            @Override
            public void onFinish() {
                textView.setText("You have worked out for an hour");//sets pre defined text when workout has reached an hour
            }
        }.start();
    }

    public void resetWorkoutTimer(){//Stops the timer and resets the timer
        countdown.cancel();//stops the timer
        totalTimeWorkedOut = 0;//resets the total time worked out variable
    }

    public Long getTotalTimeWorkedOut(){//returns the work out time
        return totalTimeWorkedOut;
    }
}
