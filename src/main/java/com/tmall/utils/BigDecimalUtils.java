package com.tmall.utils;


import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal mul(BigDecimal price,Integer quantity){
        if (price == null || quantity == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        String s = quantity.toString();
        BigDecimal temp = new BigDecimal(s);

        return price.multiply(temp);
    }


}
