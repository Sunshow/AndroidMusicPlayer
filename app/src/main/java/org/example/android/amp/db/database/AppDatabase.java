package org.example.android.amp.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.example.android.amp.db.dao.UserDao;
import org.example.android.amp.db.entity.UserEntity;

@Database(entities = {UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
