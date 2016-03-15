package com.example.kriti.youtubeseacrh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

//import java.security.Provider;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;


/**
 * Created by sandeep on 12/3/16.
 */
public class PlayerActivity  extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;
    private YouTubePlayer mPlayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_player);
        Log.d("kriti_player", "error");
        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, this);
    }

    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean restored) {
        if (!restored) {
            player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            mPlayer =player;
        }
    }

    @Override
    public void finish() {
        if(mPlayer.isPlaying() && mPlayer.getCurrentTimeMillis()/1000>=30) {
            // int t = (mPlayer.getCurrentTimeMillis()/1000);
            Log.d("duration", String.valueOf(mPlayer.getCurrentTimeMillis() / 1000));
            Log.d("kriti_play", "after 30 secs");
            String url = "http://requestb.in/rnx5w6rn";
            new JSONTask().execute(url);
            //return;
        }
        super.finish();
    }

        public class JSONTask extends AsyncTask<String, String, String> {

            //String url="http://requestb.in/rnx5w6rn";
            @Override
            protected String doInBackground(String... params) {

                try {
                    URL url = new URL(params[0]);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    JSONObject cred = new JSONObject();
                    cred.put("FacebookId", Constants.id);
                    cred.put("Name", Constants.name);
                    cred.put("Location", Constants.location);
                    cred.put("Email", Constants.email);
                    cred.put("VideoId", getIntent().getStringExtra("VIDEO_ID"));

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(cred.toString());
                    wr.flush();
                    wr.close();

                    Log.d("kriti_play", "request_sent");

                    StringBuilder sb = new StringBuilder();
                    int HttpResult = conn.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        br.close();

                        System.out.println("post" + sb.toString());

                    } else {
                        System.out.println(conn.getResponseMessage());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }

}