package com.iset.nichao.utils;/**
 * Created by ChaoNi on 2017/3/23.
 */

import java.io.File;
import java.util.ArrayList;

/**
 * 初始化数据数据集合的工具类
 */
public class FileSetUtils {

    /**
     * * 初始化项目文件集合
     *
     * @param dataSetName 项目集合名称E.G. AEEEM Relief
     * @param fileSets    传入的项目集合
     */
    public static void initFileSet(String dataSetName, ArrayList<File> fileSets) {
        //首先清空该项目集合中的所有文件
        fileSets.clear();
        File dataFile = new File("d:\\testinput\\" + dataSetName);
        if (dataFile.isFile()) {// 仅仅是一个文件
            fileSets.add(dataFile);
        } else if (dataFile.isDirectory()) {// 输入的是一个目录，里面有一组文件
            for (File file : dataFile.listFiles()) {
                // 不支持嵌套的目录，如果在目录下还有目录，则直接忽略
                if (file.isFile())
                    fileSets.add(file);
            }
        }
    }
}
