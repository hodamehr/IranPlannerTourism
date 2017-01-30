
package com.iranplanner.tourism.iranplannertourism.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultItineraryAttractionListFull  implements Serializable {

    @SerializedName("Status")
    @Expose
    private Status status;
    @SerializedName("Result_Itinerary_Attraction")
    @Expose
    private List<ResultItineraryAttraction> resultItineraryAttraction = null;
    @SerializedName("Statistics")
    @Expose
    private Statistics statistics;
    @SerializedName("ParsDID")
    @Expose
    private ParsDID parsDID;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ResultItineraryAttraction> getResultItineraryAttraction() {
        return resultItineraryAttraction;
    }

    public void setResultItineraryAttraction(List<ResultItineraryAttraction> resultItineraryAttraction) {
        this.resultItineraryAttraction = resultItineraryAttraction;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public ParsDID getParsDID() {
        return parsDID;
    }

    public void setParsDID(ParsDID parsDID) {
        this.parsDID = parsDID;
    }

}
