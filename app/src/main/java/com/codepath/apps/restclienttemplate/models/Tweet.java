package com.codepath.apps.restclienttemplate.models;

import android.content.Entity;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public String imageUrl;
    public String relativeTime;
    public String id;
    public String tweetID;
    public User user;
    public boolean isFavorited;
    public boolean isRetweeted;
    public int favoriteCount;
    public int retweetedCount;


    // Empty constructor needed by the Parcler Library
    public Tweet() {}

    // turn it into a java tweet object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        if(jsonObject.has("retweeted_status")) {
            return null;
        }

        // For entity
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.relativeTime = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.id = jsonObject.getString("id");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetedCount = jsonObject.getInt("retweet_count");
        JSONObject entities = jsonObject.getJSONObject("entities");

        // Initialize the imageUrl
        tweet.imageUrl = "";
        if (entities.has("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            if (mediaArray.length() != 0) {
                JSONObject mediaObject = mediaArray.getJSONObject(0);
                if (mediaObject.getString("type").equals("photo")) {
                    tweet.imageUrl = mediaObject.getString("media_url_https");
                }
            }
        }

        return tweet;
    }

    // return a list of tweet object from json array
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Tweet newTweet = fromJson(jsonArray.getJSONObject(i));

            // Ensures that we are not adding a null tweet to the home feed
            if(newTweet != null) {
                tweets.add(newTweet);
            }
        }
        return tweets;
    }

    // Code for parsing a relative twitter date
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}
