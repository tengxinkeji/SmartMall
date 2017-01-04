package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public File onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        }
        return null;
    }
}
