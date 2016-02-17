package com.roadwatch.gcarroll.irishroadwatchlive.incident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Incident {

  @SerializedName("Area")
  @Expose
  private String Area;

  @SerializedName("ID")
  @Expose
  private Integer ID;

  @SerializedName("IncidentTypeID")
  @Expose
  private Integer IncidentTypeID;

  @SerializedName("Latitude")
  @Expose
  private Double Latitude;

  @SerializedName("Location")
  @Expose
  private String Location;

  @SerializedName("Longitude")
  @Expose
  private Double Longitude;

  @SerializedName("Report")
  @Expose
  private String Report;

  @SerializedName("Title")
  @Expose
  private String Title;

  @SerializedName("UpdatedAt")
  @Expose
  private String UpdatedAt;

  @SerializedName("ZoomLevel")
  @Expose
  private Integer ZoomLevel;

  /**
   * @return The Area
   */
  public String getArea() {
    return Area;
  }

  /**
   * @param Area The Area
   */
  public void setArea(final String Area) {
    this.Area = Area;
  }

  /**
   * @return The ID
   */
  public Integer getID() {
    return ID;
  }

  /**
   * @param ID The ID
   */
  public void setID(final Integer ID) {
    this.ID = ID;
  }

  /**
   * @return The IncidentTypeID
   */
  public Integer getIncidentTypeID() {
    return IncidentTypeID;
  }

  /**
   * @param IncidentTypeID The IncidentTypeID
   */
  public void setIncidentTypeID(final Integer IncidentTypeID) {
    this.IncidentTypeID = IncidentTypeID;
  }

  /**
   * @return The Latitude
   */
  public Double getLatitude() {
    return Latitude;
  }

  /**
   * @param Latitude The Latitude
   */
  public void setLatitude(final Double Latitude) {
    this.Latitude = Latitude;
  }

  /**
   * @return The Location
   */
  public String getLocation() {
    return Location;
  }

  /**
   * @param Location The Location
   */
  public void setLocation(final String Location) {
    this.Location = Location;
  }

  /**
   * @return The Longitude
   */
  public Double getLongitude() {
    return Longitude;
  }

  /**
   * @param Longitude The Longitude
   */
  public void setLongitude(final Double Longitude) {
    this.Longitude = Longitude;
  }

  /**
   * @return The Report
   */
  public String getReport() {
    return Report;
  }

  /**
   * @param Report The Report
   */
  public void setReport(final String Report) {
    this.Report = Report;
  }

  /**
   * @return The Title
   */
  public String getTitle() {
    return Title;
  }

  /**
   * @param Title The Title
   */
  public void setTitle(final String Title) {
    this.Title = Title;
  }

  /**
   * @return The UpdatedAt
   */
  public String getUpdatedAt() {
    return UpdatedAt;
  }

  /**
   * @param UpdatedAt The UpdatedAt
   */
  public void setUpdatedAt(final String UpdatedAt) {
    this.UpdatedAt = UpdatedAt;
  }

  /**
   * @return The ZoomLevel
   */
  public Integer getZoomLevel() {
    return ZoomLevel;
  }

  /**
   * @param ZoomLevel The ZoomLevel
   */
  public void setZoomLevel(final Integer ZoomLevel) {
    this.ZoomLevel = ZoomLevel;
  }

}
