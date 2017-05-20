import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



public class id3Process {

	// class type names
	private final String[] classType={"not_recom","recommend","very_recom","priority","spec_prior"};
	
	private int attrNum;
	private String trainFilePath;
	private String testFilePath;
	private String[][] trainData;
	private String[][] testData;
	private String[][] data;
	private String[] attrNames;
	private HashMap<String,ArrayList<String>> attrValue;
	
//	public id3Process(String trainFilePath,String testFilePath){
//		this.trainFilePath=trainFilePath;
//		this.testFilePath=testFilePath;
//		attrValue=new HashMap<>();
//	}
	public id3Process(String trainFilePath){
		this.trainFilePath=trainFilePath;
		
		attrValue=new HashMap<>();
	}
	
	//read data from file
	private void readDataFile(){
		File file=new File(trainFilePath);
		ArrayList<String[]> dataArray=new ArrayList<String[]>();
		
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while((str=in.readLine())!=null){
				tempArray=str.split(",");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = new String[dataArray.size()][];
		dataArray.toArray(data);
		attrNum = data[0].length;
		attrNames = data[0];
		
	}
	
//	public void getAllData(String trainDataPath,String testDataPath){
//		ArrayList<String[]> data1=readDataFromFile(trainDataPath);
//		ArrayList<String[]> data2=readDataFromFile(testDataPath);
//		trainData=new String[data1.size()][];
//		data1.toArray(trainData);
//		attrNum=trainData[0].length;
//		attrNames=trainData[0];
//		testData=new String[data2.size()][];
//		data2.toArray(testData);
//		
//	}
	
	private void initAttrValue(){
		ArrayList<String> tempValues;
		for(int j=1;j<attrNum;j++){
			tempValues=new ArrayList<>();
			for(int i=1;i<data.length;i++){
				if(!tempValues.contains(data[i][j])){
					tempValues.add(data[i][j]);
				}
			}
			attrValue.put(data[0][j], tempValues);
		}
	}
	
	private double computeEntropy(String[][] remainData,String attrName,String value,boolean isParent){
		int total=0;
		int[] classCount=new int[classType.length];
		Arrays.fill(classCount, 0);
		double[] probal=new double[classType.length];
		Arrays.fill(probal, 0);
		double entropyValue=0;
		for(int j=1;j<attrNames.length;j++){
			for(int i=1;i<remainData.length;i++){
				if(isParent
						||(!isParent&&remainData[i][j].equals(value))){
					for(int k=0;k<classType.length;k++){
						if(remainData[i][attrNames.length-1].equals(classType[k])){
							classCount[k]++;
						}
						
					}
					
					
				}
			}
		}
		for(int m=0;m<classType.length;m++){
			total+=classCount[m];
		}
		for(int n=0;n<classType.length;n++){
			probal[n]=classCount[n]/total;
			if(probal[n]==1||probal[n]==0){
				return 0;
			}
			entropyValue+=-probal[n]*Math.log(probal[n])/Math.log(2.0);
		}
		return entropyValue;
		
	}
	private double computeGain(String[][] remainData,String value){
		double gainValue=0;;
		double entropyOri=0;
		double childEntropySum=0;
		int childValueNum=0;
		ArrayList<String> attrTypes=attrValue.get(value);
		HashMap<String,Integer> ratioValues=new HashMap<>();
		for(int i=0;i<attrTypes.size();i++){
			ratioValues.put(attrTypes.get(i), 0);
		}
		for(int j=1;j<attrNames.length;j++){
			if(value.equals(attrNames[j])){
				for(int i=1;i<=remainData.length-1;i++){
					childValueNum=ratioValues.get(remainData[i][j]);
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
			}
		}	
		entropyOri=computeEntropy(remainData,value,null,true);
		for(int i=0;i<attrTypes.size();i++){
			double ratio=(double) ratioValues.get(attrTypes.get(i))
					/(remainData.length-1);
			childEntropySum+=ratio
					*computeEntropy(remainData,value,attrTypes.get(i),false);
		}
		gainValue=entropyOri-childEntropySum;
		return gainValue;
	}
	
	private double computeGainRatio(String[][] remainData,String value){
		double gain=0;
		double splitInfo=0;
		int childValueNum=0;
		ArrayList<String> attrTypes=attrValue.get(value);
		HashMap<String,Integer> ratioValues=new HashMap<>();
		for(int i=0;i<attrTypes.size();i++){
			ratioValues.put(attrTypes.get(i), 0);
		}
		for(int j=1;j<attrNames.length;j++){
			if(value.equals(attrNames[j])){
				for(int i=1;i<=remainData.length-1;i++){
					childValueNum=ratioValues.get(remainData[i][j]);
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
				
			}
		}
		gain=computeGain(remainData,value);
		for(int i=0;i<attrTypes.size();i++){
			double ratio=(double) ratioValues.get(attrTypes.get(i))
					/(remainData.length-1);
			splitInfo+=-ratio*Math.log(ratio)/Math.log(2.0);
		}
		return gain/splitInfo;
	}
	
	private void buildDecisionTree(attrNode node,String parentAttrValue,
			String[][] remainData,ArrayList<String> remainAttr,boolean isID3){
		node.setParentAttrValue(parentAttrValue);
		String attrName="";
		double gainValue=0;
		double tempValue=0;
		
		if(remainAttr.size()==1){
			System.out.println("attr null");
			return;
		}
		for(int i=0;i<remainAttr.size();i++){
			if(isID3){
				tempValue=computeGain(remainData,remainAttr.get(i));
			}else{
				tempValue=computeGainRatio(remainData,remainAttr.get(i));
			}
			if(tempValue>gainValue){
				gainValue=tempValue;
				attrName=remainAttr.get(i);
			}
		}
		node.setAttrName(attrName);
		ArrayList<String> valueTypes = attrValue.get(attrName);
		remainAttr.remove(attrName);

		attrNode[] childNode = new attrNode[valueTypes.size()];
		String[][] rData;
		for (int i = 0; i < valueTypes.size(); i++){
			rData = removeData(remainData, attrName, valueTypes.get(i));

			childNode[i] = new attrNode();
			boolean sameClass = true;
			ArrayList<String> indexArray = new ArrayList<>();
			for (int k = 1; k < rData.length; k++){
				indexArray.add(rData[k][0]);
				if (!rData[k][attrNames.length - 1]
						.equals(rData[1][attrNames.length - 1])){
					sameClass = false;
					break;
				}
			}
			if (!sameClass) {
				ArrayList<String> rAttr = new ArrayList<>();
				for (String str : remainAttr) {
					rAttr.add(str);
			}
				buildDecisionTree(childNode[i], valueTypes.get(i), rData,
						rAttr, isID3);
		}else{
			childNode[i].setParentAttrValue(valueTypes.get(i));
			childNode[i].setChildDataIndex(indexArray);
		}
		}
		node.setChildAttrNode(childNode);
			
	}
	
		private String[][] removeData(String[][] srcData, String attrName,
				String valueType) {
			String[][] desDataArray;
			ArrayList<String[]> desData = new ArrayList<>();
			ArrayList<String[]> selectData = new ArrayList<>();
			selectData.add(attrNames);
			for (int i = 0; i < srcData.length; i++) {
				desData.add(srcData[i]);
			}
			for (int j = 1; j < attrNames.length; j++) {
				if (attrNames[j].equals(attrName)) {
					for (int i = 1; i < desData.size(); i++) {
						if (desData.get(i)[j].equals(valueType)) {
							selectData.add(desData.get(i));
						}
					}
				}
			}
			desDataArray = new String[selectData.size()][];
			selectData.toArray(desDataArray);

			return desDataArray;
		}
		
		public void startBuildingTree(boolean isID3) {
			readDataFile();
			initAttrValue();

			ArrayList<String> remainAttr = new ArrayList<>();
			
			for (int i = 1; i < attrNames.length - 1; i++) {
				remainAttr.add(attrNames[i]);
			}

			attrNode rootNode = new attrNode();
			buildDecisionTree(rootNode, "", data, remainAttr, isID3);
			showDecisionTree(rootNode, 1);
		}
		private void showDecisionTree(attrNode node, int blankNum) {
			System.out.println();
			for (int i = 0; i < blankNum; i++) {
				System.out.print("\t");
			}
			System.out.print("--");
			
			if (node.getParentAttrValue() != null
					&& node.getParentAttrValue().length() > 0) {
				System.out.print(node.getParentAttrValue());
			} else {
				System.out.print("--");
			}
			System.out.print("--");

			if (node.getChildDataIndex() != null
					&& node.getChildDataIndex().size() > 0) {
				String i = node.getChildDataIndex().get(0);
				System.out.print("class:"
						+ data[Integer.parseInt(i)][attrNames.length - 1]);
				System.out.print("[");
				for (String index : node.getChildDataIndex()) {
					System.out.print(index + ", ");
				}
				System.out.print("]");
			} else {
				
				System.out.print("【" + node.getAttrName() + "】");
				for (attrNode childNode : node.getChildAttrNode()) {
					showDecisionTree(childNode, 2 * blankNum);
				}
			}

		}
}
