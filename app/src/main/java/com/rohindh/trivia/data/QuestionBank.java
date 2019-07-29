package com.rohindh.trivia.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.rohindh.trivia.controller.Appcontroller;
import com.rohindh.trivia.module.Question;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class QuestionBank {
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    ArrayList<Question> questionArrayList = new ArrayList<>();

    public ArrayList<Question> getQuestions (final answerListAsynResponse callback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i =0;i<response.length();i++){
                    try {
                        Question question = new Question();
                        question.setQuestion(response.getJSONArray(i).get(0).toString());
                        question.setQuestionTrue(response.getJSONArray(i).getBoolean(1));
                        //add to list
                        questionArrayList.add(question);


//                        Log.d("JSON" , "response :"+response.getJSONArray(i).get(0));
//                        Log.d("JSON2" , "response2 :"+response.getJSONArray(i).getBoolean(1));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                Log.d("work's fine", ""+questionArrayList.size());

                if(callback != null) callback.ProcessFinished(questionArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Appcontroller.getInstance().addToRequestQueue(jsonArrayRequest);
        return null;
    }
}
