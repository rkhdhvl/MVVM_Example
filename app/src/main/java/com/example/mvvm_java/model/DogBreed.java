package com.example.mvvm_java.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "DogBreed")
public class DogBreed {

    @ColumnInfo(name = "id")
    @SerializedName("id")
    public String breedId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String dogBreed;

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    public String lifeSpan;

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    public String breedGroup;

    @ColumnInfo(name = "breed_for")
    @SerializedName("breed_for")
    public String bredFor;

    @SerializedName("temperament")
    public String temperament;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    public String imageURL;

    @PrimaryKey(autoGenerate = true)
    public int uui;

    public DogBreed(String breedId, String dogBreed, String lifeSpan, String breedGroup, String bredFor, String temperament, String imageURL) {
        this.breedId = breedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        this.temperament = temperament;
        this.imageURL = imageURL;
    }


}
