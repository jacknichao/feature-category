package com.iset.nichao.driver;/**
 * Created by ChaoNi on 2017/3/23.
 */


import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
       String relink = "D:\\testinput\\Relink\\Apache.arff";
        String aeeem = "D:\\testinput\\AEEEM\\EQ.arff";
        HashMap<String,String> hashMap=new HashMap<String, String>();
        hashMap.put(relink,"Relink");
        hashMap.put(aeeem,"AEEEM");

        for(String name:new String[]{relink,aeeem}){
            try {
                BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(hashMap.get(name)+"IndexToName.csv"));
                Instances instances= ConverterUtils.DataSource.read(name);
                for(int i=0;i<instances.numAttributes();i++){
                    bufferedWriter.write(i+","+instances.attribute(i).name()+"\r\n");
                }
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
