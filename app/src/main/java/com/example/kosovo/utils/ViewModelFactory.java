package com.example.kosovo.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosovo.repository.PostRepository;
import com.example.kosovo.repository.UserRepository;
import com.example.kosovo.data.remote.ApiClient;
import com.example.kosovo.data.remote.ApiService;
import com.example.kosovo.ui.auth.LoginViewModel;
import com.example.kosovo.ui.homepage.MainViewModel;
import com.example.kosovo.ui.postupload.PostUploadViewModel;
import com.example.kosovo.ui.profile.ProfileViewModel;
import com.example.kosovo.ui.search.SearchViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ViewModelFactory() {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        this.userRepository = UserRepository.getRepository(apiService);
        this.postRepository = PostRepository.getRepository(apiService);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(userRepository);
        }else if (modelClass.isAssignableFrom(ProfileViewModel.class)){
            return (T) new ProfileViewModel(userRepository,postRepository);
        }else if (modelClass.isAssignableFrom(PostUploadViewModel.class)){
            return (T) new PostUploadViewModel(postRepository);
        }else if (modelClass.isAssignableFrom(SearchViewModel.class)){
            return (T) new SearchViewModel(userRepository);
        }else if (modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(userRepository,postRepository);
        }
        throw new IllegalArgumentException("View Model not Found !!");
    }
}
