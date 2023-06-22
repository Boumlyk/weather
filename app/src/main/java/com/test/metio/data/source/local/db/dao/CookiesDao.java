package com.test.metio.data.source.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.metio.module.cookies.Cookies;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CookiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertOrUpdateCookies(Cookies cookies);

    @Query("SELECT * FROM myCookies WHERE cookiesId = :cookies_id")
    Single<Cookies> getCookies(String cookies_id);

    @Query("SELECT EXISTS(SELECT * FROM myCookies WHERE cookiesId = :cookies_id)")
    Single<Boolean> exist(String cookies_id);

}