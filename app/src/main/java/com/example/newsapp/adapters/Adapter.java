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

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    private List<Source> sources;
    private Context context;

    public Adapter(List<Source> sources, Context context) {
        this.sources = sources;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        final Source model =  sources.get(position);
        AppDataBase db = App.getInstance().getDb();
        //Проверка списка на наличие в избранном
        Source mdf = sources.get(position);
        db.sourceDao().getById(mdf.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Source>() {
                    @Override
                    public void onSuccess(Source model) {
                        holder.btnFavor.setBackgroundColor(Color.YELLOW);
                    }
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onError(Throwable e) {
                        holder.btnFavor.setBackgroundColor(Color.GRAY);
                    }
                });

        //Проверка на наличие в избранном по Id
        //если нет в избранном, то добавить, иначе удалить
        holder.btnFavor.setOnClickListener(v ->{
            db.sourceDao().getById(model.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<Source>() {
                        @Override
                        public void onSuccess(Source model) {
                            Runnable run = () -> db.sourceDao().delete(model);
                            Thread t = new Thread(run);
                            t.start();
                            holder.btnFavor.setBackgroundColor(Color.GRAY);
                            Toast.makeText(App.getInstance(), R.string.del, Toast.LENGTH_SHORT).show();
                        }

                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onError(Throwable e) {
                            Runnable run = () -> db.sourceDao().insertAll(model);

                            Thread t = new Thread(run);
                            t.start();
                            holder.btnFavor.setBackgroundColor(Color.YELLOW);
                            Toast.makeText(App.getInstance(), R.string.add, Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        holder.source.setText(model.getName());
        holder.desc.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView desc,source;
        Button btnFavor;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnFavor = itemView.findViewById(R.id.btn_favor);

            desc = itemView.findViewById(R.id.desc);
            source = itemView.findViewById(R.id.source);
        }

    }

}