package com.eme.mas.model.entity;

import java.io.Serializable;

/**
 * Created by zulei on 16/8/25.
 */
public class BrotherLocationBo implements Serializable{

    /**
     * order_state : null
     * state_info : null
     * create_time : null
     * dest_distance : null
     * location_info : null
     * current_location : null
     * order_id : null
     * dest_location : null
     * operator : null
     */

    private String order_state;
    private String state_info;
    private String create_time;
    private String dest_distance;
    private String location_info;
    private String current_location;
    private String order_id;
    private String dest_location;
    private String operator;

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getState_info() {
        return state_info;
    }

    public void setState_info(String state_info) {
        this.state_info = state_info;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDest_distance() {
        return dest_distance;
    }

    public void setDest_distance(String dest_distance) {
        this.dest_distance = dest_distance;
    }

    public String getLocation_info() {
        return location_info;
    }

    public void setLocation_info(String location_info) {
        this.location_info = location_info;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDest_location() {
        return dest_location;
    }

    public void setDest_location(String dest_location) {
        this.dest_location = dest_location;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
