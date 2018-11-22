package com.iset.nichao.utils;/**
 * Created by ChaoNi on 2017/3/21.
 * <p>
 * 功能说明：
 * 这个类首先读入ARFF文件，将该数据集中的每一个属性名称，根据定义的三个类别
 * 即：complexity ,count 和object oriented
 * 将数据集中的特征进行分组，并返回每一个属性在对应分组中的属性标号
 */

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;


public class MapFeatureToCatagory {
	private static Logger logger = Logger.getLogger(MapFeatureToCatagory.class.getName());

    /*---------------------------Relink数据集上特征分类------------------------------------*/
	//根据understand工具官网提供的指标信息，可以将所有的指标分为三类
	/**
	 * 复杂度相关的指标
	 */
	private static ArrayList<String> complexityMetric = null;
	/**
	 * 统计量相关的指标
	 */
	private static ArrayList<String> countMetric = null;
	/**
	 * 面向对象相关的
	 */
	private static ArrayList<String> objectOrientedMetric = null;
	/**
	 * 类别索引到类别名称的映射
	 */
	private static HashMap<CategoryEnums, String> cateIndexToName = null;
	/**
	 * 类别编号->类别成员
	 */
	private static HashMap<CategoryEnums, ArrayList<String>> cateEnumToMembers = null;


	/**
	 * 数据集的枚举到该数据集中含有的特征种类的映射
	 * Relink-->{Relink_ComplexityMetric,Relink_CountMetric,Relink_ObjectOrientedMetric}
	 */
	private static HashMap<DataSetEnum, ArrayList<CategoryEnums>> datasetToCategoryEnum = null;

	public static void main(String[] args) {
		initMetaCatagory();
		Test();

		// sort();
	}


	/**
	 * @return
	 */
	public static HashMap<CategoryEnums, ArrayList<String>> getCateEnumToMembers() {
		if (cateEnumToMembers == null) {
			initMetaCatagory();
		}
		return cateEnumToMembers;
	}

	public static HashMap<DataSetEnum, ArrayList<CategoryEnums>> getDatasetToCategoryEnum() {
		if (datasetToCategoryEnum == null) {
			initMetaCatagory();
		}
		return datasetToCategoryEnum;
	}



