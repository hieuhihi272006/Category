package com.javaweb.model.builder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoMoItem {

    private String id;
    private String name;
    private String category;
    private String image;
    private String currency = "VND";
    private long price;
    private int quantity;
    private long totalPrice;
     
}
