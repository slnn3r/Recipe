package com.example.user.recipe.Model;

/**
 * Created by Slnn3R on 22/2/2018.
 */

public class Recipe {

    private String RecipeID;
    private String RecipeName;
    private String RecipeType;
    private String RecipeIngredient;
    private String RecipeStep;
    private String RecipeStatus;

    public Recipe() {
    }


    public String getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(String recipeID) {
        RecipeID = recipeID;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getRecipeType() {
        return RecipeType;
    }

    public void setRecipeType(String recipeType) {
        RecipeType = recipeType;
    }

    public String getRecipeIngredient() {
        return RecipeIngredient;
    }

    public void setRecipeIngredient(String recipeIngredient) {
        RecipeIngredient = recipeIngredient;
    }

    public String getRecipeStep() {
        return RecipeStep;
    }

    public void setRecipeStep(String recipeStep) {
        RecipeStep = recipeStep;
    }

    public String getRecipeStatus() {
        return RecipeStatus;
    }

    public void setRecipeStatus(String recipeStatus) {
        RecipeStatus = recipeStatus;
    }

}
