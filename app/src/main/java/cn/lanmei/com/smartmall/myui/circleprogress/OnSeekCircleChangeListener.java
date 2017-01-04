package cn.lanmei.com.smartmall.myui.circleprogress;

import android.view.View;

public interface OnSeekCircleChangeListener<T extends View>{

        /**
         * Notification that the progress level has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically.
         *
         *
         *            The SeekCircle whose progress has changed
         * @param progress
         *            The current progress level. This will be in the range
         *            0..max where max was set by
         *            {@link ProgressCircle#setMax(int)}. (The default value for
         *            max is 100.)
         * @param fromUser
         *            True if the progress change was initiated by the user.
         */
        void onProgressChanged(T seekView, int progress, boolean fromUser);

        /**
         * Notification that the user has started a touch gesture. Clients may
         * want to use this to disable advancing the seek circle.
         *
         *
         *            The SeekCircle in which the touch gesture began
         */
        void onStartTrackingTouch(T seekView);

        /**
         * Notification that the user has finished a touch gesture. Clients may
         * want to use this to re-enable advancing the seek circle.
         *
         *
         *            The SeekCircle in which the touch gesture began
         */
        void onStopTrackingTouch(T seekView);
    }