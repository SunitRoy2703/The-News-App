package com.sunit.thenewsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sunit.thenewsapp.api.ApiClient;
import com.sunit.thenewsapp.api.ApiInterface;
import com.sunit.thenewsapp.models.Article;
import com.sunit.thenewsapp.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    public static final String API_KEY = "8aa13e9088ae49ea91e71b15a94fd185";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsAdapter adapter ;
    private String TAG = MainFragment.class.getSimpleName();
    private TextView topHeadline;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    int pos;
    public MainFragment(int i) {
        this.pos = i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);


        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        topHeadline = v.findViewById(R.id.topheadelines);
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);



        onLoadingSwipeRefresh("");



        errorLayout = v.findViewById(R.id.errorLayout);
        errorImage = v.findViewById(R.id.errorImage);
        errorTitle = v.findViewById(R.id.errorTitle);
        errorMessage = v.findViewById(R.id.errorMessage);
        btnRetry = v.findViewById(R.id.btnRetry);



        return v;


    }





        public void  LoadJson(final String keyword){

            String sources = "";
            if (pos == 0) {
                sources = "vice-news";
            } else if (pos == 1)
                sources = "ary-news";
            else if (pos == 2)
                sources = "bbc-news";
            else if (pos == 3)
                sources = "bbc-sport";
            else if (pos == 4)
                sources = "usa-today";
            else if (pos == 5)
                sources = "cnn";
            else if (pos == 6)
                sources = "fox-news";
            else if (pos == 7)
                sources = "google-news";
            else if (pos == 8)
                sources = "the-verge";
            else if (pos == 9)
                sources = "news24";
            else {
                sources = "abc-news";
            }



            errorLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            String country = Utils.getCountry();
            String language = Utils.getLanguage();

            Call<News> call;

            if (keyword.length() > 0) {
                call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
            } else {
                call = apiInterface.getNews(sources, API_KEY);
            }

            call.enqueue(new Callback<News>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.isSuccessful() && response.body().getArticle() != null) {

                        if (!articles.isEmpty()) {
                            articles.clear();
                        }

                        articles = response.body().getArticle();
                        adapter = new NewsAdapter(response.body().getArticle());
                        recyclerView.setAdapter(adapter);

                        initListener();

                        topHeadline.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);


                    } else {

                        topHeadline.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                        String errorCode;
                        switch (response.code()) {
                            case 404:
                                errorCode = "404 not found";
                                break;
                            case 500:
                                errorCode = "500 server broken";
                                break;
                            default:
                                errorCode = "unknown error";
                                break;
                        }

                        showErrorMessage(
                                R.drawable.no_result,
                                "No Result",
                                "Please Try Again!\n" +
                                        errorCode);

                    }
                }


                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorMessage(
                            R.drawable.oops,
                            "Oops..",
                            "Network failure, Please Try Again\n" +
                                    t.toString());
                }
            });

        }



    private void initListener(){

        adapter.setOnItemClickListener((NewsAdapter.OnItemClickListener) (view, position) -> {
            ImageView imageView = view.findViewById(R.id.img);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);

            Article article = articles.get(position);
            intent.putExtra("url", article.getUrl());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("img",  article.getUrlToImage());
            intent.putExtra("date",  article.getPublishedAt());
            intent.putExtra("source",  article.getSource().getName());
            intent.putExtra("author",  article.getAuthor());

            Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),(View)imageView,ViewCompat.getTransitionName(imageView));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, optionsCompat.toBundle());
            }else {
                startActivity(intent);
            }

        });

    }


    @Override
    public void onRefresh() {
        LoadJson("");
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onLoadingSwipeRefresh(final String keyword){

        final boolean post = swipeRefreshLayout.post(
                () -> {
                    LoadJson(keyword);
                }
        );

    }

    private void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(v -> onLoadingSwipeRefresh(""));

    }


    }

