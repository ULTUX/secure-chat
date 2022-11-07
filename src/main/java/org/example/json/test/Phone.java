package org.example.json.test;

import lombok.Data;

import java.util.List;

@Data
public class Phone {
    int id;
    String title;
    String description;
    int price;
    String discountPercentage;
    float rating;
    int stock;
    String brand;
    String category;
    String thumbnail;
    List<String> images;


}
