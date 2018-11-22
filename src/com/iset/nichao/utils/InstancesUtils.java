package com.iset.nichao.utils;

import weka.core.Instances;

/**
 * 处理训练实例的工具类
 * Created by ChaoNi on 2015/12/31.
 */
public class InstancesUtils {
    /**
     * 将两个数据集进行整合，第一个数据集将放在前面，第二个数据集中的实例将放在后面
     *
     * @param first  第一个数据集放在前面
     * @param second 第二个数据集放在后面
     */
    public static Instances combineDataset(Instances first, Instances second) {
        Instances tmp = null;
        try {
            tmp = new Instances(first);
            for (int i = 0; i < second.numInstances(); i++) {
                tmp.add(second.instance(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

}
