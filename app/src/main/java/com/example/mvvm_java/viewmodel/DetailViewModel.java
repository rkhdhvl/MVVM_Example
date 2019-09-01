package com.example.mvvm_java.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_java.model.AppDatabase;
import com.example.mvvm_java.model.DogBreed;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogBreedValue = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }
    private RetrieveDogDetails detailsTask;
    public void getBreedInfo()
    {
        DogBreed dogInfo = new DogBreed("1","Corgi","","","","","");
        dogBreedValue.setValue(dogInfo);
    }

    public void fetch(int uuid)
    {
        detailsTask = new RetrieveDogDetails();
        detailsTask.execute(uuid);
    }

    private class RetrieveDogDetails extends AsyncTask<Integer,Void,DogBreed>
    {
        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return AppDatabase.getInstance(getApplication()).databaseDao().getDogBreedInfo(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogBreedValue.setValue(dogBreed);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if(detailsTask!=null)
        {
            detailsTask.cancel(true);
        }
        detailsTask = null;
    }
}
