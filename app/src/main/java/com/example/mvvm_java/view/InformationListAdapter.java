package com.example.mvvm_java.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm_java.R;
import com.example.mvvm_java.databinding.ListItemBinding;
import com.example.mvvm_java.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.InformationViewHolder> implements RowClickedListener {

    private ArrayList<DogBreed> listOfDogBreeds;

    public InformationListAdapter(ArrayList<DogBreed> listOfDogBreeds) {
        this.listOfDogBreeds = listOfDogBreeds;
    }

    public void updateListOfDogBreeds(List<DogBreed> updatedListOfDogBreeds) {
        listOfDogBreeds.clear();
        listOfDogBreeds.addAll(updatedListOfDogBreeds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemBinding view = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new InformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        holder.itemView.setDogType(listOfDogBreeds.get(position));
        holder.itemView.setListener(this);
        /*ImageView imageView = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifespan = holder.itemView.findViewById(R.id.lifespan);
        LinearLayout linearLayout = holder.itemView.findViewById(R.id.linear_layout);
        name.setText(listOfDogBreeds.get(position).dogBreed);
        lifespan.setText(listOfDogBreeds.get(position).lifeSpan);
        ImageUtils.loadImage(imageView,listOfDogBreeds.get(position).imageURL,ImageUtils.getProgressDrawable(imageView.getContext()));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListFragmentDirections.ViewDetails directions = ListFragmentDirections.viewDetails();
                directions.setDogUuid(listOfDogBreeds.get(position).uui);
                Navigation.findNavController(linearLayout).navigate(directions);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listOfDogBreeds.size();
    }

    @Override
    public void onRowClicked(View view) {
        String uuidString = ((TextView)view.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);
        ListFragmentDirections.ViewDetails directions = ListFragmentDirections.viewDetails();
        directions.setDogUuid(uuid);
        Navigation.findNavController(view).navigate(directions);
    }

    class InformationViewHolder extends RecyclerView.ViewHolder {
        public ListItemBinding itemView;

        public InformationViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
