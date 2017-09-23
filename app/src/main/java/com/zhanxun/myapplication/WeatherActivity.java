package com.zhanxun.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.zhanxun.myapplication.bean.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG ="MyWeather" ;
    private EditText m_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        m_editText=(EditText)findViewById(R.id.btn_edit);
    }


    public void check(View v){
        //OkHttpClient mOkHttpClient = new OkHttpClient();
        String city=m_editText.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this,"请输入城市名",Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://free-api.heweather.com/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service=retrofit.create(ApiService.class);

        MultipartBuilder multipartBuilder=new MultipartBuilder();
        RequestBody requestBody=multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("city",city)
                .addFormDataPart("key","96217f3f638b4d61ba3581bb184d889b")
                .build();
        //Call<ResponseBody> call=service.check(requestBody);
        Call<WeatherModel> call=service.postRequest(city,"96217f3f638b4d61ba3581bb184d889b");
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                try {
                    loadData(response.body().getHeWeather5());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                t.printStackTrace();
                Log.i(TAG, "onFailure(),," + t.getMessage());
            }
        });


    }

    private void loadData(List<WeatherModel.HeWeather5Bean> heWeather5) {
        Log.i(TAG, "loadData,," + heWeather5.toString());

    }


}
