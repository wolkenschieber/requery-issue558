package de.nebelhoernchen.requery558;

import android.app.Application;
import android.os.StrictMode;

import de.nebelhoernchen.requery558.model.Models;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.ConfigurationBuilder;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import io.requery.sql.TransactionMode;

public class App extends Application
{
    private static final int DB_VERSION = 1;
    private ReactiveEntityStore<Persistable> dataStore;

    @Override
    public void onCreate()
    {
        super.onCreate();
        StrictMode.enableDefaults();
    }

    ReactiveEntityStore<Persistable> getData(TransactionMode transactionMode)
    {
        if (dataStore == null)
        {
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT, DB_VERSION);
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            source.setLoggingEnabled(true);

            ConfigurationBuilder configurationBuilder =
                    new ConfigurationBuilder(source, Models.DEFAULT).useDefaultLogging();
            configurationBuilder.setTransactionMode(transactionMode);
            configurationBuilder.useDefaultLogging();
            Configuration configuration = configurationBuilder.build();
            dataStore = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }

}
