package com.example.faceit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.transform.Result;
import android.*;

public class QuestionActivity extends AppCompatActivity {
    private TextView countLabel;
//    private TextView questionLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;

    static final int ANSWERS = 4;

    static final private int QUIZ_COUNT = 3; //number of times the quiz is taken before showing the results

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData[][] = {
            // {"Question","Right Answer", "Choice 1", "Choice 2", "Choice 3"}
            {"Who is this in the photo?","Ten","Eight","Nine","Eleven"},
            {"Who is this in the photo?","Eight","Seven","Ten","Twelve"},
            {"Who is this in the photo?","Ten","Six","Five","Eleven"},
            {"Who is this in the photo?","Ten","Six","Five","Eleven"},
            {"Who is this in the photo?","Ten","Six","Five","Eleven"},
            {"Who is this in the photo?","Ten","Six","Five","Eleven"},
            {"Who is this in the photo?","Eleven","Thirteen","Two","Ten"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        countLabel = (TextView)findViewById(R.id.countLabel);
//        questionLabel = (TextView)findViewById(R.id.questionLabel);
        answerBtn1 = (Button)findViewById(R.id.answerBtn1);
        answerBtn2 = (Button)findViewById(R.id.answerBtn2);
        answerBtn3 = (Button)findViewById(R.id.answerBtn3);
        answerBtn4 = (Button)findViewById(R.id.answerBtn4);

        //create quizArray from quizData
        for (int i=0; i<quizData.length; i++){
            //Prepare Array
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); //Question
            tmpArray.add(quizData[i][1]); //Right Answer
            tmpArray.add(quizData[i][2]); //Choice 1
            tmpArray.add(quizData[i][3]); //choice 2
            tmpArray.add(quizData[i][4]); //Choice 3
            quizArray.add(tmpArray);
        }
        showNextQuiz();
    }

    public void showNextQuiz(){
        //update quizCountLabel
        String quizCountLabel = "Q" + quizCount;
        countLabel.setText(quizCountLabel);

        //Generate random number between 0 and 3
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        //Pick one quiz set
        ArrayList<String> quiz = quizArray.get(randomNum);

        //Set question and right answer
        rightAnswer = quiz.get(1);

        Log.i("quiz length:", Integer.toString( quiz.size()));
        Log.i("quiz content:", quiz.toString());
        //Set choices
        ArrayList<Integer> shuffledIndex = new ArrayList<>(3);
        for(int i=0; i<ANSWERS; i++)
        {
            shuffledIndex.add(i+1);
        }
        Collections.shuffle(shuffledIndex);
        answerBtn1.setText(quiz.get(shuffledIndex.get(0)));
        answerBtn2.setText(quiz.get(shuffledIndex.get(1)));
        answerBtn3.setText(quiz.get(shuffledIndex.get(2)));
        answerBtn4.setText(quiz.get(shuffledIndex.get(3)));
    }

    public void checkAnswer(View view){
        //get pushed button
        Button answerBtn = (Button)findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;

        if(btnText.equals(rightAnswer)){
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
        builder.setMessage("Answer: " + rightAnswer);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT){
                    //show result
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    startActivity(intent);
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
