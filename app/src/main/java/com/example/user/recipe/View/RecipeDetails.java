package com.example.user.recipe.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.recipe.Interface.PresenterInterface;
import com.example.user.recipe.Interface.ViewInterface;
import com.example.user.recipe.Model.AccessDatabase;
import com.example.user.recipe.Model.Recipe;
import com.example.user.recipe.Presenter.Presenter;
import com.example.user.recipe.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RecipeDetails extends AppCompatActivity implements ViewInterface.DetailsView{

    String recipeID = null ;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipe_details);
        count=0;
        Bundle extras = getIntent().getExtras();

        // get the Recipe ID that pass from the Main Activity Screen
        if (!extras.isEmpty()) {
            recipeID=(String) extras.getString("ID");
        }


        setupUI();

        final PresenterInterface.Presenter presenter = new Presenter(this, new AccessDatabase());
        // call presenter to fill in the spinner
        presenter.updateSpinner(this.getApplicationContext());

        // Call Presenter to ask Model to get the Recipe Data to fill in the data field
        presenter.getRecipeDetails(this,this.getApplicationContext(),recipeID);



    }

   // Executed When Update button was clicked
    public void update(View v) throws SQLException {

        EditText nameInput = (EditText) findViewById(R.id.recipeNameFieldUpdate);
        Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeUpdate);
        EditText ingredientInput = (EditText) findViewById(R.id.ingredientFieldUpdate);
        EditText stepInput = (EditText) findViewById(R.id.stepFieldUpdate);
        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        String recipeName = nameInput.getText().toString();
        String recipeType = typeInput.getSelectedItem().toString();
        String recipeIngredient = ingredientInput.getText().toString();
        String recipeStep = stepInput.getText().toString();

        // If Update Button is Clicked for the 1st Time, system will enable all Field to allow user to edit
        // If Update Button didn't clicked, Field will remain in View Mode and not editable
        if(count==0){
            nameInput.setFocusable(true);
            nameInput.setFocusableInTouchMode(true);
            nameInput.requestFocus();
            typeInput.setEnabled(true);
            ingredientInput.setFocusable(true);
            ingredientInput.setFocusableInTouchMode(true);
            stepInput.setFocusable(true);
            stepInput.setFocusableInTouchMode(true);
            buttonUpdate.setTextColor(Color.BLUE);
            buttonUpdate.setText("Click to Submit");
            Toast toast = Toast.makeText(getApplicationContext(), "Click on Field to Edit", Toast.LENGTH_SHORT);
            toast.show();
            count+=1;
        }else{ // If Update Button is Clicked for 2nd Time, it will Check the field and submit the update

            // Validation Checking ensure all input is valid
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
                recipeData.setRecipeID(recipeID);

                 recipeName = nameInput.getText().toString();
                 recipeType = typeInput.getSelectedItem().toString();
                 recipeIngredient = ingredientInput.getText().toString();
                 recipeStep = stepInput.getText().toString();

                recipeData.setRecipeName(recipeName);
                recipeData.setRecipeType(recipeType);
                recipeData.setRecipeIngredient(recipeIngredient);
                recipeData.setRecipeStep(recipeStep);

                // Display Confirmation dialog
                updateRecipeConfirmation(recipeData);

            }

        }

    }

    // confirmation dialog that pop out when update Button is clicked for the 2nd time
    public void updateRecipeConfirmation(final Recipe recipeData){
        final ViewInterface.DetailsView detailsView = this;


        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Confirm Update Recipe?");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        // Call presenter to tell Model to update the data to database
                        PresenterInterface.Presenter presenter = new Presenter(detailsView, new AccessDatabase());
                        presenter.updateRecipeData(detailsView,getApplicationContext(),recipeData);

                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void notice(View v){
        Toast toast = Toast.makeText(getApplicationContext(), "Click Update to Edit Field.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void delete(View v) throws SQLException {

        // Display Confirmation dialog
        deleteRecipeConfirmation(recipeID);


    }

    // confirmation dialog that pop out when Delete Button is clicked
    public void deleteRecipeConfirmation(final String recipeID){
        final ViewInterface.DetailsView detailsView = this;


        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Confirm Delete Recipe?");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        // Call presenter to tell Model to delete the data from database
                        PresenterInterface.Presenter presenter = new Presenter(detailsView, new AccessDatabase());
                        presenter.deleteRecipeData(detailsView,getApplicationContext(),recipeID);  //!!!!!!!!!!

                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
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


    // to fill in the spinner
    @Override
    public void listSpinnerItem(String[] arraySpinner) {

        Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeUpdate);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        typeInput.setAdapter(adapter);
    }


    // Called by presenter to receive data that retrieve by Model to fill in the Data Field
    @Override
    public void updateItemField(Recipe recipeDetails) {

        EditText nameInput = (EditText) findViewById(R.id.recipeNameFieldUpdate);
        Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeUpdate);
        EditText ingredientInput = (EditText) findViewById(R.id.ingredientFieldUpdate);
        EditText stepInput = (EditText) findViewById(R.id.stepFieldUpdate);
        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);

        nameInput.setEnabled(true);
        ingredientInput.setEnabled(true);
        stepInput.setEnabled(true);
        buttonUpdate.setEnabled(true);
        buttonDelete.setEnabled(true);

        nameInput.setFocusable(false);
        nameInput.setFocusableInTouchMode(false);
        typeInput.setEnabled(false);
        ingredientInput.setFocusable(false);
        ingredientInput.setFocusableInTouchMode(false);
        stepInput.setFocusable(false);
        stepInput.setFocusableInTouchMode(false);

        nameInput.setText(recipeDetails.getRecipeName());

        for(int i = 0; i < typeInput.getCount(); i++){

            String a =typeInput.getItemAtPosition(i).toString();

                if(typeInput.getItemAtPosition(i).toString().equals(recipeDetails.getRecipeType())){
                    typeInput.setSelection(i);
                    break;
               }
        }

          ingredientInput.setText(recipeDetails.getRecipeIngredient());
          stepInput.setText(recipeDetails.getRecipeStep());


    }

    // Display Error message,
    // Operation Result, ETC

    @Override
    public void updateRecipeResult(Boolean result) {
        if(result){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Update Successful")
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

    @Override
    public void deleteRecipeResult(Boolean result) {
        if (result) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete Successful")
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

    @Override
    public void displayErrorMsg() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Operation Failed, In Offline Mode or Something Went Wrong.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void setupUI(){
        EditText nameInput = (EditText) findViewById(R.id.recipeNameFieldUpdate);
        Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeUpdate);
        EditText ingredientInput = (EditText) findViewById(R.id.ingredientFieldUpdate);
        EditText stepInput = (EditText) findViewById(R.id.stepFieldUpdate);
        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);

        nameInput.setEnabled(false);
        typeInput.setEnabled(false);
        ingredientInput.setEnabled(false);
        stepInput.setEnabled(false);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
    }
}
