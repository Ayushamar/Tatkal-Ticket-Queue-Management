package com.tokenbackend.dto;

public class CounterPassengerDto {
    private Integer counterNo;
    private Integer tokenNo;
    private String name;
    private String maskedAadhaar;
    private String journeyDate;
    private String trainNo;
    private String station;

    public Integer getCounterNo() { return counterNo; }
    public void setCounterNo(Integer counterNo) { this.counterNo = counterNo; }

    public Integer getTokenNo() { return tokenNo; }
    public void setTokenNo(Integer tokenNo) { this.tokenNo = tokenNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMaskedAadhaar() { return maskedAadhaar; }
    public void setMaskedAadhaar(String maskedAadhaar) { this.maskedAadhaar = maskedAadhaar; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }

    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
} 