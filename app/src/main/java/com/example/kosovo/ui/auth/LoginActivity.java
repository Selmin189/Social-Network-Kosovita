package com.example.kosovo.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.kosovo.R;
import com.example.kosovo.ui.homepage.MainActivity;
import com.example.kosovo.entity.auth.AuthResponse;
import com.example.kosovo.utils.ViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 50;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    private ProgressDialog progressDialog;
    private SignInButton signInButton;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait..sd");
        signInButton = findViewById(R.id.btn_signin);
        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(LoginViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndSignIn();
            }
        });
    }
    private void signOutAndSignIn() {
        // Sign out from Firebase Authentication
        mAuth.signOut();

        // Sign out from Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Now start the sign-in process
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent singInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }


//    private void firebaseAuthWithGoogle(String idToken) {
//        progressDialog.show();
//        signInButton.setVisibility(View.GONE);
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "signInWithCredential:success");// Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
//                        }
//                    }
//                });
//    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");// Sign in success, update UI with the signed-in user's information
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    viewModel.login(new UserInfo(
                                                    user.getUid(),
                                                    user.getDisplayName(),
                                                    user.getEmail(),
                                                    user.getPhotoUrl().toString(),
                                                    "",
                                                    instanceIdResult.getToken()
                                            ))
                                            .observe(LoginActivity.this, new Observer<AuthResponse>() {
                                                @Override
                                                public void onChanged(AuthResponse authResponse) {
                                                    Toast.makeText(LoginActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                                    if (authResponse.getAuth()!=null){
                                                        updateUI(user);
                                                    }else {
                                                        FirebaseAuth.getInstance().signOut();
                                                        updateUI(null);
                                                    }
                                                }
                                            });
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }



    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            String profileUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "";
            String coverUrl = ""; // Ovdje možete postaviti URL slike korisničkog naslovnog fotografije ako ga imate

            currentUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String userToken = task.getResult().getToken();

                                UserInfo userInfo = new UserInfo(uid, name, email, profileUrl, coverUrl, userToken);

                                // Pridruživanje svih informacija u jedan string
                                StringBuilder userInfoString = new StringBuilder();
                                userInfoString.append("UID: ").append(uid).append("\n");
                                userInfoString.append("Name: ").append(name).append("\n");
                                userInfoString.append("Email: ").append(email).append("\n");
                                userInfoString.append("Profile URL: ").append(profileUrl).append("\n");
                                userInfoString.append("Cover URL: ").append(coverUrl).append("\n");
                                userInfoString.append("User Token: ").append(userToken);

                                // Ispisivanje svih informacija u logcat
                                Log.w("TAG", userInfoString.toString());

                                // Preusmjeravanje korisnika na sljedeći zaslon
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }
    }


    public static class UserInfo {
        String uid, name, email, profileUrl, coverUrl, userToken;

        public UserInfo(String uid, String name, String email, String profileUrl, String coverUrl, String userToken) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.profileUrl = profileUrl;
            this.coverUrl = coverUrl;
            this.userToken = userToken;
        }
    }

}