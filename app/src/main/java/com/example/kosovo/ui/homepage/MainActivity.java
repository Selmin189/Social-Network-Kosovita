package com.example.kosovo.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kosovo.R;
import com.example.kosovo.ui.auth.LoginActivity;
import com.example.kosovo.ui.homepage.friends.FriendFragment;
import com.example.kosovo.ui.homepage.friends.FriendRequestAdapter;
import com.example.kosovo.ui.homepage.newsFeed.NewsFeedFragment;
import com.example.kosovo.ui.postupload.PostUploadActivity;
import com.example.kosovo.ui.profile.ProfileActivity;
import com.example.kosovo.ui.search.SearchActivity;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.friend.FriendResponse;
import com.example.kosovo.utils.ViewModelFactory;
import com.example.kosovo.adapter.PostAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements FriendRequestAdapter.IPerformAction, PostAdapter.IUpdateUserReaction ,PostAdapter.IDeletePost{

    private BottomNavigationView bottomNavigationView;
    private ImageView topNav;
    private FriendFragment friendFragment;
    private NewsFeedFragment newsFeedFragment;
    private ImageView searchIcon;
    private ProgressBar progressBar;
    private PostAdapter postAdapter;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchIcon = findViewById(R.id.toolbar_search);
        progressBar = findViewById(R.id.progressbar);
        topNav = findViewById(R.id.top_nav);

        topNav.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, topNav);
            popupMenu.inflate(R.menu.menu_top_nav);
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_signOut){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                return true;
            });
            popupMenu.show();
        });
        mainViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(MainViewModel.class);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        bottomNavigationView = findViewById(R.id.navigation);
        friendFragment = new FriendFragment();
        newsFeedFragment = new NewsFeedFragment();
        setFragment(newsFeedFragment);
        setBottomNavigationView();
    }

    private void setBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.newsFeedFragment) {
                    item.setIcon(R.drawable.ic_home_fill);
                    setFragment(newsFeedFragment);
                    return true;
                } else if (item.getItemId() == R.id.friendFragment) {
                    bottomNavigationView.getMenu().findItem(R.id.newsFeedFragment).setIcon(R.drawable.ic_home);
                    setFragment(friendFragment);
                    return true;
                } else if (item.getItemId() == R.id.profileActivity) {
                    bottomNavigationView.getMenu().findItem(R.id.newsFeedFragment).setIcon(R.drawable.ic_home);
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("uid", FirebaseAuth.getInstance().getUid()));
                    return false;
                } else if (item.getItemId() == R.id.createPost) {
                    bottomNavigationView.getMenu().findItem(R.id.newsFeedFragment).setIcon(R.drawable.ic_home);
                    startActivity(new Intent(MainActivity.this, PostUploadActivity.class));
                    return false;
                } else {
                    return true;
                }
            }
        });
    }
//    private void setBottomNavigationView(){
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case NEWS_FEED_FRAGMENT:
//                        setFragment(newsFeedFragment);
//                        return true;
//                    case FRIEND_FRAGMENT:
//                        setFragment(friendFragment);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
//                }
//                return true;
//            }
//        });
//    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void performAction(int position, String profileId, int operationType) {
        showProgressBar();
        mainViewModel.performAction(new ProfileActivity.PerformAction(operationType + "",
                FirebaseAuth.getInstance().getUid(), profileId)).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                hideProgressBar();
                Toast.makeText(MainActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if (generalResponse.getStatus() == 200){
                    FriendResponse response = mainViewModel.loadFriends(FirebaseAuth.getInstance().getUid()).getValue();
                    response.getResult().getRequests().remove(position);
                    mainViewModel.loadFriends(FirebaseAuth.getInstance().getUid()).setValue(response);
                }
            }
        });
    }

    @Override
    public void updateUserReaction(String uid, int postId, String postOwnerId, String previousReactionType, String newReactionType, int adapterPosition) {
        newsFeedFragment.updateUserReaction(uid,postId,postOwnerId,previousReactionType,newReactionType,adapterPosition);
    }

    @Override
    public void deletePost(String postId, String postUserId, int adapterPosition) {
        newsFeedFragment.deletePost(postId,postUserId,adapterPosition);
    }
}
