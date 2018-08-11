package org.example.android.amp.util.db;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import org.example.android.amp.app.AmpApp;
import org.example.android.amp.db.dao.UserDao;
import org.example.android.amp.db.database.AppDatabase;
import org.example.android.amp.db.entity.UserEntity;
import org.example.android.amp.model.User;

import io.reactivex.Observable;

public class UserDbUtils {

    private static AppDatabase db = Room
            .databaseBuilder(AmpApp.getInstance(), AppDatabase.class, "database")
            .build();

    public static Observable<User> register(@NonNull User user) {
        UserEntity userEntity = convertFromUser(user);

        UserDao userDao = db.getUserDao();

        userDao.insertAll(userEntity);

        return Observable.just(convertFromUserEntity(userEntity));
    }

    public static Observable<User> findByUsername(@NonNull String username) {
        UserDao userDao = db.getUserDao();

        UserEntity userEntity = userDao.getByUsername(username);

        if (userEntity == null) {
            return Observable.empty();
        }

        return Observable.just(convertFromUserEntity(userEntity));
    }

    private static UserEntity convertFromUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(user.getUsername(),
                user.getPassword(), user.getFirstName(), user.getLastName());
    }

    private static User convertFromUserEntity(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());

        return user;
    }
}
