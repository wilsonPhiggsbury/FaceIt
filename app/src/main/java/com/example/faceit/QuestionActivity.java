package com.example.faceit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.transform.Result;
import android.*;

import org.json.JSONException;

public class QuestionActivity extends AppCompatActivity {
    private TextView countLabel;
//    private TextView questionLabel;
    private Button[] answerBtns = new Button[4];
    private ImageView faceImg;

    private int correctSlotNumber;
    private int questionNumber;

    private int rightAnswerCount = 0;
    private int quizCount = 1;

    static final int ANSWERS = 4;

    static final private int QUIZ_COUNT = 3; //number of times the quiz is taken before showing the results

    ArrayList<Question> quizArray;
    ArrayList<Integer> quizIndexOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        countLabel = (TextView)findViewById(R.id.countLabel);
        faceImg = (ImageView)findViewById(R.id.faceImg);
//        questionLabel = (TextView)findViewById(R.id.questionLabel);
        answerBtns[0] = (Button)findViewById(R.id.answerBtn1);
        answerBtns[1] = (Button)findViewById(R.id.answerBtn2);
        answerBtns[2] = (Button)findViewById(R.id.answerBtn3);
        answerBtns[3] = (Button)findViewById(R.id.answerBtn4);
        resetQuiz();
    }
    private  void resetQuiz()
    {
        try {
            quizArray = Persistent.loadEntriesFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(quizArray.size() < 4)
        {
            Log.e("FaceIt Game Mode","Too few questions!");
            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
            builder.setMessage(R.string.not_enough_questions);
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Log.d("FaceIt Game Mode","Number of questions is "+quizArray.size());
            showNextQuiz();
        }

    }
    public void showNextQuiz(){
        // populate quiz indexes
        quizIndexOrder = new ArrayList<>();
        for(int i=0; i<quizArray.size(); i++)
        {
            quizIndexOrder.add(i);
            Log.d("FaceIt Game Mode","Question "+i+" is "+quizArray.get(i).toString());
        }
        Collections.shuffle(quizIndexOrder);
        //update quizCountLabel
        String quizCountLabel = "Q" + quizCount;
        countLabel.setText(quizCountLabel);

        //Generate random number between 0 and 3
        Random random = new Random();
        int randomNum = random.nextInt(quizIndexOrder.size());
        //Pick one quiz question by index
        questionNumber = quizIndexOrder.remove(randomNum);
        //Paste answer into random slot
        correctSlotNumber = random.nextInt(4);
        Question currentQuestion = quizArray.get(questionNumber);
        answerBtns[correctSlotNumber].setText(currentQuestion.getName());
        currentQuestion.putImageTo(faceImg);
        // Pick other options from other questions & set choices *may RE-SELECT same other question*
        for(int i=0; i<4; i++)
        {
            if(i==correctSlotNumber)
                continue;
            do {
                randomNum = random.nextInt(quizArray.size());
            }while(randomNum == questionNumber);
            answerBtns[i].setText(quizArray.get(randomNum).getName());
        }
    }

    public void checkAnswer(View view){
        //get pushed button
        String alertTitle;
        Button answerBtn = (Button)findViewById(view.getId());
        if(answerBtn.equals(answerBtns[correctSlotNumber]))
        {
            //Correct!
            alertTitle = "Correct!";
            rightAnswerCount++;
        }
        else{
            //Wrong...
            alertTitle = "Wrong...";
        }

        //Create Dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("Answer: " + quizArray.get(questionNumber).getName());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT){
                    //show result
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    startActivity(intent);
                    resetQuiz();
                }else{
                    quizCount++;
                    showNextQuiz();
                }

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
