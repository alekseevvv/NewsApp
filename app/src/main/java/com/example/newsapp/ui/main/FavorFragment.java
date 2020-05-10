package com.example.newsapp.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.App;
import com.example.newsapp.adapters.DbAdapter;
import com.example.newsapp.R;
import com.example.newsapp.adapters.FavorAdapter;
import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiInterface;
import com.example.newsapp.dbase.AppDataBase;
import com.example.newsapp.dbase.Articles;
import com.example.newsapp.dbase.Source;
import com.example.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapp.ui.main.PlaceholderFragment.API_KEY;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavorFragment extends Fragment {
    private TextView txt_favor;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_news;
    private List<Articles> article = new ArrayList<>();
    private String source = "";
    private FavorAdapter adapter = null;


    public static FavorFragment newInstance(int index) {
        FavorFragment fragment = new FavorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 2;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favor, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        btn_news = root.findViewById(R.id.btn_get_news);
        btn_news.setOnClickListener(onClickListenert);
        txt_favor = root.findViewById(R.id.txt_favor);

        source = "";
        getDataBase();
            return root;
        }
    private View.OnClickListener onClickListenert = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(boolj){
                getDataBase();
            }else
            LoadJson(source);

        }
    };
        private Boolean boolj = false;
    private void LoadJson(String source){
        boolj = true;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<News> call;
        call = apiInterface.getNewsArt(API_KEY,source);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getArticle() != null){

                    if (!article.isEmpty()){
                        article.clear();
                    }
                    article = response.body().getArticle();
                    adapter = new FavorAdapter(article, getActivity(),true);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(),R.string.notfound,Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                AppDataBase db = App.getInstance().getDb();
                db.articlesDao().getall()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(art -> {
                            adapter = new FavorAdapter(art, getActivity(),false);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        });
            }
        });

    }

    @SuppressLint("CheckResult")
    private void getDataBase(){
        boolj = false;
        AppDataBase db = App.getInstance().getDb();

        db.sourceDao().getall()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Source>>() {
                    @Override
                    public void accept(List<Source> sour) throws Exception {
                        Source model;
                        source = "";
                        for (int i = 0; i < sour.size(); i++) {
                            model =  sour.get(i);
                            source = source + model.getId() + ",";
                        }
                        DbAdapter databaseAdapter = new DbAdapter(sour, getActivity());
                        recyclerView.setAdapter(databaseAdapter);
                        if (layoutManager.getItemCount()==0){txt_favor.setText(R.string.empty);}
                        else {txt_favor.setText("");}
                    }
                });
    }
}