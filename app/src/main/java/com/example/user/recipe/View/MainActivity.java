package com.example.user.recipe.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.recipe.Model.AccessDatabase;
import com.example.user.recipe.Presenter.Presenter;
import com.example.user.recipe.Interface.PresenterInterface;
import com.example.user.recipe.R;
import com.example.user.recipe.Interface.ViewInterface;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewInterface.MainView{


    List<String> get_array_list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        setupSpinner();
        setupListView();
    }

    // Setup the Initial UI, eg. Navigation Drawer, etc
    public void setupUI(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    // Setup the Spinner listener, Fill Up the Spinner with data
    public void setupSpinner(){

        final ViewInterface.MainView mainView = this;
        final PresenterInterface.Presenter presenter = new Presenter(this, new AccessDatabase());
        final Spinner typeInput = (Spinner) findViewById(R.id.spinnerTypeView);


        // Tell the Presenter going to fill up the Spinner List Item
        // Call the Function in Presenter
        presenter.updateSpinner(this.getApplicationContext());

        // Spinner Listener
        typeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String typeValue = typeInput.getSelectedItem().toString();

                // Whenever a Item is Selected in the Spinner, will update the List View Data
                // Call the Function in Presenter to get the Recipe List
                presenter.getRecipeList(mainView,getApplicationContext(),typeValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });



    }


    //Setup the ListView Listener
    public void setupListView(){

        final ListView recipeList = (ListView) findViewById(R.id.recipeList);

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {


                String selectedRecipe=get_array_list.get(position);

                    // When ListView with Data Click will proceed to the Recipe Detail Screen
                  if(selectedRecipe!="No Results"){
                    Intent myIntent = new Intent(getApplicationContext(), RecipeDetails.class);

                    //pass the RecipeID to the Recipe Detail Screen to retrieve data based on the Recipe ID
                     myIntent.putExtra("ID",selectedRecipe.substring(0,selectedRecipe.indexOf('-')));
                     startActivity(myIntent);
                  }else{
                      Toast toast = Toast.makeText(getApplicationContext(), "Go to Navigation Drawer to Create New Recipe.", Toast.LENGTH_SHORT);
                      toast.show();
                  }



            }
        });

    }

    // Called by Presenter to fill up the Spinner with Data
    @Override
    public void listSpinnerItem(String [] arraySpinner) {

        Spinner s = (Spinner) findViewById(R.id.spinnerTypeView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);

    }

    // Called by Presenter to update the data in the List View
    @Override
    public void updateListViewItem(List<String> your_array_list) {

        get_array_list = your_array_list;

        final ListView recipeList = (ListView) findViewById(R.id.recipeList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        recipeList.setAdapter(arrayAdapter);


    }

    // Use to display error message when Server operation failed
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


    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // go to the Create Recipe Screen to create new recipe
        if (id == R.id.nav_createRecipe) {
            Intent goChange = new Intent(this, CreateActivity.class);
            startActivity(goChange);

        }else {
            finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
