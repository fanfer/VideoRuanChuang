package com.projectUtil;


public class BeCutSubtitleSpan {
    public Long startTime;
    public Long endTime;
    public String subtitle;

    public boolean isChecked=false;

    private String convertToTime(Long l){
        return l/3600000 + ":" + (l/60000)%60 + ":" + (l/1000)%60;
    }

    @Override
    public String toString() {
        return convertToTime(startTime) + "-" + convertToTime(endTime) + ":" + subtitle;
    }
}
