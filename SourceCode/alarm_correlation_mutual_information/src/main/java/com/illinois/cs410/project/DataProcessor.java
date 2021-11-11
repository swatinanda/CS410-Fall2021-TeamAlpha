package com.illinois.cs410.project;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataProcessor {
    LinkedHashMap <Integer, BitSet> t;
    int intervals;

    public double[][] getMutualInfomation() {
        return mutualInfomation;
    }

    double[][] mutualInfomation;
    private final ThreadLocal<BitSet> tempBits;

    public DataProcessor(LinkedHashMap<Integer, BitSet> templates, int intervals) {
        this.t = templates;
        this.intervals = intervals;
        this.tempBits  = ThreadLocal.withInitial(() -> new BitSet(8640));
        this.mutualInfomation = new double[templates.size()][templates.size()];
    }
    private double getLog2(double number) {
        if (number == 0)
            return 0.0D;
        return (1.0D * Math.log(number) / Math.log(2));
    }
    public void computerMutualInformation() {
        int size = t.size();
        for(int i =0; i<size; i++)
        {
            for(int k=i+1; k<size; k++)
            {
                //System.out.format("Calculating mutual Information between %d and %d \n", i, k);
                BitSet tBits = tempBits.get();
                BitSet i_bit = t.get(i);
                BitSet k_bit = t.get(k);
                //System.out.println("Cardinality = "+i_bit.cardinality()+"Length = "+i_bit.length()+", Size = "+i_bit.size());
                double p_i_true = (1.0D * i_bit.cardinality())/intervals;
                double p_i_false = 1.00 - p_i_true;
                double p_k_true = (1.0D * k_bit.cardinality())/intervals;
                double p_k_false = 1.00 - p_k_true;
                tBits.or(i_bit);
                tBits.and(k_bit);
                int intersection = tBits.cardinality();
                double p_i_true_k_true = 1.0D * intersection/intervals;
                tBits.clear();
                double p_i_true_k_false = p_i_true - p_i_true_k_true;
                double p_i_false_k_true = p_k_true - p_i_true_k_true;
                double p_i_false_k_false = 1 - p_i_true_k_true - p_i_true_k_false - p_i_false_k_true;

                double infotmation = p_i_false_k_false * getLog2(1.0D * (p_i_false_k_false)/(p_i_false * p_k_false)) +
                    p_i_false_k_true * getLog2(1.0D * (p_i_false_k_true)/(p_i_false * p_k_true)) +
                    p_i_true_k_false * getLog2(1.0D * (p_i_true_k_false)/(p_i_true * p_k_false)) +
                    p_i_true_k_true * getLog2(1.0D * (p_i_true_k_true)/(p_i_true * p_k_true)) ;
                //System.out.format("Calculating mutual Information between %d and %d is %f\n", i, k, infotmation);
                this.mutualInfomation[i][k] = infotmation;
                this.mutualInfomation[k][i] = infotmation;
            }
        }
    }
}
