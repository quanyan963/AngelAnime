package com.tsdm.angelanime.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tsdm.angelanime.R;
import com.tsdm.angelanime.application.MyApplication;
import com.tsdm.angelanime.introduction.PopAnimAdapter;
import com.tsdm.angelanime.widget.DividerItemDecoration;
import com.tsdm.angelanime.widget.listener.PopUpListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.graphics.Bitmap.Config.RGB_565;
import static com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY;

/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public class Utils {
    public static final boolean isLog = false;

    public static String getColorStr(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String r1 = Utils.getBothColor(r);
        String g1 = Utils.getBothColor(g);
        String b1 = Utils.getBothColor(b);
        return r1 + g1 + b1;
    }

    public static VectorDrawableCompat changeSVGColor(@DrawableRes int drawable, @ColorRes int color, Context context) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources()
                , drawable, context.getTheme());
        vectorDrawableCompat.setTint(context.getResources().getColor(color));
        return vectorDrawableCompat;

    }

    public static void hideSoftKeyboard(Context context, View view) {
        if (view == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param activity
     */
    public static void showOrHideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 判断软键盘是否显示方法
     *
     * @param activity
     * @return
     */

    public static boolean isSoftShowing(Activity activity) {
        //获取当屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(activity);
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public static String getBothColor(int str) {
        if (str < 16) {
            return "0" + Integer.toHexString(str);
        } else {
            return Integer.toHexString(str);
        }
    }

    public static DisplayImageOptions getImageOptions() {
        return getImageOptions(R.mipmap.defult_img, 0);
    }

    public static DisplayImageOptions getImageOptions(int defaultIconId) {
        return getImageOptions(defaultIconId, 0);
    }

    public static DisplayImageOptions getImageOptions(int defaultIconId, int cornerRadiusPixels) {
        return new DisplayImageOptions.Builder()
                //.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .bitmapConfig(RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(EXACTLY)
                .build();
    }

    public static void displayImage(Context context, String uri, ImageView imageView) {
        MyApplication.getImageLoader(context).displayImage(uri, imageView, getImageOptions());
    }

    public static void displayImage(Context context, String uri, ImageView imageView,
                                    DisplayImageOptions displayImageOptions) {
        MyApplication.getImageLoader(context).displayImage(uri, imageView, displayImageOptions);
    }

    public static void showBottomPopUp(Activity activity, final List<String> animationList,
                                       final PopUpListener listener, ViewGroup rootView, int position) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.pop_anim_list, rootView, false);
        dialogView.setBackgroundResource(R.color.transparent);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.mtv_title);
        ImageView ivclose = (ImageView) dialogView.findViewById(R.id.img_close);
        tvTitle.setText(String.format(MyApplication.getInstance().getResources().getString(R.string.
                count), animationList.size() + ""));
        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final RecyclerView animList = (RecyclerView) dialogView.findViewById(R.id.rlv_anim_list);
        animList.setHasFixedSize(true);
        animList.setLayoutManager(new GridLayoutManager(activity, 3));
        animList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.BOTH_SET,
                activity.getResources().getDimensionPixelSize(R.dimen.dp_8_x),
                activity.getResources().getColor(R.color.white)));
        final PopAnimAdapter popAnimAdapter = new PopAnimAdapter(animationList, activity);
        popAnimAdapter.setOnPopItemClickListener((new PopAnimAdapter.PopItemClick() {
            @Override
            public void onClick(int position) {
                listener.onPopUpClick(position);
            }
        }));
        animList.setAdapter(popAnimAdapter);
        popAnimAdapter.setPosition(position);
        dialog.setContentView(dialogView);
        dialog.show();
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        activity.getWindow().setAttributes(lp);
    }

    public static String getShowTime(int timerCount) {
        int min = timerCount / 60 < 0 ? 0 : timerCount / 60;
        String minute = min < 10 ? "0" + min : min + "";
        int s = timerCount % 60 < 0 ? 0 : timerCount % 60;
        String second = s < 10 ? "0" + s : s + "";
        return minute + ":" + second;
    }

    public static String[] musicMedias = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Albums.ALBUM_ID};

    public static boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.
                NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }

