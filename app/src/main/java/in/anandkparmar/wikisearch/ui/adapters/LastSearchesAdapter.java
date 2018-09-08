package in.anandkparmar.wikisearch.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.anandkparmar.wikisearch.R;
import in.anandkparmar.wikisearch.db.LastSearches;
import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import in.anandkparmar.wikisearch.ui.activities.SearchActivity;

public class LastSearchesAdapter extends RecyclerView.Adapter<LastSearchesAdapter.SearchResultViewHolder> {
    private SearchActivity activity;
    private List<LastSearches> lastSearchesList;

    public LastSearchesAdapter(SearchActivity activity, List<LastSearches> lastSearchesList) {
        this.activity = activity;
        this.lastSearchesList = lastSearchesList;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_search_result, viewGroup, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultViewHolder holder, int i) {
        LastSearches lastSearch = lastSearchesList.get(i);

        holder.title.setText(lastSearch.getTitle());
        holder.description.setText(lastSearch.getDescription());
        if(lastSearch.getImageSource() != null) {
            Picasso.get().load(lastSearch.getImageSource()).into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    holder.imageView.setAlpha(1.0f);
                }

                @Override
                public void onError(Exception e) {
                    holder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher));
                    holder.imageView.setAlpha(0.2f);
                }
            });
        } else {
            holder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher));
            holder.imageView.setAlpha(0.2f);
        }
    }

    @Override
    public int getItemCount() {
        return lastSearchesList.size();
    }

    public void updateList(List<LastSearches> lastSearchesList) {
        this.lastSearchesList = lastSearchesList;
        notifyDataSetChanged();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView imageView;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.description)
        TextView description;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.onItemClickListenerForPreviousSearch(getLayoutPosition());
        }
    }
}