	/**
	 * 初始化Relink和AEEEM数据集指标类别的元数据信息
	 */
	private static void initMetaCatagory() {
		logger.info("Init the Relink metadata.");
		//初始化类别索引到类别名称的映射
		cateIndexToName = new HashMap<CategoryEnums, String>();
		cateIndexToName.put(CategoryEnums.Relink_ComplexityMetric, "ComplexityMetric");
		cateIndexToName.put(CategoryEnums.Relink_CountMetric, "CountMetric");
		cateIndexToName.put(CategoryEnums.Relink_ObjectOrientedMetric, "ObjectOrientedMetric");


		//初始化复杂度相关的指标信息
		complexityMetric = new ArrayList<String>();
		complexityMetric.add("AvgCyclomatic");
		complexityMetric.add("AvgCyclomaticModified");
		complexityMetric.add("AvgCyclomaticStrict");
		complexityMetric.add("AvgEssential");
		complexityMetric.add("AvgEssentialStrictModified");
		complexityMetric.add("CountPath");
		complexityMetric.add("Cyclomatic");
		complexityMetric.add("CyclomaticModified");
		complexityMetric.add("CyclomaticStrict");
		complexityMetric.add("Essential");
		complexityMetric.add("EssentialStrictModified");
		complexityMetric.add("Knots");
		complexityMetric.add("MaxCyclomatic");
		complexityMetric.add("MaxCyclomaticModified");
		complexityMetric.add("MaxCyclomaticStrict");
		complexityMetric.add("MaxEssential");
		complexityMetric.add("MaxEssentialKnots");
		complexityMetric.add("MaxEssentialStrictModified");
		complexityMetric.add("MaxInheritanceTree");
		complexityMetric.add("MaxNesting");
		complexityMetric.add("MinEssentialKnots");
		complexityMetric.add("RatioCommentToCode");
		complexityMetric.add("SumCyclomatic");
		complexityMetric.add("SumCyclomaticModified");
		complexityMetric.add("SumCyclomaticStrict");
		complexityMetric.add("SumEssential");
		complexityMetric.add("SumEssentialStrictModified");

		//初始化统计量相关的指标
		countMetric = new ArrayList<String>();
		countMetric.add("AltAvgLineBlank");
		countMetric.add("AltAvgLineCode");
		countMetric.add("AltAvgLineComment");
		countMetric.add("AltCountLineBlank");
		countMetric.add("AltCountLineCode");
		countMetric.add("AltCountLineComment");
		countMetric.add("AvgLine");
		countMetric.add("AvgLineBlank");
		countMetric.add("AvgLineCode");
		countMetric.add("AvgLineComment");
		countMetric.add("CountDeclFile");
		countMetric.add("CountDeclFunction");
		countMetric.add("CountDeclInstanceVariableInternal");
		countMetric.add("CountDeclInstanceVariableProtectedInternal");
		countMetric.add("CountDeclMethodFriend");
		countMetric.add("CountDeclMethodInternal");
		countMetric.add("CountDeclMethodProtectedInternal");
		countMetric.add("CountInput");
		countMetric.add("CountLine");
		countMetric.add("CountLineBlank");
		countMetric.add("CountLineBlank_Html");
		countMetric.add("CountLineBlank_Javascript");
		countMetric.add("CountLineBlank_Php");
		countMetric.add("CountLineCode");
		countMetric.add("CountLineCodeDecl");
		countMetric.add("CountLineCodeExe");
		countMetric.add("CountLineCode_Javascript");
		countMetric.add("CountLineCode_Php");
		countMetric.add("CountLineComment");
		countMetric.add("CountLineComment_Html");
		countMetric.add("CountLineComment_Javascript");
		countMetric.add("CountLineComment_Php");
		countMetric.add("CountLineInactive");
		countMetric.add("CountLinePreprocessor");
		countMetric.add("CountLine_Html");
		countMetric.add("CountLine_Javascript");
		countMetric.add("CountLine_Php");
		countMetric.add("CountSemicolon");
		countMetric.add("CountStmt");
		countMetric.add("CountStmtDecl");
		countMetric.add("CountStmtDecl_Javascript");
		countMetric.add("CountStmtDecl_Php");
		countMetric.add("CountStmtEmpty");
		countMetric.add("CountStmtExe");
		countMetric.add("CountStmtExe_Javascript");
		countMetric.add("CountStmtExe_Php");
		countMetric.add("RatioCommentToCode");

		//初始化面向对象的指标
		objectOrientedMetric = new ArrayList<String>();
		objectOrientedMetric.add("CountClassBase");
		objectOrientedMetric.add("CountClassCoupled");
		objectOrientedMetric.add("CountClassDerived");
		objectOrientedMetric.add("CountDeclClass");
		objectOrientedMetric.add("CountDeclClassMethod");
		objectOrientedMetric.add("CountDeclClassVariable");
		objectOrientedMetric.add("CountDeclFunction");
		objectOrientedMetric.add("CountDeclInstanceMethod");
		objectOrientedMetric.add("CountDeclInstanceVariable");
		objectOrientedMetric.add("CountDeclInstanceVariablePrivate");
		objectOrientedMetric.add("CountDeclInstanceVariableProtected");
		objectOrientedMetric.add("CountDeclInstanceVariablePublic");
		objectOrientedMetric.add("CountDeclMethod");
		objectOrientedMetric.add("CountDeclMethodAll");
		objectOrientedMetric.add("CountDeclMethodConst");
		objectOrientedMetric.add("CountDeclMethodDefault");
		objectOrientedMetric.add("CountDeclMethodFriend");
		objectOrientedMetric.add("CountDeclMethodPrivate");
		objectOrientedMetric.add("CountDeclMethodProtected");
		objectOrientedMetric.add("CountDeclMethodPublic");
		objectOrientedMetric.add("CountDeclMethodStrictPrivate");
		objectOrientedMetric.add("CountDeclMethodStrictPublished");
		objectOrientedMetric.add("CountDeclModule");
		objectOrientedMetric.add("CountDeclProgUnit");
		objectOrientedMetric.add("CountDeclSubprogram");
		objectOrientedMetric.add("CountOutput");
		objectOrientedMetric.add("CountPackageCoupled");
		objectOrientedMetric.add("MaxInheritanceTree");
		objectOrientedMetric.add("PercentLackOfCohesion");


		cateEnumToMembers = new HashMap<CategoryEnums, ArrayList<String>>();
		cateEnumToMembers.put(CategoryEnums.Relink_ComplexityMetric, complexityMetric);
		cateEnumToMembers.put(CategoryEnums.Relink_CountMetric, countMetric);
		cateEnumToMembers.put(CategoryEnums.Relink_ObjectOrientedMetric, objectOrientedMetric);


		datasetToCategoryEnum = new HashMap<DataSetEnum, ArrayList<CategoryEnums>>();
		ArrayList<CategoryEnums> relinkCategoryEnums = new ArrayList<CategoryEnums>();
		relinkCategoryEnums.add(CategoryEnums.Relink_ComplexityMetric);
		relinkCategoryEnums.add(CategoryEnums.Relink_CountMetric);
		relinkCategoryEnums.add(CategoryEnums.Relink_ObjectOrientedMetric);
		datasetToCategoryEnum.put(DataSetEnum.Relink, relinkCategoryEnums);
		logger.info("init relink finished.");


	}


