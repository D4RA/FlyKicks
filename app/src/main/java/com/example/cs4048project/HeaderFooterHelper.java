package com.example.cs4048project;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs4048project.activity.CustomProfileActivity;
import com.example.cs4048project.activity.HomeActivity;
import com.example.cs4048project.activity.ShopActivity;
import com.example.cs4048project.activity.MessagingActivity;

public class HeaderFooterHelper {
    public static void setupHeaderButtons(Activity activity) {
        ImageView profileIcon = activity.findViewById(R.id.profile_icon);
        ImageView shopIcon = activity.findViewById(R.id.shop_icon);
        ImageView homeIcon = activity.findViewById(R.id.home_icon);
        ImageView messageIcon = activity.findViewById(R.id.messaging_icon);
        TextView title = activity.findViewById(R.id.text_title);
        title.setText(activity.getTitle());

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(activity, CustomProfileActivity.class);
            }
        });

        shopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(activity, ShopActivity.class);
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(activity, HomeActivity.class);
            }
        });

        messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(activity, MessagingActivity.class);
            }
        });
    }

    private static void launchActivity(Activity activity, Class<? extends Activity> targetActivity) {
        if (targetActivity.getName().contains(activity.getTitle())) {
            return;
        }
        Intent intent = new Intent(activity, targetActivity);
        activity.startActivity(intent);
        activity.finish();
    }
}
