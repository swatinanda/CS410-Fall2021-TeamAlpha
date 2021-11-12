package com.illinois.cs410.project;

import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class MatrixGenerator {

    int[][] templateMatrix;
    public MatrixGenerator(List<Incident> incidents, int intervals, int totalTemplates)
    {
        templateMatrix = new int[intervals][totalTemplates];
        int totalIncidents = incidents.size() - 1;
        int startIndex = 0;
        for (int i = 0; i < intervals; i++) {
            int index = i % totalIncidents;
            if(index == 0)
                startIndex = 0;
            Incident incident = incidents.get(index);
            int[] bitIndex = getEnabledBit(incident.getTemplates(), incident.getRandomCount());
            for (int k = 0; k < bitIndex.length; k++) {
                templateMatrix[i][startIndex + bitIndex[k]] = 1;
            }
            Incident noise = incidents.get(totalIncidents);
            bitIndex = getEnabledBit(noise.getTemplates(), noise.getRandomCount());
            for (int k = 0; k < bitIndex.length; k++) {
                templateMatrix[i][totalTemplates - noise.getTemplates() + bitIndex[k]] = 1;
            }
            startIndex += incident.getTemplates();
        }

    }

    public int[][] getTemplateMatrix() {
        return templateMatrix;
    }
    Random rand = new Random();
    private int[] getEnabledBit(int templates, int randomCount) {

        HashSet<Integer> randomBit = new HashSet<>();
        while(true)
        {
            randomBit.add(rand.nextInt(templates));
            if(randomBit.size() == randomCount)
                break;
        }
        return randomBit.stream().mapToInt(Number::intValue).toArray();


        /*int[] enabledBits = new int[randomCount];
        for(int k =0; k<randomCount; k++){
            int value = rand.nextInt(templates);
            enabledBits[k] = value;
        }
        System.out.println("Requested "+randomCount+" Got "+enabledBits.length);
        return enabledBits;*/
    }

    public LinkedHashMap<Integer, BitSet> doDataProcessing() {
        LinkedHashMap<Integer, BitSet> storage = new LinkedHashMap<>();
        for(int col=0; col<templateMatrix[0].length; col++)
        {
            BitSet intervals = new BitSet();
            for(int row =0; row< templateMatrix.length; row++)
            {
                if(templateMatrix[row][col] ==1 )
                    intervals.set(row);
            }
            storage.put(col, intervals);
        }
        return storage;
    }
}
