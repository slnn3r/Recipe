package com.example.user.recipe.Interface;

import android.content.Context;

import com.example.user.recipe.Model.Recipe;

import java.util.List;

/**
 * Created by Slnn3R on 22/2/2018.
 */

public interface PresenterInterface {

    interface Presenter{

        void updateSpinner(Context mContext);

        void getRecipeList(ViewInterface.MainView mainView, Context mContext, String recipeType);

        void updateListView(List<String> recipeListData);

        void createRecipeData(ViewInterface.CreateView createView, final Context mContext,Recipe recipeData);

        void createResult(boolean result);

        void getRecipeDetails(ViewInterface.DetailsView detailsView, Context mContext, String recipeID);

        void updateField(Recipe recipeDetails);

        void updateRecipeData(ViewInterface.DetailsView detailsView, Context mContext, Recipe recipeDetails);

        void updateResult(boolean result);

        void deleteRecipeData(ViewInterface.DetailsView detailsView, Context mContext, String recipeID);

        void deleteResult(boolean result);

        void serverError();

    }




}