	public static void Test() {
		try {
			Instances instances = ConverterUtils.DataSource.read("/home/jacknichao/datasets/Relink/Apache.arff");
			//包含类标属性的特征总数
			int metricCount = instances.numAttributes();

			for (int i = 0; i < metricCount - 1; i++) {
				CategoryEnums cateIndex = getCatagoryIndex(instances.attribute(i).name());
				if (cateIndex == null) {
					logger.warning("Unknown catagory index");
				}
//				logger.info(indexToCategroyName(cateIndex));
				System.out.println(indexToCategroyName(cateIndex));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * @param metricName 特征的名称
	 * @return 返回这个特征所属的类别编号
	 */
	public static CategoryEnums getCatagoryIndex(String metricName) {
		CategoryEnums cateIndex = null;

		for (CategoryEnums categoryEnums : CategoryEnums.values()) {
			ArrayList<String> members = getCateEnumToMembers().get(categoryEnums);
			if (members.contains(metricName)) {
				cateIndex = categoryEnums;
				break;
			}
		}
		if (cateIndex == null) {
			logger.info("未知的类别.");
			System.exit(1);
		}


		return cateIndex;
	}

	/**
	 * 类别的编码->类别的名称
	 *
	 * @param categoryEnum
	 * @return
	 */
	public static String indexToCategroyName(CategoryEnums categoryEnum) {
		if (cateIndexToName.containsKey(categoryEnum)) {
			return cateIndexToName.get(categoryEnum);
		} else {
			return "unknown";
		}
	}


	/**
	 * 根据数据集和属性的名字来找到该属性属于的类别
	 *
	 * @param dataSetEnum 数据集枚举
	 * @param featureName 当前特征的名字
	 * @return 这个特征对应的类别枚举
	 */
	public static ArrayList<CategoryEnums> getCategoryNameOfDataset(DataSetEnum dataSetEnum, String featureName) {
		ArrayList<CategoryEnums> results = new ArrayList<CategoryEnums>();

		ArrayList<CategoryEnums> categoryEnumArr = getDatasetToCategoryEnum().get(dataSetEnum);
		for (CategoryEnums category : categoryEnumArr) {

			ArrayList<String> members = cateEnumToMembers.get(category);
			if (members.contains(featureName.trim())) {
				results.add(category);
			}
		}
		return results;
	}

}
