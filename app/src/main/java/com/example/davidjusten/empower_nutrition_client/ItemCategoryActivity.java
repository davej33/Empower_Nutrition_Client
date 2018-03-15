package com.example.davidjusten.empower_nutrition_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ItemCategoryActivity extends AppCompatActivity {

    private static final String SMOOTHIE = "Smoothie";
    private static final String SHOT = "Shot";
    private static final String BOWL = "Bowl";
    private static final String JUICE = "Juice";
    private static final String TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category);
    }

    public void smoothieClicked(View view) {
        Intent smoothieIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        smoothieIntent.putExtra(TYPE, SMOOTHIE);
        startActivity(smoothieIntent);
    }

    public void juiceClicked(View view) {
        Intent juiceIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        juiceIntent.putExtra(TYPE, JUICE);
        startActivity(juiceIntent);
    }

    public void shotsClicked(View view) {
        Intent shotIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        shotIntent.putExtra(TYPE, SHOT);
        startActivity(shotIntent);
    }

    public void bowlsClicked(View view) {
        Intent bowlsIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        bowlsIntent.putExtra(TYPE, BOWL);
        startActivity(bowlsIntent);
    }
}
