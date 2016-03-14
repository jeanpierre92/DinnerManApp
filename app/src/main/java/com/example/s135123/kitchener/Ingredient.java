package com.example.s135123.kitchener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by s130604 on 5-3-2016.
 */
public class Ingredient {
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
        amount = Double.parseDouble(getJsonInfo(o, "amount"));
        unitLong = getJsonInfo(o, "unitLong");
        unitShort = getJsonInfo(o, "unitShort");
        aisle = getJsonInfo(o, "aisle");
        name = getJsonInfo(o, "name");
        originalString = getJsonInfo(o, "originalString");
    }

    public String getJsonInfo(JSONObject o, String info) {
        try {
            return o.getJSONObject(info).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
