package com.example.mvvm_java.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mvvm_java.R;
import com.example.mvvm_java.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    private InformationListAdapter informationListAdapter = new InformationListAdapter(new ArrayList<>());

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.error_data)
    TextView error_data;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public ListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /*ListFragmentDirections.ViewDetails viewDetails = ListFragmentDirections.viewDetails();
       Navigation.findNavController(view).navigate(viewDetails);*/
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.refresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(informationListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setVisibility(View.GONE);
                error_data.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                listViewModel.bypassCache();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        observeViewModel();
    }

    private void observeViewModel()
    {
        listViewModel.list_of_dog_breeds.observe(this,dogBreedsList -> {
            if(dogBreedsList!=null && dogBreedsList instanceof List)
            {
                recyclerView.setVisibility(View.VISIBLE);
                informationListAdapter.updateListOfDogBreeds(dogBreedsList);
            }
        });

        listViewModel.load_error.observe(this, isError -> {
            if(isError!=null && isError instanceof Boolean)
            {
                error_data.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        listViewModel.loading.observe(this, isLoading -> {
         if(isLoading!=null && isLoading instanceof Boolean)
         {
             progressBar.setVisibility(isLoading ? View.VISIBLE:View.INVISIBLE);
             if(isLoading)
             {
                 recyclerView.setVisibility(View.GONE);
                 error_data.setVisibility(View.GONE);
             }
         }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.first_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.actionSettings:
                if(isAdded())
                {
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
