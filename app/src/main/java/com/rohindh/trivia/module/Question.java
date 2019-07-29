package com.rohindh.trivia.module;

public class Question {
    private String question;
    private Boolean questionTrue;

    public Question() {
    }

    @Override
    public String toString() {
        return "Question"+
                "question =" + question +
                "\n questionTrue=" + questionTrue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getQuestionTrue() {
        return questionTrue;
    }

    public void setQuestionTrue(Boolean questionTrue) {
        this.questionTrue = questionTrue;
    }

    public Question(String question, Boolean questionTrue) {
        this.question = question;
        this.questionTrue = questionTrue;
    }
}
