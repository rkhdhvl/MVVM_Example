package com.example.mvvm_java.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvm_java.R;
import com.example.mvvm_java.databinding.FragmentDetailsBinding;
import com.example.mvvm_java.model.DogBreed;
import com.example.mvvm_java.util.ImageUtils;
import com.example.mvvm_java.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    DetailViewModel detailViewModel;
    int dogUuid;
    private FragmentDetailsBinding binding;

    public DetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false);
        this.binding = binding;
        return binding.getRoot();
        /*View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dogUuid = DetailsFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.fetch(dogUuid);

        observeInformation();
    }


    /*
    * An example code for demonstrating Android Jetpack masterclasses in Java*/


    private void observeInformation() {
        detailViewModel.dogBreedValue.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed) {
                binding.setDogInfo(dogBreed);

                // example of accessing the layout element after using databinding
              //  binding.purpose.setText("Some value");

                // before implementing data binding
                /*
                ImageUtils.loadImage(breed_image, dogBreed.imageURL, ImageUtils.getProgressDrawable(breed_image.getContext()));
                details.setText(dogBreed.dogBreed);
                purpose.setText(dogBreed.breedGroup);
                lifespan.setText(dogBreed.lifeSpan);
                temperament.setText(dogBreed.temperament);*/
            }
        });
    }

/*
    void goToListFragment()
    {
        NavDirections action = DetailsFragmentDirections.backToList();
        Navigation.findNavController(detailsFab).navigate(action);
    }
*/


}
