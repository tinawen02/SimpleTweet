package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    Tweet tweet;
    Context context;

    // the view objects
    ImageView ivProfileImage;
    ImageView ivTweetImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvRelativeTime;
    TextView tvFavoriteCount;
    ImageButton ibFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);


        // resolve the view objects
        setContentView(R.layout.activity_tweet_detail);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivTweetImage = (ImageView) findViewById(R.id.ivTweetImage);
        tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);
        ibFavorite = (ImageButton) findViewById(R.id.ibFavorite);
        tvFavoriteCount = (TextView) findViewById(R.id.tvFavoriteCount);


        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // Set the body, username and favorite count
        tvBody.setText(tweet.body);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

        // Sets the profile image
        Glide.with(this).load(tweet.user.profileImageUrl)
                .transform(new RoundedCorners(300))
                .into(ivProfileImage);

        Glide.with(this).load(tweet.imageUrl)
                .transform(new RoundedCorners(30))
                .into(ivTweetImage);

    }
}