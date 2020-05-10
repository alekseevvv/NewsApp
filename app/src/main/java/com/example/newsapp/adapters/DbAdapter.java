package com.example.newsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.App;
import com.example.newsapp.R;
import com.example.newsapp.dbase.AppDataBase;
import com.example.newsapp.dbase.Source;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DbAdapter extends RecyclerView.Adapter<DbAdapter.ViewHolder> {

    private List<Source> source;


    public DbAdapter(List<Source> source, Context context) {
        this.source = source;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.channel_item, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Source model =  source.get(position);

        holder.source.setText(model.getName());
        holder.desc.setText(model.getDescription());
        holder.btn_del.setBackgroundColor(Color.YELLOW);
        //Чтение списка каналов из бд
        holder.btn_del.setOnClickListener(v -> {
            AppDataBase db = App.getInstance().getDb();
            db.sourceDao().getById(model.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<Source>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onSuccess(Source model) {
                            Runnable run = () -> db.sourceDao().delete(model);
                            Thread t = new Thread(run);
                            t.start();

                            Toast.makeText(App.getInstance(), R.string.del, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    });
        });

    }

        @Override
        public int getItemCount() {
            return source.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView desc, source;
            Button btn_del;
            public ViewHolder(View itemView) {
                super(itemView);

                btn_del = itemView.findViewById(R.id.btn_favor);
                desc = itemView.findViewById(R.id.desc);
                source = itemView.findViewById(R.id.source);

            }
        }
    }