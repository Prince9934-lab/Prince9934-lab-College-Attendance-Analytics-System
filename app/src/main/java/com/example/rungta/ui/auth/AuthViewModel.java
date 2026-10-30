package com.example.rungta.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rungta.data.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<AuthRepository.AuthResult> authResultLiveData;

    public AuthViewModel() {
        authRepository = new AuthRepository();
        authResultLiveData = new MutableLiveData<>();
    }

    public LiveData<AuthRepository.AuthResult> getAuthResult() {
        return authResultLiveData;
    }

    public void login(String email, String password) {
        authRepository.login(email, password, authResultLiveData);
    }

    public void register(String email, String password, String name, String role) {
        authRepository.register(email, password, name, role, authResultLiveData);
    }
}
