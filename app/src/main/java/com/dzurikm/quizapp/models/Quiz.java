package com.dzurikm.quizapp.models;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Locale;

public class Quiz implements Serializable {

    private LinkedList<Question> questions;
    private int questionIndex = 0;
    private boolean gameOver = false;

    public void resetGame(LinkedList<Question> questions){
        questionIndex = 0;
        gameOver = false;
        this.questions = questions;

    }

    public Quiz(LinkedList<Question> questions) {
        this.questions = questions;
    }

    public Quiz() {}

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = questions;
    }

    public int getProgress() {
        return (questionIndex + 1);
    }

    public void nextQuestion(){
        if (questions.size() > questionIndex + 1){
            this.questionIndex++;
        }

    }

    public int getFinalScore(){
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            Answer answer = questions.get(i).getAnswer();
            if (answer.getRightAnswer().trim().toLowerCase().equals(
                    answer.getUserAnswer().trim().toLowerCase()
            )){
                score++;
            }
            Log.d("debug as","Question - " + questions.get(i).getQuestion() + " ... user - " + answer.getUserAnswer() + " ... right - " + answer.getRightAnswer());
        }

        return score;
    }

    public void previousQuestion(){
        if (0 <= questionIndex - 1){
            this.questionIndex--;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Question getCurrentQuestion(){
        return questions.get(questionIndex);
    }

    public boolean answer(String answer){
        if (!gameOver){

            Answer answersData = getCurrentQuestion().getAnswer();
            // set selceted

            for (int i = 0; i < answersData.answers.size(); i++) {
                if (answersData.answers.get(i).trim().toLowerCase().equals(answer.trim().toLowerCase())) answersData.setSelectedAnswer(i);
            }
            answersData.setUserAnswer(answer.trim().toLowerCase());



        }

        return false;
    }


    public static class Answer{
        private String rightAnswer,userAnswer = "";
        private LinkedList<String> answers;
        private int selectedAnswer = -1;

        public Answer(String rightAnswer, LinkedList<String> answers) {
            this.rightAnswer = rightAnswer;
            this.answers = answers;
        }

        public int getSelectedAnswer() {
            return selectedAnswer;
        }

        public void setSelectedAnswer(int selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public LinkedList<String> getAnswers() {
            return answers;
        }

        public void setAnswers(LinkedList<String> answers) {
            this.answers = answers;
        }
    }

    public static class Question{
        Answer answer;
        String question;

        public Question(Answer answer, String question) {
            this.answer = answer;
            this.question = question;
        }

        public Answer getAnswer() {
            return answer;
        }

        public void setAnswer(Answer answer) {
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }

}
