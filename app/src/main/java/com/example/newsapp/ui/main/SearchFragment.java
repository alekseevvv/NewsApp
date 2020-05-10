package com.example.newsapp.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapters.FavorAdapter;
import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiInterface;
import com.example.newsapp.dbase.Articles;
import com.example.newsapp.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapp.ui.main.PlaceholderFragment.API_KEY;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    private TextView txt_internet;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private List<Articles> article;

    public static SearchFragment newInstance(int index) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 3;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EditText editText = root.findViewById(R.id.editText);
        txt_internet = root.findViewById(R.id.txt_internet);
        txt_internet.setText("");
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                   LoadJson( s);
            }
        });
        return root;
    }
    public void LoadJson(CharSequence q){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<News> call;
        call = apiInterface.getNewsSearch(q,API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){

                    article = response.body().getArticle();
                    FavorAdapter adapter = new FavorAdapter(article, getActivity(),true);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    txt_internet.setText(R.string.int_desc);
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                txt_internet.setText(R.string.notfound);
            }
        });

    }
}