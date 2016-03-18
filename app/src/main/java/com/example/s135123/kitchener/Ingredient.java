package com.example.s135123.kitchener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by s130604 on 5-3-2016.
 */
public class Ingredient implements Serializable {
    /*
    example:
    {
         "amount":2.375,
         "unit":"ounces",
         "metaInformation":[

         ],
         "unitShort":"oz",
         "aisle":"Baking",
         "name":"flour",
         "originalString":"1/2 cup (2 3/8 ounces) all-purpose flour",
         "unitLong":"ounces"
      }
     */
    double amount;
    String unitLong;
    String unitShort;
    String aisle;
    String name;
    String originalString;

    Ingredient(JSONObject o) {
        try {
            amount=o.getDouble("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            unitLong = o.getString("unitLong");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            unitShort = o.getString("unitShort");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            aisle = o.getString("aisle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = o.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            originalString = o.getString("originalString");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getOriginalString(){
        return originalString;
    }
}
