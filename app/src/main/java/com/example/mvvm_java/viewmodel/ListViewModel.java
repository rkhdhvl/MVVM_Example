package com.example.mvvm_java.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_java.model.ApiService;
import com.example.mvvm_java.model.AppDatabase;
import com.example.mvvm_java.model.DatabaseDao;
import com.example.mvvm_java.model.DogBreed;
import com.example.mvvm_java.util.NotificationsHelper;
import com.example.mvvm_java.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> list_of_dog_breeds = new MutableLiveData<>();
    public MutableLiveData<Boolean> load_error = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private ApiService apiService = new ApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    InsertIntoDatabaseTask insertIntoDatabaseTask;
    RetrieveDataFromDatabase retrieveDataFromDatabase;

    private SharedPrefHelper prefHelper = SharedPrefHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public void refresh()
    {
        long updateTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if(updateTime!=0 && currentTime-updateTime<refreshTime)
        {
            fetchFromDatabase();
        }
        else
        {
            fetchFromRemote();
        }
    }

    public void bypassCache()
    {
        fetchFromRemote();
    }

    private void checkCacheDuration()
    {
        String cachePreference = prefHelper.getCacheDuration();
        if(!cachePreference.equals(""))
        {
            try
            {
               int cachePreferenceInt = Integer.parseInt(cachePreference);
               refreshTime = cachePreferenceInt * 1000 *1000 * 1000L;
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void fetchFromRemote()
    {
        loading.setValue(true);
        disposable.add(apiService.getDogs()
                 .subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                     @Override
                     public void onSuccess(List<DogBreed> dogBreeds) {
                         insertIntoDatabaseTask = new InsertIntoDatabaseTask();
                         insertIntoDatabaseTask.execute(dogBreeds);
                         prefHelper.saveUpdateTime(System.nanoTime());
                         Toast.makeText(getApplication(),"Values inserted",Toast.LENGTH_LONG).show();
                         NotificationsHelper.getInstance(getApplication()).createNotifications();
                     }

                     @Override
                     public void onError(Throwable e) {
                         load_error.setValue(true);
                         loading.setValue(false);
                     }
                 })
        );
    }


    private void setRetrievedDogInfo(List<DogBreed> dogBreeds)
    {
        list_of_dog_breeds.setValue(dogBreeds);
        load_error.setValue(false);
        loading.setValue(false);
    }

    private void fetchFromDatabase()
    {
        retrieveDataFromDatabase = new RetrieveDataFromDatabase();
        retrieveDataFromDatabase.execute();
        Toast.makeText(getApplication(),"Fetched from database",Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertIntoDatabaseTask!=null)
        {
            insertIntoDatabaseTask.cancel(true);
            insertIntoDatabaseTask = null;
        }

        if(retrieveDataFromDatabase!=null)
        {
            retrieveDataFromDatabase.cancel(true);
            retrieveDataFromDatabase = null;
        }

    }


    private class InsertIntoDatabaseTask extends AsyncTask<List<DogBreed>,Void,List<DogBreed>>
    {

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];
            DatabaseDao dao = AppDatabase.getInstance(getApplication()).databaseDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> arrayList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(arrayList.toArray(new DogBreed[0]));

            int i = 0;
            while (i<result.size())
            {
                list.get(i).uui = result.get(i).intValue();
                ++i;
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            setRetrievedDogInfo(dogBreeds);
        }
    }


    private class RetrieveDataFromDatabase extends AsyncTask<Void,Void,List<DogBreed>>
    {

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return AppDatabase.getInstance(getApplication()).databaseDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            setRetrievedDogInfo(dogBreeds);
        }
    }


}
