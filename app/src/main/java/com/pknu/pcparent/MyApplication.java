package com.pknu.pcparent;

import android.app.Application;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;

import com.pknu.pcparent.R;

/**
 * Created by Hoon on 2017-06-18.
 */

public class MyApplication extends Application {

    /***** 기본 전역 변수 *****/
    public static boolean IS_AUTO_STARTED = false;  // 자동실행 플래그
    public final static String PROCESS_NAME = "com.pknu.pcparent";
    public final static String MAIN_ACTIVITY_NAME = "com.pknu.pcparent.activity.MainActivity";
    public final static String PARAM_FORM = "%loc%";

    /***** Color 전역 변수 *****/
    private int COLOR_PRIMARY;
    private int COLOR_PRIMARY_DARK;
    private int COLOR_PRIMARY_LIGHT;
    private int COLOR_ACCENT;
    private int COLOR_ACCENT_SHADE;
    private int COLOR_ACCENT_LIGHT;
    private int COLOR_TEXT_PRIMARY;
    private int COLOR_TEXT_SECONDARY;
    private int COLOR_ICON;
    private int COLOR_DIVIDER;
    private int COLOR_RED_DANGER;
    private int COLOR_BTN_GREEN;
    private int COLOR_BTN_BLUE;
    private int COLOR_BTN_RED;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int getColorPrimary() {
        return COLOR_PRIMARY;
    }

    public void setColorPrimary(int colorPrimary) {
        COLOR_PRIMARY = colorPrimary;
    }

    public int getColorPrimaryDark() {
        return COLOR_PRIMARY_DARK;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        COLOR_PRIMARY_DARK = colorPrimaryDark;
    }

    public int getColorPrimaryLight() {
        return COLOR_PRIMARY_LIGHT;
    }

    public void setColorPrimaryLight(int colorPrimaryLight) {
        COLOR_PRIMARY_LIGHT = colorPrimaryLight;
    }

    public int getColorAccent() {
        return COLOR_ACCENT;
    }

    public void setColorAccent(int colorAccent) {
        COLOR_ACCENT = colorAccent;
    }

    public int getColorAccentShade() {
        return COLOR_ACCENT_SHADE;
    }

    public void setColorAccentShade(int colorAccentShade) {
        COLOR_ACCENT_SHADE = colorAccentShade;
    }

    public int getColorAccentLight() {
        return COLOR_ACCENT_LIGHT;
    }

    public void setColorAccentLight(int colorAccentLight) {
        COLOR_ACCENT_LIGHT = colorAccentLight;
    }

    public int getColorTextPrimary() {
        return COLOR_TEXT_PRIMARY;
    }

    public void setColorTextPrimary(int colorTextPrimary) {
        COLOR_TEXT_PRIMARY = colorTextPrimary;
    }

    public int getColorTextSecondary() {
        return COLOR_TEXT_SECONDARY;
    }

    public void setColorTextSecondary(int colorTextSecondary) {
        COLOR_TEXT_SECONDARY = colorTextSecondary;
    }

    public int getColorIcon() {
        return COLOR_ICON;
    }

    public void setColorIcon(int colorIcon) {
        COLOR_ICON = colorIcon;
    }

    public int getColorDivider() {
        return COLOR_DIVIDER;
    }

    public void setColorDivider(int colorDivider) {
        COLOR_DIVIDER = colorDivider;
    }

    public int getColorRedDanger() {
        return COLOR_RED_DANGER;
    }

    public void setColorRedDanger(int colorRedDanger) {
        COLOR_RED_DANGER = colorRedDanger;
    }

    public int getColorBtnGreen() {
        return COLOR_BTN_GREEN;
    }

    public void setColorBtnGreen(int colorBtnGreen) {
        COLOR_BTN_GREEN = colorBtnGreen;
    }

    public int getColorBtnBlue() {
        return COLOR_BTN_BLUE;
    }

    public void setColorBtnBlue(int colorBtnBlue) {
        COLOR_BTN_BLUE = colorBtnBlue;
    }

    public int getColorBtnRed() {
        return COLOR_BTN_RED;
    }

    public void setColorBtnRed(int colorBtnRed) {
        COLOR_BTN_RED = colorBtnRed;
    }

}
