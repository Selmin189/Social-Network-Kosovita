package com.example.kosovo.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kosovo.repository.UserRepository;
import com.example.kosovo.entity.search.SearchResponse;

import java.util.Map;

public class SearchViewModel extends ViewModel {
    private UserRepository userRepository;

    public SearchViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<SearchResponse> search(Map<String,String> params){
        return userRepository.search(params);
    }
}
