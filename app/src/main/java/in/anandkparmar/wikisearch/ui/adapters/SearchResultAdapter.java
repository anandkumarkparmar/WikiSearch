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
import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import in.anandkparmar.wikisearch.ui.activities.SearchActivity;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private SearchActivity activity;
    private List<SearchListResponse.SearchListItem> searchListItems;

    public SearchResultAdapter(SearchActivity activity, List<SearchListResponse.SearchListItem> searchListItems) {
        this.activity = activity;
        this.searchListItems = searchListItems;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_search_result, viewGroup, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultViewHolder holder, int i) {
        SearchListResponse.SearchListItem searchListItem = searchListItems.get(i);

        holder.title.setText(searchListItem.getTitle());
        holder.description.setText(searchListItem.getDescription());
        if(searchListItem.getThumbnail() != null && searchListItem.getThumbnail().getSource() != null) {
            Picasso.get().load(searchListItem.getThumbnail().getSource()).into(holder.imageView, new Callback() {
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
        return searchListItems.size();
    }

    public void updateList(List<SearchListResponse.SearchListItem> searchListItems) {
        this.searchListItems = searchListItems;
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
            activity.onItemClickListenerForSearch(getLayoutPosition());
        }
    }
}
