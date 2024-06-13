package com.example.kosovo.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.kosovo.R;
import com.example.kosovo.data.remote.ApiClient;
import com.example.kosovo.ui.fullImage.FullImageActivity;
import com.example.kosovo.helper.GeneralResponse;
import com.example.kosovo.entity.post.PostResponse;
import com.example.kosovo.entity.post.PostsItem;
import com.example.kosovo.entity.profile.ProfileResponse;
import com.example.kosovo.entity.reaction.ReactResponse;
import com.example.kosovo.utils.Util;
import com.example.kosovo.utils.ViewModelFactory;
import com.example.kosovo.adapter.PostAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, SwipeRefreshLayout.OnRefreshListener, PostAdapter.IUpdateUserReaction,PostAdapter.IDeletePost {

    private String uid = "", profileUrl = "", coverUrl = "";
    private int current_state = 0;
    private List<PostsItem> postsItemList;
    private int limit = 100;
    private int offset = 0;
    private Boolean isFirstLoading = true;

    private Button profileOptionBtn;
    private ImageView profileImg, coverImg;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView postCounter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private ProfileViewModel profileViewModel;
    private PostAdapter postAdapter;

    private Boolean isCoverImage = false;
    private ProgressDialog progressDialog;
    private Button shareProfile;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileViewModel = new ViewModelProvider(this, new ViewModelFactory()).get(ProfileViewModel.class);
        profileOptionBtn = findViewById(R.id.profile_action_btn);
        profileImg = findViewById(R.id.profile_image);
        recyclerView = findViewById(R.id.recyclerView_profile);
        progressBar = findViewById(R.id.progressBar);
        coverImg = findViewById(R.id.profile_cover);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        postCounter = findViewById(R.id.postCounter);
        shareProfile = findViewById(R.id.shareProfileB);

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        postsItemList = new ArrayList<>();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isLastItemReached()) {
                    offset += limit;
                    getProfilePosts();
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Please wait");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.super.onBackPressed();
            }
        });

        shareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileName = (String) collapsingToolbarLayout.getTitle();
                String postTitle = "Check out this amazing profile ";

                String shareBody = "Check out this amazing profile: " + profileName + " on Kosovita social app:\n\n";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, postTitle);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                ProfileActivity.this.startActivity(Intent.createChooser(shareIntent, "Share profile using..."));
            }
        });

        uid = getIntent().getStringExtra("uid");

        if (uid.equals(FirebaseAuth.getInstance().getUid())) {
            current_state = 5;
            profileOptionBtn.setText("Edit profile");
        } else {
            profileOptionBtn.setText("loading..");
            profileOptionBtn.setEnabled(false);
        }
        fetchProfileInfo();
    }
    private void fetchProfileInfo() {
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getUid());
        if (current_state == 5) {
            params.put("current_state", current_state + "");
        } else {
            params.put("profileId", uid);
        }
        profileViewModel.fetchProfileInfo(params).observe(this, new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                progressDialog.hide();
                if (profileResponse.getStatus() == 200) {
                    collapsingToolbarLayout.setTitle(profileResponse.getProfile().getName());
                    profileUrl = profileResponse.getProfile().getProfileUrl();
                    coverUrl = profileResponse.getProfile().getCoverUrl();
                    current_state = Integer.parseInt(profileResponse.getProfile().getState());
                    Log.w("TAG", profileUrl);
                    if (!profileUrl.isEmpty()) {
                        Uri profileUri = Uri.parse(profileUrl);

                        if (profileUri.getAuthority() == null) {
                            profileUrl = ApiClient.BASE_URL + profileUrl;
                        }
                        Glide.with(ProfileActivity.this).load(profileUrl).into(profileImg);
                    } else {
                        profileUrl = R.drawable.default_profile_placeholder + "";
                    }
                    if (!coverUrl.isEmpty()) {
                        Uri coverUri = Uri.parse(coverUrl);

                        if (coverUri.getAuthority() == null) {
                            coverUrl = ApiClient.BASE_URL + coverUrl;
                        }
                        Glide.with(ProfileActivity.this).load(coverUrl).into(coverImg);
                    } else {
                        coverUrl = R.drawable.cover_picture_placeholder + "";
                    }
                    if (current_state == 0) {
                        profileOptionBtn.setText("Loading..");
                        profileOptionBtn.setEnabled(false);
                        return;
                    } else if (current_state == 1) {
                        getProfilePosts();
                        profileOptionBtn.setText("You are friends");
                    } else if (current_state == 2) {
                        profileOptionBtn.setText("Cancel Request");
                    } else if (current_state == 3) {
                        profileOptionBtn.setText("Accept Request");
                    } else if (current_state == 4) {
                        profileOptionBtn.setText("Send Request");
                    } else if (current_state == 5) {
                        profileOptionBtn.setText("Edit Profile");
                    }
                    profileOptionBtn.setEnabled(true);
                    loadProfileOptionButton();
                } else {
                    Toast.makeText(ProfileActivity.this, profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isLastItemReached() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findLastCompletelyVisibleItemPosition();
        int numberOfItems = postAdapter.getItemCount();
        return (position >= numberOfItems - 1);
    }
    private void getProfilePosts() {
        if (!isFirstLoading) return;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("limit", limit + "");
        params.put("offset", offset + "");
        params.put("current_state", current_state + "");
        progressBar.setVisibility(View.VISIBLE);
        profileViewModel.getProfilePosts(params).observe(this, new Observer<PostResponse>() {
            @Override
            public void onChanged(PostResponse postResponse) {
                progressBar.setVisibility(View.GONE);
                if (postResponse.getStatus() == 200) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        postsItemList.clear();
                        postAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    postsItemList.addAll(postResponse.getPosts());
                    if (isFirstLoading) {
                        postAdapter = new PostAdapter(ProfileActivity.this, postsItemList);
                        recyclerView.setAdapter(postAdapter);
                    } else {
                        postAdapter.notifyItemRangeChanged(postsItemList.size(), postResponse.getPosts().size());
                    }
                    if (postResponse.getPosts().size() == 0) {
                        offset -= limit;
                    }
                    isFirstLoading = false;
                    updatePostCount();
                } else {
                    if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ProfileActivity.this, postResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updatePostCount() {
        int postCount = postsItemList.size();
        postCounter.setText( " ▤ ▤ "+ postCount+" Posts");

    }

    private void loadProfileOptionButton() {
        profileOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileOptionBtn.setEnabled(false);

                if (current_state == 5) {
                    CharSequence[] options = new CharSequence[]{"Change Cover Image", "Change Profile Image", "View Cover Image", "View Profile Image"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                isCoverImage = true;
                                selectImage();
                            } else if (position == 1) {
                                isCoverImage = false;
                                selectImage();
                            } else if (position == 2) {
                                viewFullImage(coverImg, coverUrl);
                            } else if (position == 3) {
                                viewFullImage(profileImg, profileUrl);
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                } else if (current_state == 4) {
                    CharSequence[] options = new CharSequence[]{"Send friend request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                performAction();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                } else if (current_state == 3) {
                    CharSequence[] options = new CharSequence[]{"Accept request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                performAction();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                } else if (current_state == 2) {
                    CharSequence[] options = new CharSequence[]{"Cancel request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                performAction();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                } else if (current_state == 1) {
                    CharSequence[] options = new CharSequence[]{"Remove"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                performAction();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                }
            }
        });
    }

    private void performAction() {
        progressDialog.show();
        profileViewModel.performAction(new PerformAction(current_state + "",
                FirebaseAuth.getInstance().getUid(), uid)).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                progressDialog.hide();
                Toast.makeText(ProfileActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if (generalResponse.getStatus() == 200) {
                    profileOptionBtn.setEnabled(true);

                    if (current_state == 4) {
                        current_state = 2;
                        profileOptionBtn.setText("Cancel Request");
                    } else if (current_state == 3) {
                        current_state = 1;
                        profileOptionBtn.setText("You are Friends");
                    } else if (current_state == 2) {
                        current_state = 4;
                        profileOptionBtn.setText("Send Request");
                    } else if (current_state == 1) {
                        current_state = 4;
                        profileOptionBtn.setText("Send Request");
                    }
                } else {
                    profileOptionBtn.setEnabled(false);
                    profileOptionBtn.setText("Error");
                }
            }
        });

    }

    private void viewFullImage(ImageView imageView, String imageUrl) {
        Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
        intent.putExtra("imageUrl", imageUrl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(imageView, imageUrl);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, pairs);
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void selectImage() {
        ImagePicker.create(this).single().folderMode(true).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);

            try {
                File compressedImageFIle = new Compressor(this).setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));

                uploadImage(compressedImageFIle);
            } catch (IOException e) {
                Toast.makeText(this, "Image Picker Filed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(File compressedImageFIle) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", FirebaseAuth.getInstance().getUid() + "");
        builder.addFormDataPart("isCoverImage", isCoverImage + "");
        builder.addFormDataPart("file", compressedImageFIle.getName(), RequestBody.create(compressedImageFIle, MediaType.parse("multipart/form-data")));
        progressDialog.show();
        profileViewModel.uploadPost(builder.build(), true).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                progressDialog.hide();
                Toast.makeText(ProfileActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if (generalResponse.getStatus() == 200) {
                    if (isCoverImage) {
                        Glide.with(ProfileActivity.this).load(ApiClient.BASE_URL + generalResponse.getExtra()).into(coverImg);
                    } else {
                        Glide.with(ProfileActivity.this).load(ApiClient.BASE_URL + generalResponse.getExtra()).into(profileImg);
                    }
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        profileOptionBtn.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        offset = 0;
        postsItemList.clear();
        isFirstLoading = true;
    }

    @Override
    public void onRefresh() {
        offset = 0;
        isFirstLoading = true;
        getProfilePosts();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getProfilePosts();
    }
    @Override
    public void updateUserReaction(String uid, int postId, String postOwnerId, String previousReactionType, String newReactionType, int adapterPosition) {
        profileViewModel.performReaction(new Util.PerformReaction(
                uid,
                postId + "",
                postOwnerId,
                previousReactionType,
                newReactionType
        )).observe(this, new Observer<ReactResponse>() {
            @Override
            public void onChanged(ReactResponse reactResponse) {
                if (reactResponse.getStatus() == 200){
                    postAdapter.updatePostAfterReaction(adapterPosition,reactResponse.getReaction());
                }else {
                    Toast.makeText(ProfileActivity.this, reactResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void deletePost(String postId, String postUserId, int adapterPosition) {
        // Implementacija metode za brisanje posta
        Map<String, String> params = new HashMap<>();
        params.put("postId", postId);
        params.put("postUserId", postUserId);

        profileViewModel.deletePost(params).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                if (generalResponse.getStatus() == 200){

                    // Ako je post uspešno obrisan, ažurirajte listu postova i obavestite adapter
                    postsItemList.remove(adapterPosition);
                    postAdapter.notifyItemRemoved(adapterPosition);
                    updatePostCount();
                    Toast.makeText(ProfileActivity.this, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProfileActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class PerformAction {
        String operationType;
        String uid;
        String profileId;

        public PerformAction(String operationType, String uid, String profileId) {
            this.operationType = operationType;
            this.uid = uid;
            this.profileId = profileId;
        }
    }
}