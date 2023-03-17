package com.example.socialnetwork.controller;

import com.example.socialnetwork.business.SocialNetwork;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.utils.events.UserEntityChangeEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController implements Observer<UserEntityChangeEvent> {
    User loggedUser, friendUser;
    SocialNetwork socialNetwork;
    @FXML
    ListView<String> listview_Chat;
    ObservableList<String> model = FXCollections.observableArrayList();
    @FXML
    TextField textfield_WriteMessage;
    @FXML
    public void initialize(){
        listview_Chat.setItems(model);
    }
    public void initModel(){
        model.setAll(socialNetwork.getMessagesBetween(loggedUser, friendUser));
        //socialNetwork.getMessagesBetween(loggedUser, friendUser).forEach(System.out::println);
    }
    public void setUsers(User loggedUser, User friendUser){
        this.loggedUser = loggedUser;
        this.friendUser = friendUser;
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

    public void handleWriteMessage(ActionEvent actionEvent) {
        String message = textfield_WriteMessage.getText();
        socialNetwork.sendMessage(loggedUser, friendUser, message);
//        System.out.println(message);
        textfield_WriteMessage.setText("");
    }
}
