package com.example.socialnetwork.controller;

import com.example.socialnetwork.MainApplication;
import com.example.socialnetwork.business.SocialNetwork;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.RepositoryException;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController implements Observer<UserEntityChangeEvent> {
    SocialNetwork socialNetwork;
//    ObservableList<User> model = FXCollections.observableArrayList();
//    ArrayList<LoginController> loggedUsers = new ArrayList<>();
//    private static final int MAX_USERS_TABLE_ITEMS = 7;
//    private int current_page, page_before_filter;
//    private boolean filtering_status = false;
//    private String filtering_FirstName = null;
//    @FXML
//    TableView<User> tableView_Users;
//    @FXML
//    TableColumn<User, String> tableColumn_Users_FirstName;
//    @FXML
//    TableColumn<User, String> tableColumn_Users_LastName;
//    @FXML
//    TableColumn<User, String> tableColumn_Users_Email;
//    @FXML
//    TextField textField_FirstName_CRUD;
//    @FXML
//    TextField textField_LastName_CRUD;
//    @FXML
//    PasswordField passwordField_Password_CRUD;
//    @FXML
//    TextField textField_Email_CRUD;
//    @FXML
//    Label label_UsersPage;
//    @FXML
//    TextField textField_FirstName_Filter;
    @FXML
    TextField textField_Email_Login;
    @FXML
    PasswordField passwordField_Password_Login;

    private void initModel() {
    }

    @FXML
    public void initialize(){
        initializeUsersTable();
    }

    private void initializeUsersTable() {
    }

    public void setSocialNetwork(SocialNetwork socialNetwork){
        this.socialNetwork = socialNetwork;
        socialNetwork.addObserver(this);
        initModel();
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }



    public void handleLogin(ActionEvent actionEvent){
        String login_email = textField_Email_Login.getText();
        String login_password = passwordField_Password_Login.getText();
        try{
            User login_user = socialNetwork.getUser(login_email);
            if(!login_user.getPassword().equals(login_password)){
                throw new RepositoryException("Wrong password");
            }

            FXMLLoader fxmlLoaderFriendships = new FXMLLoader(MainApplication.class.getResource("views/FriendshipsView.fxml"));
            Parent friendshipsLayout = fxmlLoaderFriendships.load();

            UserController friendshipsController = fxmlLoaderFriendships.getController();
            friendshipsController.setLoggedUser(login_user);
            friendshipsController.setSocialNetwork(socialNetwork);
            friendshipsController.setStage((Stage)textField_Email_Login.getScene().getWindow());

            FXMLLoader fxmlLoaderAddFriends = new FXMLLoader(MainApplication.class.getResource("views/AddFriendView.fxml"));
            Parent addFriendLayout = fxmlLoaderAddFriends.load();

            AddFriendController addFriendController = fxmlLoaderAddFriends.getController();
            addFriendController.setLoggedUser(login_user);
            addFriendController.setSocialNetwork(socialNetwork);


            TabPane tabPane = new TabPane();

            Tab friendshipsTab = new Tab("Friendships", friendshipsLayout);
            Tab addFriendshipsTab = new Tab("Send requests", addFriendLayout);

            tabPane.getTabs().add(friendshipsTab);
            tabPane.getTabs().add(addFriendshipsTab);

            VBox vBox = new VBox(tabPane);
            Scene scene = new Scene(vBox);
            Stage tabStage = new Stage();
            tabStage.setScene(scene);
            tabStage.initStyle(StageStyle.DECORATED);
            tabStage.setTitle("Welcome, " + login_user.getFirst_name());
            tabStage.show();

            Stage loginStage = (Stage)textField_Email_Login.getScene().getWindow();
            loginStage.close();
        } catch(RepositoryException e){
            AlertMessage.showErrorMessage(null, e.getMessage());
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
