package com.asitrack.twilio_integration;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.ChatClient;


/**
 * Created by AnwarSamir on 2019-08-29.
 */

public class TokenManger {
    Context context;
    public static final String TAG="TokenManger->";
    public TokenManger(Context context) {
        this.context = context;
    }
    /**
     * To Create Chanel
     * 1- Create Token from the Twilio Server
     * - this step will need an user as an identity
     *
     *
     * @param identity 2- after creating chanel new chat client will created to handel All Chanel
     * @param mChatClientCallback this call back will be used to get the callback to get -ChatClient- inorder to use it all over the app
     */
    public void retrieveAccessTokenfromServer(String SERVER_TOKEN_URL, String identity, final CallbackListener<ChatClient> mChatClientCallback) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String tokenURL = SERVER_TOKEN_URL + "?device=" + deviceId + "&identity=" + identity;

        Log.e("TokenUrl-->",tokenURL);
        Ion.with(context)
                .load(tokenURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.e("Respooo->", result.toString());
                        }catch (NullPointerException e1)
                        {

                        }


                        if (e == null) {
                            // get the token from server json
                            String accessToken = result.get("token").getAsString();
                            // tvCreatingChanelStatus.append(" \n Token Created ... Done ");
                            ChatClient.Properties.Builder builder = new ChatClient.Properties.Builder();
                            ChatClient.Properties props = builder.createProperties();
                            ChatClient.create(context, accessToken, props, mChatClientCallback);

                        } else {
                            Log.e(TAG, e.getMessage(), e);
//                            Toast.makeText(context,
//                                    R.string.error_retrieving_access_token, Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    }
                });
    }


}