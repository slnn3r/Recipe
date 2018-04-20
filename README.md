# **Android Java Recipe Apps Architecture**

Android Java Recipe Apps uses the Model-View-Presenter (MVP) Pattern. With MVP Pattern, the presentation layer is separated from the logic which ensure that everything related to the logic behind is works separatedly from the presentation of the screen.

## MVP Pattern

There are 3 layers which are View, Presenter and Model. Each layers' responsibilities are separated to allow the program to have better maintainability and also allow the code to be easier to reuse and more readable.

![A][MVP]

**(Figure 1: The figure above illustrate the basic MVP structure)**

### View

In this project, View solely responsible to deal with the handling of user interactions and inputs such as list view click listensers and triggers the appropriate function of the Presenter when necessary. Also, it is use to display the data to the user interface that is sent by the presenters.

### Presenter

In this project, Presenter is responsible to perform some logically implementation such as Reading the Recipe Category from XML File, triggers the function from Model to receive the data back send it back to View upon the request.

Code Snippet of Presenter perform a Logical Implementation to Reading XML File:

      // Presenter Layer
      public class Presenter implements PresenterInterface.Presenter{
              ...
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
      }



### Model

In this project, Model solely deal with the implementations to retrieve data from the database (000Webhost - PHPMySQL) upon request from the View that passed by the Presenter. After finished its data retrieving, it will return the retrieved data back to the Presenter. Not only that, it also perform inserting data to the database without return or retrieve any data from database.


**MVP Flow Example of Android Java Recipe Apps:**

1. In Main Screen of Recipe Apps which is the View Layer, it will call the `getRecipeList()` function of the Presenter Layer to retrieve Recipe List Data from the Model Layer and return the Recipe List Data back so that the View Layer can to display the Recipe List on the Screen.

        // View Layer
        public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener, ViewInterface.MainView{
                    ...
                    public void setupSpinner(){
                        ...
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            ...
                            // Call the Function of Presenter to get the Recipe List
                            presenter.getRecipeList(mainView,getApplicationContext(),typeValue);
                        });
                    }
        }

2. The `getRecipeList()` function of the Presenter Layer received the call from View Layer, then it call the `getList()` function of the Model Layer in order to retrieve the Recipe List from the database.

        // Presenter Layer
        public class Presenter implements PresenterInterface.Presenter{
            ...
            @Override
            public void getRecipeList(ViewInterface.MainView mainView,Context mContext,String recipeType) {
              // Presenter Call Function of Model to retrieve the Recipe List from database
              maintenance.getList(mainView,mContext,recipeType);
            }
        }

3. The `getList()` function of the Model Layer received the call from Presenter Layer, then it execute the implementations of retrieving the Recipe List from database. After it complete the retrieve, it will store the Recipe List into a `ArrayList Variable` and call the `updateListView()` function of Presenter Layer to pass the `ArrayList Variable` back to the View Layer.

        // Model Layer
        public class AccessDatabase implements ModelInterface.Maintenance{
          ...
            @Override
            public void getList(ViewInterface.MainView mainView, final Context mContext, final String recipeType) {
                  ...
                  StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                      @Override
                      public void onResponse(String response) {
                          try {
                          ...
                            // After finished the Retrieve, pass the ArrayList back to Presenter
                            // Then ask Presenter to tell View to update the ListView with the data in the ArrayList
                            presenter.updateListView(your_array_list);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              }
          }

4. The `updateListView()` function of Presenter Layer received the call from the Model Layer and obtain the `ArrayList Variable` that store the Recipe List. Then, it call the `updateListViewItem()` function of View Layer and pass the `ArrayList Variable` to the View Layer for it to display the Recipe List on the Screen.

        // Presenter Layer
        public class Presenter implements PresenterInterface.Presenter{
            ...
            @Override
            public void updateListView(List<String> recipeListData) {
                // Presenter Layer Call the View to update the List View to with the Recipe List Data
                mainView.updateListViewItem(recipeListData);
            }
        }

5. The Final Stage where the `updateListViewItem()` function of View Layer received the call from Presenter Layer and obtain the `ArrayList Variable` that store the Recipe List. Now, it will perform the implementations to use the Recipe List Data in the `ArrayList Variable` to display them on the Screen.

        // View Layer
        public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener, ViewInterface.MainView{
                    ...
                    // Called by Presenter to update the Recipe List Data into the List View to be display on the Screen
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
        }


**Highlights:**

From the MVP flow example above, you can clearly see that each Layer perform their task specifically and seperately. Where the View Layer solely used to perform user interface functionality that to display thing on the screen. Presenter Layer act as the middle man of View and Model to create a connection which help to receive request from View Layer and send the response of Model Layer back to the View Layer. Lastly, Model Layer is the place to solely deal with retrieving of data from the Database and send back the requested data upon request.




[MVP]:https://cdn.journaldev.com/wp-content/uploads/2017/08/android-mvp-flow.png
