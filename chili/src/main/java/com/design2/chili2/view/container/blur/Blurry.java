package com.design2.chili2.view.container.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class Blurry {

    private static final String TAG = Blurry.class.getSimpleName();

    public static Composer with(Context context) {
        return new Composer(context);
    }

    public static class Composer {

        private final View blurredView;
        private final Context context;
        private final BlurFactor factor;

        public Composer(Context context) {
            this.context = context;
            blurredView = new View(context);
            blurredView.setTag(TAG);
            factor = new BlurFactor();
        }

        public Composer radius(int radius) {
            factor.radius = radius;
            return this;
        }

        public Composer sampling(int sampling) {
            factor.sampling = sampling;
            return this;
        }

        public Composer color(int color) {
            factor.color = color;
            return this;
        }


        public BitmapComposer.ImageComposer capture(View capture) {
            return new BitmapComposer.ImageComposer(context, capture, factor);
        }

        public BitmapComposer from(Bitmap bitmap) {
            return new BitmapComposer(context, bitmap, factor);
        }

        public static class BitmapComposer {

            private final Context context;
            private final Bitmap bitmap;
            private final BlurFactor factor;

            public BitmapComposer(Context context, Bitmap bitmap, BlurFactor factor) {
                this.context = context;
                this.bitmap = bitmap;
                this.factor = factor;
            }

            public static class ImageComposer {

                private final Context context;
                private final View capture;
                private final BlurFactor factor;

                public ImageComposer(Context context, View capture, BlurFactor factor) {
                    this.context = context;
                    this.capture = capture;
                    this.factor = factor;
                }

                public Bitmap get() {
                    factor.width = capture.getMeasuredWidth();
                    factor.height = capture.getMeasuredHeight();
                    return Blur.of(capture, factor);
                }
            }
        }
    }
}
