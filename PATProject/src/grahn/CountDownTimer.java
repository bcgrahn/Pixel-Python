/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grahn;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Bryce Tristan
 */
public class CountDownTimer {

    private int counter;

    public CountDownTimer() {
    }

    public void startTimer()                                                       // creates a 20 second count down timer for certain power ups
    {

        Timer timer = new Timer();
        counter = 20;
        TimerTask task;
        task = new TimerTask() {

            public void run() {
                System.out.println(counter);
                counter--;

                if (counter <= -1) {                                               // Cancels the timer after 20 seconds
                    timer.cancel();
                }
            }

        };
        timer.scheduleAtFixedRate(task, 1000, 1000);                                //1000ms = 1sec
    }

    public int getTime() {                                                      // returns time in seconds
        return counter;

    }

}
