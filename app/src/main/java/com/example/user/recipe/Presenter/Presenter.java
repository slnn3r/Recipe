package com.example.user.recipe.Presenter;

import android.content.Context;

import com.example.user.recipe.Interface.ModelInterface;
import com.example.user.recipe.Interface.PresenterInterface;
import com.example.user.recipe.Interface.ViewInterface;
import com.example.user.recipe.Model.Recipe;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Slnn3R on 21/2/2018.
 */

public class Presenter implements PresenterInterface.Presenter{

    ViewInterface.MainView mainView;
    ModelInterface.Maintenance maintenance;
    ViewInterface.CreateView createView;
    ViewInterface.DetailsView detailsView;



    public Presenter(ViewInterface.MainView mainView, ModelInterface.Maintenance maintenance) {
        this.mainView = mainView;
        this.maintenance = maintenance;

    }

    public Presenter(ViewInterface.CreateView createView, ModelInterface.Maintenance maintenance) {
        this.createView = createView;
        this.maintenance = maintenance;

    }

    public Presenter(ViewInterface.DetailsView detailsView, ModelInterface.Maintenance maintenance) {
        this.detailsView = detailsView;
        this.maintenance = maintenance;

    }

    // Presenter Call the Implementation Function in MainActivity to fill up the Spinner (Called by View)
    @Override
    public void updateSpinner(Context mContext) {


        String[] arraySpinner = new String[0];

        // Read the data from XML File and store in StringArray
        try {
            InputStream is = mContext.getAssets().open("recipetypes.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("recipetype");
            arraySpinner = new String[nList.getLength()];
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    arraySpinner[i]=getValue("name",element2);


                }
            }

        } catch (Exception e) {e.printStackTrace();}

        // Pass the StringArray back to View to fill up the Spinner with Data
        if(mainView!=null){
            mainView.listSpinnerItem(arraySpinner);
        }else if(createView!=null){
            createView.listSpinnerItem(arraySpinner);
        }else{
            detailsView.listSpinnerItem(arraySpinner);
        }



    }

    // Presenter Call Implementation Function of the Model to retrieve the Recipe List (Called by View)
    @Override
    public void getRecipeList(ViewInterface.MainView mainView,Context mContext,String recipeType) {
        maintenance.getList(mainView,mContext,recipeType);
    }

    // Presenter Call the Implementation Function of the View to update the List View (Called by Model)
    @Override
    public void updateListView(List<String> recipeListData) {
        mainView.updateListViewItem(recipeListData);
    }

    // Presenter Pass the data from View and Call the Model to insert data to database (Called by View)
    @Override
    public void createRecipeData(ViewInterface.CreateView createView, Context mContext, Recipe recipeData) {
        maintenance.createData(createView, mContext, recipeData);
    }

    // Presenter call the function in View to display the Successful Create Data message (Called by Model)
    @Override
    public void createResult(boolean result) {
        createView.createRecipeResult(result);
    }


    // Presenter pass the Recipe ID from View and call the Model the get the Recipe Details (Called by View)
    @Override
    public void getRecipeDetails(ViewInterface.DetailsView detailsView, Context mContext, String recipeID) {
        maintenance.getDetails(detailsView, mContext, recipeID);
    }

    // Presenter pass data retrieve from Model and call the View to update the Data Field (Called by Model)
    @Override
    public void updateField(Recipe recipeDetails) {
        detailsView.updateItemField(recipeDetails);
    }

    // Presenter pass the Recipe Data and call the Model to update the data in database (Called by View)
    @Override
    public void updateRecipeData(ViewInterface.DetailsView detailsView, Context mContext, Recipe recipeDetails) {
        maintenance.updateData(detailsView, mContext, recipeDetails);
    }

    // Presenter call the function in View to display the Successful Update Data message (Called by Model)
    @Override
    public void updateResult(boolean result) {
        detailsView.updateRecipeResult(result);
    }

    // Presenter pass the Recipe ID and call the Model to delete the data in database (Called by View)
    @Override
    public void deleteRecipeData(ViewInterface.DetailsView detailsView, Context mContext, String recipeID) {
        maintenance.deleteData(detailsView, mContext, recipeID);
    }

    // Presenter call the function in View to display the Successful Delete Data message (Called by Model)
    @Override
    public void deleteResult(boolean result) {
        detailsView.deleteRecipeResult(result);
    }

    // Presenter call the View to display error message when operation failed in server (Called by Model)
    @Override
    public void serverError() {
        if(mainView!=null){
            mainView.displayErrorMsg();
        }else if(createView!=null){
            createView.displayErrorMsg();
        }else{
            detailsView.displayErrorMsg();
        }
    }

    // Part of read XML file function (to get the specify value in the XML file)
    public String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }



}
