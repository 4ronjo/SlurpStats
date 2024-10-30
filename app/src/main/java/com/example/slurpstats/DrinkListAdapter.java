package com.example.slurpstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrinkListAdapter extends ArrayAdapter<Drink> {

    private Context context;
    private List<Drink> drinks;
    private boolean isSelectionMode;
    public List<Boolean> checkedStates;

    public DrinkListAdapter(Context context, List<Drink> drinks) {
        super(context, 0, drinks);
        this.context = context;
        this.drinks = drinks;
        this.isSelectionMode = false;
        this.checkedStates = new ArrayList<>(Collections.nCopies(drinks.size(), false));
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!selectionMode) {
            checkedStates = new ArrayList<>(Collections.nCopies(drinks.size(), false));
        }
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    public List<Drink> getSelectedDrinks() {
        List<Drink> selectedDrinks = new ArrayList<>();
        for (int i = 0; i < drinks.size(); i++) {
            if (checkedStates.get(i)) {
                selectedDrinks.add(drinks.get(i));
            }
        }
        return selectedDrinks;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drink_list_item, parent, false);
        }

        Drink drink = drinks.get(position);

        TextView nameTextView = convertView.findViewById(R.id.drink_name);
        CheckBox checkBox = convertView.findViewById(R.id.drink_checkbox);

        nameTextView.setText(drink.getName() + " (" + drink.getAlcoholContent() + "%)");

        if (isSelectionMode) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(checkedStates.get(position));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> checkedStates.set(position, isChecked));
        } else {
            checkBox.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }
}
