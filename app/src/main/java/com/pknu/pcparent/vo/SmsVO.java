package com.pknu.pcparent.vo;

import android.util.Log;

import com.pknu.pcparent.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hoon on 2017-06-11.
 */

public class SmsVO {

    private int no;         // PK
    private String smsNo;   // SMS 발신자 번호
    private String smsDate; // SMS 발신 시각
    private String smsText; // SMS 내용
    private Double lat;     // 위도
    private Double lng;     // 경도
    private String address; // 주소

    public SmsVO(int no, String smsNo, String smsDate, String smsText, Double lat, Double lng, String address) {
        this.no = no;
        this.smsNo = smsNo;
        this.smsDate = smsDate;
        this.smsText = smsText;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public String getDateString() {
        SimpleDateFormat orgFormat = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh:mm:ss", Locale.KOREA);
        SimpleDateFormat todayFormat = new SimpleDateFormat("aa h:mm:ss");
        SimpleDateFormat yesterdayFormat = new SimpleDateFormat("M월 dd일");
        try {
            Date curDate = new Date(System.currentTimeMillis());
            Date orgDate = orgFormat.parse(this.smsDate);
            String todayDate = todayFormat.format(orgDate);
            String yesterdayDate = yesterdayFormat.format(orgDate);
            long diffDay = curDate.getTime()/(1000 * 60 * 60 * 24) - orgDate.getTime()/(1000 * 60 * 60 * 24)/* / (1000 * 60 * 60 * 24)*/; // 일 단위로 비교
//            Log.d("getDateString()", "todayDate: " + todayDate);
//            Log.d("getDateString()", "yesterdayDate: " + yesterdayDate);
//            Log.d("getDateString()", "curDate: " + curDate);
//            Log.d("getDateString()", "orgDate: " + orgDate);
//            Log.d("getDateString()", "diffDay: " + diffDay);

            // 오늘 구분해서 반환
            if(diffDay < 1) // 오늘 ("aa h:mm:ss")
                return todayDate;
            else            // 오늘이 아닐 때 ("M월 dd일")
                return yesterdayDate;
        } catch (Exception e) {
            e.printStackTrace();
            return this.smsDate;
        }
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getSmsNo() {
        return smsNo;
    }

    public void setSmsNo(String smsNo) {
        this.smsNo = smsNo;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
