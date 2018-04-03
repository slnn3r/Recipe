package com.example.user.recipe.Interface;

import android.content.Context;

import com.example.user.recipe.Model.Recipe;

/**
 * Created by Slnn3R on 22/2/2018.
 */

public interface ModelInterface {

    interface Maintenance {

        void getList(ViewInterface.MainView mainView, Context mContext, String recipeType);

        void createData(ViewInterface.CreateView createView, final Context mContext,Recipe recipeData);

        void getDetails(ViewInterface.DetailsView detailsView, Context mContext, String recipeID);

        void updateData(ViewInterface.DetailsView detailsView, Context mContext, Recipe recipeDetails);

        void deleteData(ViewInterface.DetailsView detailsView, Context mContext, String recipeID);

    }


}
