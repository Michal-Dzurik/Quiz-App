package com.dzurikm.quizapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dzurikm.quizapp.R;
import com.dzurikm.quizapp.activities.QuizActivity;
import com.dzurikm.quizapp.models.Quiz;

import java.util.LinkedList;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.ViewHolder> {

    private Quiz.Answer answer;
    private char[] letters = new char[]{'A','B','C','D','E','F','G'};
    Context context;
    int selected = -1;
    ViewHolder lastSelected;
    LinkedList<String> data;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param answer LinkedList<Quiz.Answer>.String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public AnswerListAdapter(Context context, Quiz.Answer answer) {
        this.answer = answer;
        this.context = context;

        data = answer.getAnswers();
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView answerText,answerLetter;
        View rootView;

        public ViewHolder(View view) {
            super(view);
            rootView = view;

            // Define click listener for the ViewHolder's View

            answerText = (TextView) view.findViewById(R.id.answerText);
            answerLetter = (TextView) view.findViewById(R.id.answerLetter);

        }

        public TextView getAnswerText() {
            return answerText;
        }

        public TextView getAnswerLetter() {
            return answerLetter;
        }

        public View getRootView() {
            return rootView;
        }

        public void setRootView(View rootView) {
            this.rootView = rootView;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.answer_list_item, viewGroup, false);


        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TextView t = (TextView) view.findViewById(R.id.answerText);

                QuizActivity.answer(t.getText().toString());
                animate(viewHolder,viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        TextView answerText = viewHolder.getAnswerText();
        TextView answerLetter = viewHolder.getAnswerLetter();

        selected = position;

        if (answer.getSelectedAnswer() == position){
            animate(viewHolder,position);
        }

        answerText.setText(answer.getAnswers().get(position));
        answerLetter.setText(String.valueOf(letters[position]));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void animate(ViewHolder viewHolder,int position){
        TransitionDrawable transition = (TransitionDrawable) viewHolder.itemView.getBackground();

        if (position == answer.getSelectedAnswer()){
            transition.startTransition(500);
            Log.i("se","kosk");
        }

        if (lastSelected != null){
            TransitionDrawable transitionBack = (TransitionDrawable) lastSelected.itemView.getBackground();
            transitionBack.reverseTransition(500);
        }

        lastSelected = viewHolder;


    }
}

