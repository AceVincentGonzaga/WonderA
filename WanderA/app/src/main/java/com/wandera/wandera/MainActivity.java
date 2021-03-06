package com.wandera.wandera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.wandera.wandera.activity.HomeSlider;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private ConstraintLayout constraintLayout;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    BussinessListRecyclerViewAdapter bussinessListRecyclerViewAdapter;
    RecyclerView businessItemRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout  = (ConstraintLayout) findViewById(R.id.container);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        signIn();
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent i = new Intent(MainActivity.this,HomeSlider.class);

            startActivity(i);
            finish();
        }
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
                firebaseAuthWithGoogle(account);

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                              try {
                                  saveUserProfile(FirebaseAuth.getInstance().getCurrentUser()
                                          .getUid(),FirebaseAuth.getInstance().getCurrentUser()
                                          .getPhotoUrl().toString());
                                  HashMap<String, Object> result = new HashMap<>();
                                  result.put("user", true);
                                  FirebaseDatabase.getInstance()
                                          .getReference().child("users")
                                          .child(mAuth.getUid()).updateChildren(result);
                              }catch (NullPointerException e){
                                  Snackbar.make(constraintLayout,e.toString(),Snackbar.LENGTH_SHORT).show();
                              }

                            }

                        },
                        5000
                );


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void saveUserProfile(String userId,String userImage){

        String userID= mAuth.getCurrentUser().getUid();
        String userImagePath= mAuth.getCurrentUser().getPhotoUrl().toString();
        UserProfileMapModel userProfileMapModel= new UserProfileMapModel(userID,
                userImagePath,mAuth.getCurrentUser().getDisplayName(),
                mAuth.getCurrentUser().getPhoneNumber(),
                mAuth.getCurrentUser().getEmail());
        Map<String,Object> profileValue = userProfileMapModel.toMap();
        Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(userId,profileValue);

        FirebaseDatabase.getInstance().getReference().child("users").updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(MainActivity.this,HomeSlider.class);
                startActivity(i);
                finish();

            }
        });

    }
}
