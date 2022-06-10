package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    // Pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout for the tweet
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
       return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);

        //Bind the tweet with view holder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        ImageView ivTweetImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvRelativeTime;
        TextView tvFavoriteCount;
        ImageButton ibFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

            // Sets the time stamp
            tvRelativeTime.setText(tweet.relativeTime);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

            if (!((tweet.imageUrl).equals(""))) {
                Glide.with(context).load(tweet.imageUrl).into(ivTweetImage);
                ivTweetImage.setVisibility(View.VISIBLE);
            } else {
                ivTweetImage.setVisibility(View.GONE);
            }

            // Allows the user to click on the favorite button
            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // If tweet is not already favorited
                    if(!tweet.isFavorited) {
                        // Tell Twitter I want to favorite this
                        TwitterApp.getRestClient(context).favorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                        // Change the drawable to btn_star_big_on
                        tweet.isFavorited = true;
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_on);
                        ibFavorite.setImageDrawable(newImage);

                        // Increment the text inside tvFavoriteText
                        tweet.favoriteCount ++;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    } else {
                        // Else if tweet is already favorited
                        // Tell Twitter I want to unfavorite this


                        // Change the drawable back to btn_star_big_off
                        tweet.isFavorited = false;
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_off);
                        ibFavorite.setImageDrawable(newImage);

                        // Decrement the text inside tvFavoriteCount
                        tweet.favoriteCount --;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    }
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }



}
