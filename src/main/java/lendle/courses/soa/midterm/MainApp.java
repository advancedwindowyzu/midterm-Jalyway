package lendle.courses.soa.midterm;

import java.io.File;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

public class MainApp extends Application {

    private TabPane tabPane = null;
    private ListView<String> recentList = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FlowPane menuPane = new FlowPane();
        Button webButton = new Button("Browser");
        Button imageButton = new Button("Image");
        menuPane.getChildren().addAll(webButton, imageButton);
        root.setTop(menuPane);

        tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        root.setCenter(tabPane);

        recentList = new ListView<String>();
        root.setLeft(recentList);
        //1. process recentList
        //1.1 (10%) create ObservableList and assign it to the items property of recentList
        //1.2 (20%)handle double click on recentList; on double-clicking, open the selected url (u have to distinguish between an image url and a web url)
        ObservableList<String> items = FXCollections.observableArrayList();
        recentList.setItems(items);
        recentList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (recentList.getSelectionModel().getSelectedItem().startsWith("http") == false) {
                        openImageTab(primaryStage, recentList.getSelectionModel().getSelectedItem());
                    } else {
                        openWebTab(recentList.getSelectionModel().getSelectedItem());
                    }

                }
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        webButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openWebTab(null);
            }

        });

        imageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openImageTab(primaryStage, null);

            }
        });
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void openWebTab(String url) {
        BorderPane mainPane = new BorderPane();
        BorderPane addressPane = new BorderPane();
        final TextField address = new TextField(url);
        Button goButton = new Button("GO");
        addressPane.setCenter(address);
        addressPane.setRight(goButton);
        mainPane.setTop(addressPane);
        WebView webView = new WebView();
        mainPane.setCenter(webView);
        webView.getEngine().setJavaScriptEnabled(true);
        //2. process webView
        //2.1 (10%) add it to a tab
        //2.2 (20%) handle action event on the goButton; goto to the url entered in address field
        //2.3 (10%) add the url to recentList via recentList.getItems()
        tabPane.getTabs().add(new Tab("Browser", mainPane));
        webView.getEngine().load(url);
        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                webView.getEngine().load(address.getText());
                recentList.getItems().add(address.getText());
            }
        });
    }

    private void openImageTab(Stage stage, String url) {
        if (url == null) {
            //3. (20%) open a fileChooser to allow the user to select a local image file
            //////////////////////////////////////////////////////////////////////
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                url = file.toURI().toString();
            }
            if (recentList.getItems().contains(url) == false) {
                recentList.getItems().add(url);
            }
        }
        if (url != null) {
            //4. (10%) load the image into a imageView
            Image img = new Image(url);
            ImageView imageView = new ImageView(img);

            ///////////////////////////////////////////
            ScrollPane scrollPane = new ScrollPane(imageView);
            tabPane.getTabs().add(new Tab(url, scrollPane));
        }
    }
}
