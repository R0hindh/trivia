package com.rohindh.trivia.data;

import com.rohindh.trivia.module.Question;

import java.util.ArrayList;

public interface answerListAsynResponse {
    void ProcessFinished(ArrayList<Question> questionArrayList);
}
