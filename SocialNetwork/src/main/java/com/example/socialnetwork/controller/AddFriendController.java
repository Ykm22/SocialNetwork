package com.example.socialnetwork.controller;

import com.example.socialnetwork.business.SocialNetwork;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.ValidationException;
import com.example.socialnetwork.repository.RepositoryException;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class AddFriendController implements Observer<UserEntityChangeEvent> {
    SocialNetwork socialNetwork;
    User loggedUser;
    ObservableList<String> model = FXCollections.observableArrayList();
    @FXML
    ListView<String> listview_Users;
    @FXML
    TextField textfield_EmailFilter;
    @FXML
    Button button_SendRequest;
    
    public void handleSendFriendRequest(ActionEvent actionEvent) {
        String email_receiver = textfield_EmailFilter.getText();
        try{
            socialNetwork.saveFriend(loggedUser.getEmail(), email_receiver, LocalDateTime.now(), "PENDING");
        } catch(RepositoryException e){
            AlertMessage.showErrorMessage(null, e.getMessage());
        } catch(ValidationException e){
            AlertMessage.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void initialize(){
        listview_Users.setItems(model);
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }

    private void initModel() {
        model.setAll(socialNetwork.getUsers().stream()
                .map(User::getEmail)
                .filter(user_email -> !user_email.equals(loggedUser.getEmail()))
                .collect(Collectors.toList())
        );
    }

    public void setLoggedUser(User user) {
        loggedUser = user;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
        socialNetwork.addObserver(this);
        initModel();
    }

    public void handleEmailFilterTextChanged(KeyEvent actionEvent) {
        String email = textfield_EmailFilter.getText();
        model.setAll(socialNetwork.getUsers().stream()
                .map(User::getEmail)
                .filter(userEmail -> userEmail.startsWith(email))
                .filter(userEmail -> !userEmail.equals(loggedUser.getEmail()))
                .collect(Collectors.toList())
        );
    }

    public void handleMouseClicked(MouseEvent mouseEvent) {
        String email = listview_Users.getSelectionModel().getSelectedItem().toString();
        textfield_EmailFilter.setText(email);
        System.out.println(email);
    }
}
