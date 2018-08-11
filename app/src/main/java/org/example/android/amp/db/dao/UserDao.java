package org.example.android.amp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.example.android.amp.db.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertAll(UserEntity... users);

    @Query("SELECT * FROM user WHERE username = (:username)")
    UserEntity getByUsername(String username);

    @Query("SELECT * FROM user")
    List<UserEntity> getAll();

    @Delete
    void delete(UserEntity user);

}
