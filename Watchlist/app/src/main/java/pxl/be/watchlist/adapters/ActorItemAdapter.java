package pxl.be.watchlist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import pxl.be.watchlist.R;
import pxl.be.watchlist.domain.ActorsPage;
import pxl.be.watchlist.domain.Cast;
import pxl.be.watchlist.domain.MovieDetails;
import pxl.be.watchlist.services.ImageApiService;
import pxl.be.watchlist.viewholders.ActorViewHolder;

/**
 * Created by 11501537 on 21/10/2017.
 */

public class ActorItemAdapter extends RecyclerView.Adapter<ActorViewHolder> {

    private Context context;
    private ActorsPage page;

    public ActorItemAdapter(Context context, ActorsPage page){
        this.context = context;
        this.page = page;
    }

    @Override
    public ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_items_layout, parent, false);
        ActorViewHolder holder = new ActorViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActorViewHolder holder, int position) {
        Cast cast = page.getCast().get(position);
        Picasso.with(context).load(ImageApiService.BASE_URL+cast.getProfilePath());
        holder.actorNameTextView.setText(cast.getName());
        holder.characterNameTextView.setText(cast.getCharacter());
    }

    @Override
    public int getItemCount() {
        return page.getCast().size();
    }
}
