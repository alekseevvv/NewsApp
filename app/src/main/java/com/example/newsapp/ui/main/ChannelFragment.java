package com.example.newsapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.adapters.Adapter;
import com.example.newsapp.R;
import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiInterface;
import com.example.newsapp.dbase.Source;
import com.example.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChannelFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    public static final String API_KEY = "b74de9f058fd49fda341fbc1d9c9bc67";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Source> source = new ArrayList<>();
    private Adapter adapter;
    private TextView txt_internet;

    public static ChannelFragment newInstance(int index) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
                View root = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        txt_internet = root.findViewById(R.id.txt_internet);
        LoadJson();

        return root;
    }
    public void LoadJson(){
//Чтение списка с Api
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<News> call;
        call = apiInterface.getNews(API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getSource() != null){

                    if (!source.isEmpty()){
                        source.clear();
                    }
                    source = response.body().getSource();
                    adapter = new Adapter(source, getActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(getActivity(),R.string.notfound,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                txt_internet.setText(R.string.int_desc);
            }
        });


    }

}