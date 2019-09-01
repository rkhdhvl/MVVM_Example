package com.example.mvvm_java.model;

import java.util.List;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiList {
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getListOfDogBreeds();
}
