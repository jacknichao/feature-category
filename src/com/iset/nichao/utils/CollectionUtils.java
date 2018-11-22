package com.iset.nichao.utils;/**
 * Created by ChaoNi on 2017/3/24.
 * 集合类的工具
 */

import java.util.ArrayList;

public class CollectionUtils {

    /**
     *将ArrayList<Integer>转换成int[]
     * @param arrayList
     * @return
     */
    public static int[] arrayListToInt(ArrayList<Integer> arrayList) {
        if (arrayList == null || arrayList.size() == 0) return null;
        int[] arr = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            arr[i] = arrayList.get(i);
        }
        return arr;
    }

    /**
     * 将int[]转换成ArrayList<Integer>
     * @param arr
     * @return
     */
    public static ArrayList<Integer> intToArrayList(int[] arr){
        if(arr==null || arr.length==0) return null;
        ArrayList<Integer> arrayList=new ArrayList<Integer>(arr.length);
        for(int elem:arr){
            arrayList.add(elem);
        }
        return arrayList;
    }


    /**
     * 将ArrayList中的数据转化成一个字符串，将并这些数据以_进行分割
     * @param arrayList
     * @return
     */
    public static String arrayListToStringSplitByUnderline(ArrayList<Integer> arrayList){
        if(arrayList==null || arrayList.size()==0) return "";
        StringBuilder sb =new StringBuilder();
        sb.append(arrayList.get(0));
        for(int i=1;i<arrayList.size();i++){
            sb.append("_"+arrayList.get(i));
        }
        return sb.toString();
    }
}
