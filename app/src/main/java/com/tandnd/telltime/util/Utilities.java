package com.tandnd.telltime.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.tandnd.telltime.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ModITer on 5/12/2017.
 */

public class Utilities {

    public static final String URL_REQUEST  = "https://time-learner.herokuapp.com/questions";

    public static void questionParserData(final JSONObject json, List<Question> list){
        try {
            JSONArray jArray = json.getJSONArray("questions");
            Question question;
            for(int i=0; i<jArray.length(); i++){
                JSONObject json_data = jArray.getJSONObject(i);
                question= new Question();
                question.setTime_to_display(json_data.getString("time_to_display"));
                question.setOption1((String)json_data.getJSONArray("options").get(0));
                question.setOption2((String)json_data.getJSONArray("options").get(1));
                question.setOption3((String)json_data.getJSONArray("options").get(2));
                list.add(question);
            }
        }
        catch(JSONException e){
            Log.e("TellTime", "One or more fields not found in the JSON data");
        }
    }

    public static boolean isPM(String time_showing){
        int hour = Integer.valueOf(time_showing.substring(0,2));
        return hour > 12 ? true : false;
    }

    public static boolean determineInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void internetConnectionFailedDialog(Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("No internet");
        alertDialogBuilder
                .setMessage("Try to reconnect the internet!")
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public static void truePopup(Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("");
        TextView myMsg = new TextView(context);
        myMsg.setText("Correct!");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(28);
        alertDialogBuilder.setView(myMsg);
        alertDialogBuilder
                //.setMessage("Correct!")
                .setCancelable(true);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        //TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //textView.setTextSize(30);
        alertDialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
    }

    public static void falsePopup(Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder
                //.setMessage("Incorrect!")
                .setCancelable(true);
        TextView myMsg = new TextView(context);
        myMsg.setText("Incorrect!");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(28);
        alertDialogBuilder.setView(myMsg);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        //TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //textView.setTextSize(30);
        alertDialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
    }

    public static String setFlag(int score){
        switch (score){
            case 0:
                case 1:
            case 2:
            case 3:
                return "Keep Trying!";
            case 4:
            case 5:
                return "Well Done!";
            case 6:
            case 7:
            case 8:
                return "Great Job!";
            case 9:
                return "Excellent!";
            case 10:
                return "Genius!";
            default:
                break;
        }
        return "No Title";
    }
}
