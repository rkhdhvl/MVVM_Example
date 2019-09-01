package com.example.mvvm_java.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseDao {
@Insert
List<Long> insertAll(DogBreed... dogBreed);

@Query("SELECT * FROM DogBreed")
    List<DogBreed> getAllDogs();

@Query("DELETE FROM DogBreed")
    void deleteAllDogs();

}
