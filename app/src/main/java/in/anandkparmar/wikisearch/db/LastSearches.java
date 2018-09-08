package in.anandkparmar.wikisearch.db;

import android.support.annotation.NonNull;

import java.util.List;

import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;

public class LastSearches extends RealmObject {
    private String title;
    private String description;
    private String fullurl;
    private String imageSource;
    private long timestamp;

    public LastSearches() {
    }

    public LastSearches(SearchListResponse.SearchListItem searchListItem) {
        this.title = searchListItem.getTitle();
        this.description = searchListItem.getDescription();
        this.fullurl = searchListItem.getFullurl();
        if (searchListItem.getThumbnail() != null && searchListItem.getThumbnail().getSource() != null) {
            this.imageSource = searchListItem.getThumbnail().getSource();
        }
        this.timestamp = System.currentTimeMillis();
    }

    public LastSearches(String title, String description, String fullurl, String imageSource) {
        this.title = title;
        this.description = description;
        this.fullurl = fullurl;
        this.imageSource = imageSource;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static void save(final SearchListResponse.SearchListItem searchListItem) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealm(new LastSearches(searchListItem));
            }
        });
    }

    public static List<LastSearches> getLastSearches() {
        return Realm.getDefaultInstance().where(LastSearches.class).findAll().sort("timestamp", Sort.DESCENDING);
    }
}
