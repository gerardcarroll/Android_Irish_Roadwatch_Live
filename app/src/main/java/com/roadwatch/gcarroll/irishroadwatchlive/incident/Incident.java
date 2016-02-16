package com.roadwatch.gcarroll.irishroadwatchlive.incident;

import java.util.Date;

/**
 * Created by gcarroll on 16/02/2016.
 */
public class Incident {

  private String area;

  private int id;

  private int incidentTypeId;

  private Double latitude;

  private String location;

  private Double longitude;

  private String report;

  private String title;

  private Date updatedAt;

  private int zoomLevel;

  public String getArea() {
    return area;
  }

  public void setArea(final String area) {
    this.area = area;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public int getIncidentTypeId() {
    return incidentTypeId;
  }

  public void setIncidentTypeId(final int incidentTypeId) {
    this.incidentTypeId = incidentTypeId;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(final Double latitude) {
    this.latitude = latitude;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(final Double longitude) {
    this.longitude = longitude;
  }

  public String getReport() {
    return report;
  }

  public void setReport(final String report) {
    this.report = report;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(final Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public int getZoomLevel() {
    return zoomLevel;
  }

  public void setZoomLevel(final int zoomLevel) {
    this.zoomLevel = zoomLevel;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    final Incident incident = (Incident) o;

    if (id != incident.id)
      return false;
    if (incidentTypeId != incident.incidentTypeId)
      return false;
    if (zoomLevel != incident.zoomLevel)
      return false;
    if (!area.equals(incident.area))
      return false;
    if (!latitude.equals(incident.latitude))
      return false;
    if (!location.equals(incident.location))
      return false;
    if (!longitude.equals(incident.longitude))
      return false;
    if (!report.equals(incident.report))
      return false;
    if (!title.equals(incident.title))
      return false;
    return updatedAt.equals(incident.updatedAt);

  }

  @Override
  public int hashCode() {
    int result = area.hashCode();
    result = 31 * result + id;
    result = 31 * result + incidentTypeId;
    result = 31 * result + latitude.hashCode();
    result = 31 * result + location.hashCode();
    result = 31 * result + longitude.hashCode();
    result = 31 * result + report.hashCode();
    result = 31 * result + title.hashCode();
    result = 31 * result + updatedAt.hashCode();
    result = 31 * result + zoomLevel;
    return result;
  }

}
