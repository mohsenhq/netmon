package org.redapps.netmon.util;

import java.util.Random;

public class RandomCodeGenerator {

    public String generate(int len){
        Random r = new Random( System.currentTimeMillis() );
        int base = (int)Math.pow(10, len-1) ;
        Integer rnd =  ((1 + r.nextInt(2)) * base + r.nextInt(base));

        return Integer.toString(rnd);
    }
}
