package com.example.kosovo.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kosovo.repository.UserRepository;
import com.example.kosovo.entity.auth.AuthResponse;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<AuthResponse> login(LoginActivity.UserInfo userInfo){
        return userRepository.login(userInfo);
    }

}
