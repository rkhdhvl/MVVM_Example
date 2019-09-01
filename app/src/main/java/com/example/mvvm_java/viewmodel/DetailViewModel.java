package com.example.mvvm_java.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_java.model.DogBreed;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogBreed = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void getBreedInfo()
    {
        DogBreed dogInfo = new DogBreed("1","Corgi","","","","","");
        dogBreed.setValue(dogInfo);
    }


}
