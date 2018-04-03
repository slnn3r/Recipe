package com.example.user.recipe.Model;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.recipe.Interface.ModelInterface;
import com.example.user.recipe.Presenter.Presenter;
import com.example.user.recipe.Interface.PresenterInterface;
import com.example.user.recipe.Interface.ViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Slnn3R on 21/2/2018.
 */

    // Everything here called by Presenter
    public class AccessDatabase implements ModelInterface.Maintenance{

    // Implementation to retrieve the Recipe List then Pass the retrieved Data
    // to Presenter to ask View to Update the ListView
    @Override
    public void getList(ViewInterface.MainView mainView, final Context mContext, final String recipeType) {

        // Implementation to retrieve Data from WebHost Database
        String url = "https://unsurfaced-cross.000webhostapp.com/getRecipeList.php";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        final PresenterInterface.Presenter presenter = new Presenter(mainView, new AccessDatabase());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("RecipeList");

                    List<String> your_array_list = new ArrayList<String>();


                    int count = jsonarray.length();

                    if(count>0){

                        int loop=0;

                        while(loop<count){

                            // Store the retrieve Data into your_array_list(ArrayList)
                            JSONObject data = jsonarray.getJSONObject(loop);
                            your_array_list.add(data.getString("RecipeID")+"- "+data.getString("RecipeName"));

                            loop+=1;

                        }



                    }else{
                        your_array_list.add("No Results");
                    }

                    // After Finish Retrieve pass the your_array_list(ArrayList)
                    // back to Presenter and ask Presenter to tell View to update the ListView
                    presenter.updateListView(your_array_list);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.serverError();            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("RecipeType", recipeType);

                return parameters;
            }
        };

        queue.add(stringRequest);

    }


    // Implementation to create the Recipe into the database
    @Override
    public void createData(ViewInterface.CreateView createView, final Context mContext, final Recipe recipeData) {

        String url = "https://unsurfaced-cross.000webhostapp.com/createRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        final PresenterInterface.Presenter presenter = new Presenter(createView, new AccessDatabase());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    //call the presenter to tell view the operation is success
                    presenter.createResult(true);

                    JSONObject jsonobject = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.serverError();            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();


                parameters.put("RecipeName", recipeData.getRecipeName());
                parameters.put("RecipeType", recipeData.getRecipeType());
                parameters.put("RecipeIngredient", recipeData.getRecipeIngredient());
                parameters.put("RecipeStep", recipeData.getRecipeStep());


                return parameters;
            }
        };

        queue.add(stringRequest);



    }



    // Implementation to retrieve the Recipe Detail Data then Pass the retrieved Data
    // to Presenter to ask View to Update the Recipe Data Field
    @Override
    public void getDetails(ViewInterface.DetailsView detailsView, Context mContext, final String recipeID) {

        String url = "https://unsurfaced-cross.000webhostapp.com/getRecipeDetail.php";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        final PresenterInterface.Presenter presenter = new Presenter(detailsView, new AccessDatabase());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("RecipeDetail");

                    JSONObject data = jsonarray.getJSONObject(0);

                    Recipe recipeDetails= new Recipe();

                    recipeDetails.setRecipeName(data.getString("RecipeName"));
                    recipeDetails.setRecipeType(data.getString("RecipeType"));
                    recipeDetails.setRecipeIngredient(data.getString("RecipeIngredient"));
                    recipeDetails.setRecipeStep(data.getString("RecipeStep"));

                    // pass the Retrieved data and Call the presenter to tell View to update the Recipe Detail Field
                    presenter.updateField(recipeDetails);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.serverError();            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("RecipeID", recipeID);

                return parameters;
            }
        };

        queue.add(stringRequest);


    }

    // Implementation to get the user input data and update the data into database
    @Override
    public void updateData(ViewInterface.DetailsView detailsView, Context mContext, final Recipe recipeDetails) {

        String url = "https://unsurfaced-cross.000webhostapp.com/updateRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        final PresenterInterface.Presenter presenter = new Presenter(detailsView, new AccessDatabase());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    //call the presenter to tell view the operation is success
                    presenter.updateResult(true);

                    JSONObject jsonobject = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.serverError();            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();


                parameters.put("RecipeID",recipeDetails.getRecipeID());
                parameters.put("RecipeName", recipeDetails.getRecipeName());
                parameters.put("RecipeType", recipeDetails.getRecipeType());
                parameters.put("RecipeIngredient", recipeDetails.getRecipeIngredient());
                parameters.put("RecipeStep", recipeDetails.getRecipeStep());


                return parameters;
            }
        };

        queue.add(stringRequest);


    }

    // Implementation to get the input Recipe ID and delete the data from database
    @Override
    public void deleteData(ViewInterface.DetailsView detailsView, Context mContext, final String recipeID) {

        String url = "https://unsurfaced-cross.000webhostapp.com/deleteRecipe.php";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        final PresenterInterface.Presenter presenter = new Presenter(detailsView, new AccessDatabase());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    //call the presenter to tell view the operation is success
                    presenter.deleteResult(true);

                    JSONObject jsonobject = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                presenter.serverError();            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();


                parameters.put("RecipeID",recipeID);


                return parameters;
            }
        };

        queue.add(stringRequest);


    }
}




