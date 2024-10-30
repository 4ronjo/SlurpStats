package com.example.slurpstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultListAdapter extends ArrayAdapter<Result> {

    private Context context;
    private List<Result> results;
    private boolean isSelectionMode;
    public List<Boolean> checkedStates;

    public ResultListAdapter(Context context, List<Result> results) {
        super(context, 0, results);
        this.context = context;
        this.results = results;
        this.isSelectionMode = false;
        this.checkedStates = new ArrayList<>(Collections.nCopies(results.size(), false));
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!selectionMode) {
            checkedStates = new ArrayList<>(Collections.nCopies(results.size(), false));
        }
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    public List<Result> getSelectedResults() {
        List<Result> selectedResults = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (checkedStates.get(i)) {
                selectedResults.add(results.get(i));
            }
        }
        return selectedResults;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.result_list_item, parent, false);
        }

        Result result = results.get(position);

        TextView titleTextView = convertView.findViewById(R.id.result_title);
        CheckBox checkBox = convertView.findViewById(R.id.result_checkbox);

        titleTextView.setText(result.getTitle() + " - " + result.getDate());

        if (isSelectionMode) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(checkedStates.get(position));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedStates.set(position, isChecked);
                }
            });
        } else {
            checkBox.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return results.size();
    }
}
