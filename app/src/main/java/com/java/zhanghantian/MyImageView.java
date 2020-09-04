package com.java.zhanghantian;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;



import androidx.appcompat.widget.AppCompatImageView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyImageView extends AppCompatImageView {
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message message)
        {
            Bitmap bitmap = (Bitmap)message.obj;
            MyImageView.this.setImageBitmap(bitmap);
        }
    };
    public MyImageView(Context context)
    {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setImageUrl(final String imgUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imgUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //do nothing
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        });
    }


}
