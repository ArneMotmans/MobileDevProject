package pxl.be.watchlist.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pxl.be.watchlist.R;

/**
 * Created by 11501537 on 21/10/2017.
 */

public class ActorViewHolder extends RecyclerView.ViewHolder {
    public ImageView actorImageView;
    public TextView characterNameTextView;
    public TextView actorNameTextView;

    public ActorViewHolder(View itemView) {
        super(itemView);
        actorImageView = itemView.findViewById(R.id.actorImageView);
        characterNameTextView = itemView.findViewById(R.id.characterNameTextView);
        actorNameTextView = itemView.findViewById(R.id.actorNameTextView);
    }
}
