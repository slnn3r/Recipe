package com.example.user.recipe.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.user.recipe.Interface.PresenterInterface;
import com.example.user.recipe.Interface.ViewInterface;
import com.example.user.recipe.Model.AccessDatabase;
import com.example.user.recipe.Model.Recipe;
import com.example.user.recipe.R;
import com.example.user.recipe.Presenter.Presenter;


public class CreateActivity extends AppCompatActivity implements ViewInterface.CreateView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create);

        // call the presenter to update the spinner
        final PresenterInterface.Presenter presenter = new Presenter(this, new AccessDatabase());
        presenter.updateSpinner(this.getApplicationContext());

    }


    // Executed When Create button was clicked
    public void create(View v){

        EditText nameInput = (EditText) findViewById(R.id.recipeNameFieldCreate);
        Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeCreate);
        EditText ingredientInput = (EditText) findViewById(R.id.ingredientFieldCreate);
        EditText stepInput = (EditText) findViewById(R.id.stepFieldCreate);

        String recipeName = nameInput.getText().toString();
        String recipeType = typeInput.getSelectedItem().toString();
        String recipeIngredient = ingredientInput.getText().toString();
        String recipeStep = stepInput.getText().toString();

        // validation to ensure all field was enter with valid data
        if(recipeName.equals("")||recipeName.equals(" ")){
            Toast toast = Toast.makeText(getApplicationContext(), "Name Field Cannot Be Empty, Please Try Again.", Toast.LENGTH_SHORT);
            toast.show();
            nameInput.setHintTextColor(Color.RED);
            nameInput.requestFocus();
            nameInput.setHint("Name is empty.");

        }else if(recipeIngredient.equals("")||recipeIngredient.equals(" ")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ingredient Field Cannot Be Empty, Please Try Again.", Toast.LENGTH_SHORT);
            toast.show();
            ingredientInput.setHintTextColor(Color.RED);
            ingredientInput.requestFocus();
            ingredientInput.setHint("Ingredient is empty.");
        }else if(recipeStep.equals("")||recipeStep.equals(" ")){
            Toast toast = Toast.makeText(getApplicationContext(), "Step Field Cannot Be Empty, Please Try Again.", Toast.LENGTH_SHORT);
            toast.show();
            stepInput.setHintTextColor(Color.RED);
            stepInput.requestFocus();
            stepInput.setHint("Step is empty.");
        }else{

            Recipe recipeData= new Recipe();
            recipeData.setRecipeName(recipeName);
            recipeData.setRecipeType(recipeType);
            recipeData.setRecipeIngredient(recipeIngredient);
            recipeData.setRecipeStep(recipeStep);

            // call the confirmation dialog to pop out
            createRecipeConfirmation(recipeData);

        }


    }

    // confirmation dialog that pop out when Create Button is clicked
    public void createRecipeConfirmation(final Recipe recipeData) {

        final ViewInterface.CreateView createView = this;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Confirm Create Recipe?");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        // call Presenter to ask Model to insert data into database
                        PresenterInterface.Presenter presenter = new Presenter(createView, new AccessDatabase());
                        presenter.createRecipeData(createView,getApplicationContext(),recipeData);

                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    @Override
    public void onBackPressed() {

    }

    // if Create successfully will pop out Successful message (called by presenter)
    @Override
    public void createRecipeResult(Boolean result) {
        if(result){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Create Successful")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(myIntent, 0);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }
    }

    // update the spinner (called by presenter)
    @Override
    public void listSpinnerItem(String[] arraySpinner) {

        Spinner s = (Spinner) findViewById(R.id.spinnerTypeCreate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);


    }

    // display error message (called by presenter)
    @Override
    public void displayErrorMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Operation Failed, In Offline Mode or Something Went Wrong.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
