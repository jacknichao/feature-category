package com.iset.nichao.driver;

import com.iset.nichao.utils.CategoryEnums;
import com.iset.nichao.utils.CollectionUtils;
import com.iset.nichao.utils.FileSetUtils;
import com.iset.nichao.utils.MapFeatureToCatagory;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by ChaoNi on 2017/3/23.
 * 功能：根据各个属性的类别来完成跨项目缺陷预测的研究
 * 也就是说，根据属性的类别，依次选择每一类的属性进行跨项目
 */

public class FeatureCategoryModel {
    private static Logger logger= Logger.getLogger(FeatureCategoryModel.class.getName());

    //类别编号->类别成员
    private static HashMap<CategoryEnums,ArrayList<String>> cateEnumToMembers= MapFeatureToCatagory.getCateEnumToMembers();

    /**
     * 数据集名称的枚举
     */
    private enum DatasetEnum{
        Relink,
        PROMISE
    }

    public static void main(String[] args) throws Exception{
        ArrayList<File> fileSets=new ArrayList<File>();
        BufferedWriter bufferedWriter=null;
        try {
            for (DatasetEnum datasetEnum: new DatasetEnum[]{DatasetEnum.Relink})

//                for (DatasetEnum datasetEnum: new DatasetEnum[]{DatasetEnum.Relink, DatasetEnum.AEEEM})
            {
                bufferedWriter=new BufferedWriter(new FileWriter("d:\\JCSTresults\\"+datasetEnum.name()+".csv"));
                bufferedWriter.write("runs,Source,Target,Category,F1,f1(0),f1(1),AUC,precision,recall\r\n");

                FileSetUtils.initFileSet(datasetEnum.name(),fileSets);
                //遍历输入目录，对所有项目进行两两跨项目缺陷预测
                for(File s: fileSets){
                    ArrayList<File> remaining =(ArrayList<File>)fileSets.clone();
                    remaining.remove(s);
                    for(File t:remaining){
                        logger.info(s.getName()+"-->"+t.getName());
                        predictOnDataset(s, t,datasetEnum,bufferedWriter);
                    }
                }

                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 在特定的数据集上进行缺陷预测
     * @param sourceFile    源项目名称
     * @param targetFile    目标项目名称
     * @param datasetEnum   数据集名称
     * @param bufferedWriter    保存评估结果
     */
    private static void predictOnDataset(File sourceFile, File targetFile, DatasetEnum datasetEnum,BufferedWriter bufferedWriter) {
        try {
            switch (datasetEnum){
                case Relink:
                    //遍历Relink数据中两个类别的属性
                    for(CategoryEnums categoryEnum: new CategoryEnums[]{CategoryEnums.Relink_ComplexityMetric, CategoryEnums.Relink_CountMetric}){
                        predictOnCategory(sourceFile, targetFile, categoryEnum,bufferedWriter);
                    }
                    break;
                case PROMISE:
                    //遍历AEEEM数据集上的各个类别的属性
                    logger.info("arrive at here.........");
                    break;
                default:
                    logger.info("未知的数据集");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在制定的数据集上的特定类型属性上做跨项目缺陷预测
     * @param sourceFile    源项目实例
     * @param targetFile    目标项目实例
     * @param categoryEnum  类别枚举
     * @throws Exception
     */
    private static void predictOnCategory(File sourceFile, File targetFile,
                                          CategoryEnums categoryEnum, BufferedWriter bufferedWriter) throws Exception {
        Instances sourceInstance= ConverterUtils.DataSource.read(sourceFile.toString());
        Instances targetInstance= ConverterUtils.DataSource.read(targetFile.toString());

        ArrayList<Integer> toDelete=new ArrayList<Integer>();
        //除了类标以外，逐个扫描该分类下的属性属于的列标号，把不属于当前类的特征列标标记出来
        //logger.info("当前类别："+categoryEnum.name()+" 当前保存的类别有:");
        for(int i=0;i<sourceInstance.numAttributes()-1;i++){
            if(!cateEnumToMembers.get(categoryEnum).contains(sourceInstance.attribute(i).name().trim())){
                toDelete.add(i);
            }/*else{
                logger.info(sourceInstance.attribute(i).name());
            }*/
        }

        sourceInstance.setClassIndex(sourceInstance.numAttributes()-1);
        targetInstance.setClassIndex(targetInstance.numAttributes()-1);

        //使用过滤器过滤到不需要的属性
        Remove remove = new Remove();
        remove.setAttributeIndicesArray(CollectionUtils.arrayListToInt(toDelete));//删除不必要的属性
        remove.setInputFormat(sourceInstance);
        Instances filteredSourceInstance= Filter.useFilter(sourceInstance, remove);
        Instances filteredTargetInstance=Filter.useFilter(targetInstance,remove);

        int runs=10;
        int numFolds=2;
        for(int i=0;i<runs;i++){

            int seedSource=i+1;
            int seedTarget=2*i+1;

            Instances curSource=new Instances(filteredSourceInstance);
            Random rand = new Random(seedSource);
            curSource.randomize(rand);
            if (curSource.classAttribute().isNominal()) {
                curSource.stratify(numFolds);
            }

            Instances curTarget=new Instances(filteredTargetInstance);
            Random randTar = new Random(seedTarget);
            curTarget.randomize(randTar);
            if (curTarget.classAttribute().isNominal()) {
                curTarget.stratify(numFolds);
            }

            //crossvalidation multipleruns
            Evaluation evaluation = new Evaluation(curSource);
            for(int n=0;n<numFolds;n++){
                Instances train=curSource.trainCV(numFolds,n);
                Instances test=curTarget.testCV(numFolds,n);
//                String[] option= Utils.splitOptions("-S 0 -B -1.0 ");
//                LibLINEAR libLINEAR=new LibLINEAR();
                NaiveBayes naiveBayes=new NaiveBayes();
//                libLINEAR.setOptions(option);
//                libLINEAR.buildClassifier(train);
                naiveBayes.buildClassifier(train);
//                evaluation.evaluateModel(libLINEAR,test);
                evaluation.evaluateModel(naiveBayes,test);
            }
            //保存评估结果
            saveEvaluation(i,sourceFile.getName(),targetFile.getName(),categoryEnum,evaluation,bufferedWriter);
        }//end for

    }

    /**
     * 保存评估的结果
     */
    private static void saveEvaluation(int runs, String sourceName, String targetName, CategoryEnums categoryEnum, Evaluation evaluation, BufferedWriter bufferedWriter){
        try {
            bufferedWriter.write(runs+","+sourceName+","+targetName+","
                    +categoryEnum.name()+","+evaluation.weightedFMeasure()+","
                    +evaluation.fMeasure(0)+","+evaluation.fMeasure(1)+","
                    +evaluation.weightedAreaUnderROC()+","
                    +evaluation.weightedPrecision() +","
                    +evaluation.weightedRecall()+"\r\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
