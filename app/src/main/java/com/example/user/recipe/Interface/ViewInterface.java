package com.example.user.recipe.Interface;

import android.content.Context;
import android.database.Cursor;

import com.example.user.recipe.Model.Recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Slnn3R on 22/2/2018.
 */

public interface ViewInterface {

    interface MainView{

        void listSpinnerItem(String [] arraySpinner);

        void updateListViewItem(List<String> recipeListData);

        void displayErrorMsg();

    }

    interface CreateView{

        void listSpinnerItem(String [] arraySpinner);

        void createRecipeResult(Boolean result);

        void displayErrorMsg();
    }

    interface DetailsView{

        void listSpinnerItem(String [] arraySpinner);

        void updateItemField(Recipe recipeDetails);

        void updateRecipeResult(Boolean result);

        void deleteRecipeResult(Boolean result);

        void displayErrorMsg();

    }



}