//    public static String getWeekString(String weeks, Context context) {
//        int[] week = detail_new int[7];
//        String repeatText = "";
//        String[] menu = context.getResources().getStringArray(R.array.week_repeat_list);
//        if (weeks.equals(Constants.ALL_WEEK)) {
//            return context.getString(R.string.everyday);
//        } else if (weeks.equals(Constants.NONE_WEEK)) {
//            return context.getString(R.string.none);
//        } else if (weeks.equals(Constants.WORK_DAY)) {
//            return context.getString(R.string.workday);
//        } else if (weeks.equals(Constants.WEEKEND_DAY)) {
//            return context.getString(R.string.weekend);
//        } else {
//            for (int i = 0; i < weeks.length(); i++) {
//                week[i] = Integer.parseInt(String.valueOf(weeks.charAt(i)));
//                if (week[i] == 1) {
//                    repeatText += " " + menu[i];
//                }
//            }
//            if (repeatText.length() <= 4) {
//                repeatText = context.getString(R.string.every) + repeatText;
//            }
//            return repeatText;
//        }
//    }

    public static String utf8Togb2312(String str) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            switch (c) {

                case '+':

                    sb.append(' ');

                    break;

                case '%':

                    try {

                        sb.append((char) Integer.parseInt(

                                str.substring(i + 1, i + 3), 16));

                    } catch (NumberFormatException e) {

                        throw new IllegalArgumentException();

                    }

                    i += 2;

                    break;

                default:

                    sb.append(c);

                    break;

            }

        }

        String result = sb.toString();

        String res = null;

        try {

            byte[] inputBytes = result.getBytes("8859_1");

            res = new String(inputBytes, "UTF-8");

        } catch (Exception e) {
        }

        return res;

    }

// 将 GB2312 编码格式的字符串转换为 UTF-8 格式的字符串：

    public static String gb2312ToUtf8(String str) {

        String urlEncode = "";

        try {

            urlEncode = URLEncoder.encode(str, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return urlEncode;

    }

    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }


    public byte[] gbk2utf8(String chenese) {


        char c[] = chenese.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);
            System.out.println(word);
            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");
            System.out.println(sb.toString());
            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);
            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];
        }
        return fullByte;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    public static String formatData(int data) {
        return data < 10 ? "0" + data : data + "";
    }

    public static String formatData(String data) {
        int s = Integer.parseInt(data);
        return formatData(s);
    }

    public static String formatHex(int data) {
        String s = ((Integer) data).toHexString(data);
        return data < 16 ? "0" + s : s + "";
    }

    public static String getSubTime(String time) {
        return time.substring(0, 2);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    public static void setVolume(int volume) {
        AudioManager audioManager = (AudioManager) MyApplication.getInstance().getSystemService(
                Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.
                FLAG_SHOW_UI);
    }

    public static int format12Hour(int meridian, int hour) {
        int formatHour;
        if (meridian == 0) {
            if (hour == 11) {
                formatHour = 0;
            } else {
                formatHour = hour + 1;
            }
        } else {
            if (hour == 11) {
                formatHour = hour + 1;
            } else {
                formatHour = hour + 13;
            }
        }
        return formatHour;
    }

    public static float x = 0;
    public static float y = 0;

//    public static boolean changeViewColor(View v, MotionEvent event, Context context, ViewClickListener listener) {
//        Drawable drawable = ((ImageView) v).getDrawable().mutate();
//        Drawable.ConstantState state = drawable.getConstantState();
//        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
//        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            DrawableCompat.setTint(drawable1, ContextCompat.getColor(context, R.color.low_grey));
//            ((ImageView) v).setImageDrawable(drawable1);
//            x = event.getX();
//            y = event.getY();
//            return true;
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            return true;
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            DrawableCompat.setTint(drawable1, ContextCompat.getColor(context, R.color.white));
//            ((ImageView) v).setImageDrawable(drawable1);
//            if (Math.abs(event.getX() - x) < (v.getWidth() * 2 / 3) &&
//                    Math.abs(event.getY() - y) < (v.getHeight() * 2 / 3)) {
//                listener.getViewId(v.getId());
//            }
//            return false;
//        } else {
//            DrawableCompat.setTint(drawable1, ContextCompat.getColor(context, R.color.white));
//            ((ImageView) v).setImageDrawable(drawable1);
//            return false;
//        }
//    }

    public static int getSoundValue(int value, float everyValue) {
        if (value % everyValue > everyValue / 2f) {
            return (int) (value / everyValue + 1);
        }
        return (int) (value / everyValue);
    }

    public static void Logger(String TAG, String type, String value) {
        if (!isLog)
            return;
        Log.i(TAG, type + ":------" + value);
    }
}
