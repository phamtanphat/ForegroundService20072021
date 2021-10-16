package com.example.foregroundservice20072021;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    int resourceMp3;
    String name;

    public Song(int resourceMp3, String name) {
        this.resourceMp3 = resourceMp3;
        this.name = name;
    }

    protected Song(Parcel in) {
        resourceMp3 = in.readInt();
        name = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resourceMp3);
        dest.writeString(name);
    }
}
