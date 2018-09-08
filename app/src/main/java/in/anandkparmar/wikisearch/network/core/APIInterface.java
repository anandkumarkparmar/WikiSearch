package in.anandkparmar.wikisearch.network.core;

import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/w/api.php?action=query&format=json" +
            "&prop=info%7Cpageimages%7Cdescription&list=&titles=" +
            "&generator=prefixsearch&formatversion=2" +
            "&inprop=url&piprop=thumbnail%7Cname")
    Observable<SearchListResponse> getSearchList(@Query("gpssearch") String query);
}
