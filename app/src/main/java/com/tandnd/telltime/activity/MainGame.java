package com.tandnd.telltime.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import static com.android.volley.VolleyLog.TAG;
import static com.tandnd.telltime.util.Utilities.URL_REQUEST;
import static com.tandnd.telltime.util.Utilities.isPM;

import com.android.volley.toolbox.JsonObjectRequest;
import com.tandnd.telltime.R;
import com.tandnd.telltime.controller.AppController;
import com.tandnd.telltime.model.Question;
import com.tandnd.telltime.util.Utilities;
import com.tandnd.telltime.view.AnalogClockView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainGame extends AppCompatActivity implements Button.OnClickListener{
    private int score = 0;
    private int currentQuestion = 0;
    public static List<Question> listQuestion = new ArrayList<>();
    private AnalogClockView analogClock;
    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.answer_1,
            R.id.answer_2,
            R.id.answer_3
    };
    private TextView tvMiddleTime;
    private TextView tvScore;
    private TextView tvNumberQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        init();
    }

    private void init(){
        tvScore = (TextView) findViewById(R.id.scoreTxt);
        tvMiddleTime = (TextView) findViewById(R.id.tvMiddleTime);
        tvNumberQuestion = (TextView) findViewById(R.id.numberQuestionTxt);
        analogClock = (AnalogClockView) findViewById(R.id.analog_clock);
        buttons = new ArrayList<>();
        for(int idButton : BUTTON_IDS) {
            Button button = (Button) findViewById(idButton);
            button.setOnClickListener(this);
            buttons.add(button);
        }
        makeJsonObjectRequest(URL_REQUEST);
        //prepareGamePlay();
    }

    private void prepareGamePlay(){
        if (currentQuestion >= 10)
        {
            listQuestion.clear();
            Intent intent = new Intent(this, ReviewScreen.class);
            intent.putExtra("score", score);
            startActivity(intent);
            //Toast.makeText(this, "WANT TO TRY AGAIN", Toast.LENGTH_LONG).show();
            //makeJsonObjectRequest(URL_REQUEST);
            //currentQuestion = 0;
            //score = 0;
        }
        if (listQuestion.size() == 0){
            return;
        }
        Question question = listQuestion.get(currentQuestion);
        tvNumberQuestion.setText(String.format(getResources().getString(R.string.string_format_question), currentQuestion+1));
        tvScore.setText(String.format(getResources().getString(R.string.string_format_score), score));
        if (isPM(question.getTime_to_display())){
            tvMiddleTime.setText("PM");
        } else {
            tvMiddleTime.setText("AM");
        }
        //Setting analog clock
        analogClock.setHands(question);
        //Setting answer text
        buttons.get(0).setText(question.getOption1());
        buttons.get(1).setText(question.getOption2());
        buttons.get(2).setText(question.getOption3());
        buttons.get(0).setEnabled(true);
        buttons.get(1).setEnabled(true);
        buttons.get(2).setEnabled(true);
    }

    public  void  makeJsonObjectRequest(String urlReq) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlReq,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //listener.onRequestCompleted(response);
                Utilities.questionParserData(response, listQuestion);
                prepareGamePlay();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onClick(View v) {
        String answer = "";
        Question question = listQuestion.get(currentQuestion);
        switch (v.getId()){
            case R.id.answer_1:
                answer = question.getOption1();
                break;
            case R.id.answer_2:
                answer = question.getOption2();
                break;
            case R.id.answer_3:
                answer = question.getOption3();
                break;
        }
        if (answer.equals(question.getTime_to_display())){
            //Show Popup
            Utilities.truePopup(this);
            score += 1;
        } else {
            //Show Popup
            Utilities.falsePopup(this);
        }

        currentQuestion++;
        prepareGamePlay();
    }
}
