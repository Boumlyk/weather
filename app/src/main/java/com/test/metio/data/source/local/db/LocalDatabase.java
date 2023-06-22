package com.test.metio.data.source.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.test.metio.data.source.local.db.dao.CookiesDao;
import com.test.metio.data.source.local.db.dao.WeatherDao;
import com.test.metio.module.cookies.Cookies;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.tools.security.AuthenticityChecker;
import com.test.metio.tools.security.HashManager;

import net.sqlcipher.database.SupportFactory;

import dagger.hilt.android.qualifiers.ApplicationContext;


@Database(entities = {Cookies.class, CurrentWeatherEntity.class}, version = 7, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract CookiesDao cookiesDao();
    public abstract WeatherDao weatherDao();



    private static LocalDatabase instance = null;
    public static synchronized LocalDatabase getInstance(@ApplicationContext Context context) {
        if (instance == null) {
            synchronized (LocalDatabase.class) {
                if (instance == null)
                    instance = buildDatabase(context);
            }
        }
        return instance;
    }

    private static LocalDatabase buildDatabase(Context context) {
        final byte[] passphrase = HashManager.hashString(AuthenticityChecker.getCurrentSignature(context)+ context.getPackageName(), HashManager.HEX_FORM).getBytes();
        final SupportFactory factory = new SupportFactory(passphrase);
        return Room.databaseBuilder(context, LocalDatabase.class, "weather-db")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .fallbackToDestructiveMigration()
                 .openHelperFactory(factory)
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyDatabase() {
        instance = null;
    }

}
