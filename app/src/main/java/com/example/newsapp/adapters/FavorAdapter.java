package com.example.newsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsapp.App;
import com.example.newsapp.R;
import com.example.newsapp.dbase.AppDataBase;
import com.example.newsapp.dbase.Articles;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.MyViewHolder>{
    private boolean online;
    private List<Articles> articles;

    private Context context;
    private Button btn_favor;

    public FavorAdapter(List<Articles> articles, Context context, boolean online) {
        this.articles = articles;
        this.context = context;
        this.online = online;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        btn_favor = view.findViewById(R.id.btn_favor);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorAdapter.MyViewHolder holder, int position) {

         final Articles model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        AppDataBase db = App.getInstance().getDb();
        Articles art = new Articles(model);

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        if (online == true){
            holder.source.setText(model.getSource().getName());}
        else {
        holder.source.setText(model.getSourcenameOff());}

        holder.time.setText(" \u2022 " + DateToTimeFormat(model.getPublishedAt()));
        holder.published_ad.setText(DateFormat(model.getPublishedAt()));
        holder.author.setText(model.getAuthor());
        //Проверка новости на наличие в бд
        //Удалить/добавить
        holder.btnFavor.setOnClickListener(v ->{
                            db.articlesDao().getArticleByUrl(art.getUrl())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableSingleObserver<Articles>() {
                                @Override
                                public void onSuccess(Articles art) {
                                    Runnable run = () -> db.articlesDao().delete(art);
                                    Thread t = new Thread(run);
                                    t.start();
                                    holder.btnFavor.setBackgroundColor(Color.GRAY);
                                    Toast.makeText(App.getInstance(), R.string.del, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Runnable run = () -> db.articlesDao().insertAll(art);
                                    Thread t = new Thread(run);
                                    t.start();
                                    holder.btnFavor.setBackgroundColor(Color.YELLOW);
                                    Toast.makeText(App.getInstance(), R.string.add, Toast.LENGTH_SHORT).show();
                                }
                            });
                });
        //Проверка списка новостей на наличие в бд по url
        db.articlesDao().getArticleByUrl(art.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Articles>() {
                    @Override
                    public void onSuccess(Articles art) {
                        holder.btnFavor.setBackgroundColor(Color.YELLOW);
                    }

                    @Override
                    public void onError(Throwable e) {
                        holder.btnFavor.setBackgroundColor(Color.GRAY);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, author, published_ad, source, time;
        ImageView imageView;
        Button btnFavor;

        public MyViewHolder(View itemView) {
            super(itemView);
            btnFavor = itemView.findViewById(R.id.btn_favor);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            desc.setMaxLines(2);
            author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);

        }

    }
    public static String DateFormat(String oldstringDate){
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale("ru"));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldstringDate;
        }

        return newDate;
    }
    public static String DateToTimeFormat(String oldstringDate){
        PrettyTime p = new PrettyTime(new Locale("ru"));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }
}
