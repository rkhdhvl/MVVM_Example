package com.example.mvvm_java.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DogBreed.class}, version =1 )
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "ExampleDatabase")
                    .build();
        }
        return instance;
    }

    public abstract DatabaseDao databaseDao();

}
