package in.anandkparmar.wikisearch;

import android.app.Application;

import io.realm.Realm;

public class WikiSearchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
