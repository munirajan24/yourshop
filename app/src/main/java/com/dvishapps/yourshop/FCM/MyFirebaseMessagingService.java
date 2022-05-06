package com.dvishapps.yourshop.FCM;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.CallActivity;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    boolean foreground = false;
    private static final Type transaction_type = Transaction.class;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onNewToken(@NonNull String s) {
        //Get updated token
        String refreshedToken = s;
        Log.e(TAG, "Refreshed Token: " + refreshedToken);
        // Saving reg id to shared preferences

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        final Intent registrationComplete = new Intent("tokenReceiver");
        registrationComplete.putExtra("token", refreshedToken);
        broadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        super.onNewToken(s);
    }

    @SuppressLint("WrongThread")
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        showCallMessage(remoteMessage);
//        if (remoteMessage != null) {
//            Intent i = new Intent(this, CallActivity.class);
//            if (remoteMessage.getNotification() != null) {
//                i.putExtra("trans_id",""+remoteMessage.getNotification().getBody());
//            }
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody(), "100");
//        }
        // Check if message contains a notification payload.

//        Check if message contains a data payload.
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//                if (remoteMessage.getData().get("trans_status_id").equalsIgnoreCase("1")) {
                String transaction = remoteMessage.getData().get("message");
                try {
                    foreground = new ForegroundCheckTask().execute(this).get();
                    showCallMessage(remoteMessage, foreground);
//                    if (!foreground) {
//                        //TODO : TEST
//
//                        if (remoteMessage.getData().get("message").toLowerCase().contains("{".toLowerCase())) {
////                            sendNotificationForBackground(transaction, "New Order Received. Tap to open", "100");
//                         } else {
//                            sendNotification(remoteMessage.getData().get("message"), "100");
//                        }
////                            Intent i = new Intent(this, CallActivity.class);
////                            i.putExtra("isforeground", foreground);
////                            i.putExtra("transaction", "" + transaction);
////                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
////                            pendingIntent.cancel();
////                            startActivity(i);
//                    } else {
//                        showCallMessage(remoteMessage, foreground);
//
////                            Intent i = new Intent(this, CallActivity.class);
////                            i.putExtra("isforeground", foreground);
////                            i.putExtra("transaction", "" + transaction);
////                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
////                            pendingIntent.cancel();
////                            startActivity(i);
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String messageBody, String requestNotification) {

        Intent intent;

        intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "4655";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence channelName = "Your Shop";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo);

            builder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Your Shop")
                    .setContentText(messageBody)
                    .setTicker("")
                    .setLargeIcon(bm)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
                    .setContentIntent(pendingIntent)
                    .setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
            Notification notification = builder.getNotification();
            notificationManager.notify(R.drawable.app_icon, notification);

        } else {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo);

            builder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Your Shop")
                    .setContentText(messageBody)
                    .setTicker("")
                    .setLargeIcon(bm)
                    .setColor(Color.parseColor("#BCBCBC"))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)*/
                    .setContentIntent(pendingIntent);
            //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    /*.addAction (R.drawable.ic_stat_snooze,
                            getString(R.string.snooze), piSnooze);*/
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

            Notification notification = builder.getNotification();
            notificationManager.notify(R.drawable.app_icon, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotificationForBackground(String transaction, String messageBody, String requestNotification) {

        Intent intent;

        intent = new Intent(this, CallActivity.class);
        intent.putExtra("transaction", "" + transaction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "4655";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            CharSequence channelName = "Your Shop";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(soundUri, attributes);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo);

            builder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Your Shop")
                    .setContentText(messageBody)
                    .setTicker("")
                    .setLargeIcon(bm)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
                    .setContentIntent(pendingIntent)
                    .setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
            Notification notification = builder.getNotification();
            notificationManager.notify(R.drawable.app_icon, notification);

        } else {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo);

            builder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Your Shop")
                    .setContentText(messageBody)
                    .setTicker("")
                    .setLargeIcon(bm)
                    .setColor(Color.parseColor("#BCBCBC"))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)*/
                    .setContentIntent(pendingIntent);
            //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    /*.addAction (R.drawable.ic_stat_snooze,
                            getString(R.string.snooze), piSnooze);*/
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

            Notification notification = builder.getNotification();
            notificationManager.notify(R.drawable.app_icon, notification);
        }
    }

    public class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void showCallMessage(RemoteMessage remoteMessage, boolean foreground) {
        try {
            if (remoteMessage != null) {
                if (remoteMessage.getData() != null) {
                    Console.logDebug(remoteMessage.getData().get("message"));
                    String transaction = remoteMessage.getData().get("message");
                    Transaction transaction1;
                    if (remoteMessage.getData().get("message").contains("{")) {

                        if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {

                            transaction1 = JsonUtils.fromJsonString(transaction, Transaction.class);
                            if (transaction1.getTrans_status_id().equals("7")) {            //Cancel order - 7
                                broadcaster = LocalBroadcastManager.getInstance(this);
                                Intent intent = new Intent("MyData");
                                intent.putExtra("textResponse", "cancelled");
                                intent.putExtra("transactionResponse", transaction);
                                intent.putExtra("transactionId", transaction1.getId());
                                broadcaster.sendBroadcast(intent);
                                //                            sendNotification("Your order cancelled by the user", "100");
                            } else if (transaction1.getTrans_status_id().equals("4")) {

                                broadcaster = LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
                                Intent intent = new Intent("MyData");
                                intent.putExtra("removeCancel", "true");
                                intent.putExtra("textResponse", "Your order accepted by the hotel");
                                intent.putExtra("transactionResponse", transaction);
                                intent.putExtra("transactionId", transaction1.getId());
                                broadcaster.sendBroadcast(intent);
                                sendNotification("Your order accepted by the hotel", "100");

                                try {
                                    if (SessionData.getInstance().getCheckoutResult() != null) {
                                        if (SessionData.getInstance().getCheckoutResult().transactionHeaderUpload.getId().equals(transaction1.getId())) {
                                            SessionData.getInstance().getCheckoutResult().order_cancel_btn.setVisibility(View.GONE);
                                            SessionData.getInstance().getCheckoutResult().img_failure.setVisibility(View.GONE);
                                            SessionData.getInstance().getCheckoutResult().img_success.setVisibility(View.VISIBLE);
                                            SessionData.getInstance().getCheckoutResult().success_text.setText("Order accepted by the hotel");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (transaction1.getTrans_status_id().equals("5")) {

                                broadcaster = LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this);
                                Intent intent = new Intent("MyData");
                                intent.putExtra("orderRejected", "true");
                                intent.putExtra("removeCancel", "true");
                                intent.putExtra("textResponse", "rejected");
                                intent.putExtra("transactionResponse", transaction);
                                intent.putExtra("transactionId", transaction1.getId());
                                broadcaster.sendBroadcast(intent);
                                sendNotification("Your order rejected by the hotel", "100");

                                try {
                                    if (SessionData.getInstance().getCheckoutResult() != null) {
                                        if (SessionData.getInstance().getCheckoutResult().transactionHeaderUpload.getId().equals(transaction1.getId())) {
                                            SessionData.getInstance().getCheckoutResult().order_cancel_btn.setVisibility(View.GONE);
//                                            SessionData.getInstance().getCheckoutResult().img_failure.setVisibility(View.GONE);
//                                            SessionData.getInstance().getCheckoutResult().img_success.setVisibility(View.VISIBLE);
                                            SessionData.getInstance().getCheckoutResult().success_text.setText("Order rejected by the hotel");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (SessionData.getInstance().getCallActivity() == null) {
                                    Constants.play_ringtone(getApplicationContext());
                                    Intent i = new Intent(this, CallActivity.class);
                                    if (transaction != null) {
                                        i.putExtra("transaction", "" + transaction);
                                    }
                                    //                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (foreground) {
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
                                        pendingIntent.cancel();
                                        startActivity(i);
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            NotificationChannel n_channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                                            n_channel.setSound(null, null);
                                            n_channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(n_channel);
                                        }
                                        Transaction trans = JsonUtils.fromJsonString(transaction, Transaction.class);
                                        PendingIntent p_intent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_call_mini)
                                                .setAutoCancel(false)
                                                .setContentTitle("New Order Amount : Rs." + trans.getTotal_item_amount())
                                                .setContentText(trans.getContact_name() + "'s Order")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                                .setFullScreenIntent(p_intent, true);
                                        Notification notification = notificationBuilder.build();
                                        notification.flags = Notification.FLAG_INSISTENT;
                                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                                        notificationManagerCompat.notify(Constants.NOTIFICATION_ID, notification);
                                    }
                                } else {
                                    //                                Console.toast(" Call activity exist");
                                    broadcaster = LocalBroadcastManager.getInstance(this);
                                    Intent intent = new Intent("MyData");
                                    intent.putExtra("textResponse", "more order");
                                    intent.putExtra("transactionResponse", transaction);
                                    intent.putExtra("transactionId", transaction1.getId());
                                    broadcaster.sendBroadcast(intent);
                                    //                                sendNotification("One more order Arrived", "100");
                                }
                            }
                        } else {
                            try {
                                transaction1 = JsonUtils.fromJsonString(transaction, Transaction.class);
                                if (transaction1.getTrans_status_id().equals("4")) {
                                    broadcaster = LocalBroadcastManager.getInstance(this);
                                    Intent intent = new Intent("MyData");
                                    intent.putExtra("removeCancel", "true");
                                    intent.putExtra("textResponse", "Your order accepted by the hotel");
                                    intent.putExtra("transactionResponse", transaction);
                                    intent.putExtra("transactionId", transaction1.getId());
                                    broadcaster.sendBroadcast(intent);
                                    sendNotification("Your order accepted by the hotel", "100");
                                } else if (transaction1.getTrans_status_id().equals("5")) {
                                    broadcaster = LocalBroadcastManager.getInstance(this);
                                    Intent intent = new Intent("MyData");
                                    intent.putExtra("orderRejected", "true");
                                    intent.putExtra("removeCancel", "true");
                                    intent.putExtra("textResponse", "rejected");
                                    intent.putExtra("transactionResponse", transaction);
                                    intent.putExtra("transactionId", transaction1.getId());
                                    broadcaster.sendBroadcast(intent);
                                    sendNotification("Your order rejected by the hotel", "100");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        //                    startActivity(i);
                    } else if (remoteMessage.getData().get("message").toLowerCase().contains("Your Order Placed Successfully".toLowerCase())) {
                        sendNotification(remoteMessage.getData().get("message"), "100");

                        broadcaster = LocalBroadcastManager.getInstance(this);
                        Intent intent = new Intent("MyData");
                        intent.putExtra("textResponse", remoteMessage.getData().get("message"));
                        broadcaster.sendBroadcast(intent);

                    } else if (remoteMessage.getData().get("message").toLowerCase().contains("rejected".toLowerCase())) {
                        sendNotification(remoteMessage.getData().get("message"), "100");

                        broadcaster = LocalBroadcastManager.getInstance(this);
                        Intent intent = new Intent("MyData");
                        intent.putExtra("textResponse", "rejected");
                        intent.putExtra("orderRejected", "true");
                        broadcaster.sendBroadcast(intent);


                    } else {


                        broadcaster = LocalBroadcastManager.getInstance(this);
                        Intent intent = new Intent("MyData");
                        intent.putExtra("textResponse", remoteMessage.getData().get("message"));
                        //                    intent.putExtra("transactionResponse", transaction);
                        //                    intent.putExtra("transactionId", transaction1.getId());
                        broadcaster.sendBroadcast(intent);

                        sendNotification(remoteMessage.getData().get("message"), "100");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}