package com.example.mvvm_java.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mvvm_java.R;
import com.example.mvvm_java.databinding.FragmentDetailsBinding;
import com.example.mvvm_java.databinding.SendSmsLayoutBinding;
import com.example.mvvm_java.model.BgPalette;
import com.example.mvvm_java.model.DogBreed;
import com.example.mvvm_java.model.SmsInfo;
import com.example.mvvm_java.util.ImageUtils;
import com.example.mvvm_java.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    DetailViewModel detailViewModel;
    int dogUuid;
    private FragmentDetailsBinding binding;
    private Boolean sendSmsStarted = false;
    private DogBreed currentVal;

    public DetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        setHasOptionsMenu(true);
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
                currentVal = dogBreed;
                if (dogBreed.imageURL != null) {
                    setUpBackgroundColor(dogBreed.imageURL);
                }

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

    private void setUpBackgroundColor(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    BgPalette bgPalette = new BgPalette(intColor);
                                    binding.setPalette(bgPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_TEXT,currentVal.dogBreed+" "+currentVal.bredFor);
                intent.putExtra(Intent.EXTRA_STREAM,currentVal.imageURL);
                startActivity(Intent.createChooser(intent,"Share with"));
                break;

            case R.id.action_send_sms:
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        if (isAdded() && sendSmsStarted && permissionGranted) {
            SmsInfo smsInfo = new SmsInfo("", currentVal.dogBreed + "bred for" + currentVal.bredFor, currentVal.imageURL);
            SendSmsLayoutBinding sendSmsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.send_sms_layout, null, false);
            sendSmsLayoutBinding.setSmsInfo(smsInfo);
            new AlertDialog.Builder(getContext())
                    .setView(sendSmsLayoutBinding.getRoot())
                    .setPositiveButton("Send SMS", ((dialog, which) -> {
                        if (!sendSmsLayoutBinding.smsText.getText().toString().isEmpty()) {
                            smsInfo.to = sendSmsLayoutBinding.smsDestination.getText().toString();
                            sendSms(smsInfo);
                        }
                    }))
                    .setNegativeButton("No", (dialog, which) -> {

                    })
                    .show();
            sendSmsStarted = false;
        }
    }

    private void sendSms(SmsInfo smsInfo) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to,null,smsInfo.text,pi,null);
    }

}
