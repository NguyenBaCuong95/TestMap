package com.example.testmap.model;

import java.util.List;

public class Item {
    private String title;
    private String id;
    private String resultType;
    private String houseNumberType;
    private Address address;
    private Position position;
    private List<Access> access;
    private MapView mapView;
    private Scoring scoring;

    public Item() {
    }

    public Item(String title, String id, String resultType, String houseNumberType, Address address,
                Position position, List<Access> access, MapView mapView, Scoring scoring) {
        this.title = title;
        this.id = id;
        this.resultType = resultType;
        this.houseNumberType = houseNumberType;
        this.address = address;
        this.position = position;
        this.access = access;
        this.mapView = mapView;
        this.scoring = scoring;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getHouseNumberType() {
        return houseNumberType;
    }

    public void setHouseNumberType(String houseNumberType) {
        this.houseNumberType = houseNumberType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Access> getAccess() {
        return access;
    }

    public void setAccess(List<Access> access) {
        this.access = access;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public Scoring getScoring() {
        return scoring;
    }

    public void setScoring(Scoring scoring) {
        this.scoring = scoring;
    }
}
