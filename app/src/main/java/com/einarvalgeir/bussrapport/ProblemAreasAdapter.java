package com.einarvalgeir.bussrapport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

public class ProblemAreasAdapter extends RecyclerView.Adapter<ProblemAreasAdapter.ViewHolder> {

    private String[] problemAreas;
    private IProblemAreaSelector callback;
    private int selectedPos = 0;

    public ProblemAreasAdapter(String[] problemAreas, IProblemAreaSelector callback) {
        this.callback = callback;
        this.problemAreas = problemAreas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_areas_card, parent, false);
        ViewHolder vh = new ViewHolder(v);

        RxView.clicks(vh.cardView).subscribe(aVoid ->
                problemAreaSelected(vh, vh.textView.getText().toString()));
        return vh;
    }

    private void problemAreaSelected(ViewHolder holder, String name) {
        callback.onProblemAreaSelected(holder, name);
        selectedPos = holder.getLayoutPosition();
        notifyItemChanged(selectedPos);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(problemAreas[position]);
        holder.textView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return problemAreas.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView textView;



        public ViewHolder(View v) {
            super(v);
            cardView = v;
            cardView.setClickable(true);
            textView = (TextView) v.findViewById(R.id.problem_area_name);
        }
    }

    public interface IProblemAreaSelector {
        void onProblemAreaSelected(ViewHolder vh, String name);
    }
}
