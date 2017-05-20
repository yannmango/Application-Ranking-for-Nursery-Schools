
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayesTool {

    private String RE = "recommend";
    private String VR = "very_recom";
    private String SP = "spec_prior";
    private String PO = "priority";
    private String NR = "not_recom";

    ArrayList<String[]> reClassData = new ArrayList<>();
    ArrayList<String[]> vrClassData = new ArrayList<>();
    ArrayList<String[]> spClassData = new ArrayList<>();
    ArrayList<String[]> poClassData = new ArrayList<>();
    ArrayList<String[]> nrClassData = new ArrayList<>();

    
    private String filePath;
    private String testPath;
    // Array of attributes name
    private String[] attrNames;
    // training set
    private String[][] data;
    
    private HashMap<String, ArrayList<String>> attrValue;
    HashMap<String, String> testMap = new HashMap<String, String>();

    public static void main(String[] args) {
        
        String filePath = "/Users/haoyuepan/Desktop/656dataset/1/train6.txt";
        String testPath = "/Users/haoyuepan/Desktop/656dataset/1/test6.txt";
        NaiveBayesTool tool = new NaiveBayesTool(filePath, testPath);

        tool.calcuCorrect();
    }

    public NaiveBayesTool(String filePath, String testPath) {
        this.filePath = filePath;
        this.testPath = testPath;

        readDataFile();
        saveTestFile();
        initAttrValue();
        saveClass();

    }

    private void readDataFile() {
        File file = new File(filePath);
        ArrayList<String[]> dataArray = new ArrayList<String[]>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            String[] tempArray;
            while ((str = in.readLine()) != null) {
                tempArray = str.split(",");
                dataArray.add(tempArray);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        data = new String[dataArray.size()][];
        dataArray.toArray(data);
        attrNames = data[0];

    }

    private void saveTestFile() {
        File file = new File(testPath);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            String[] tempArray;
            while ((str = in.readLine()) != null) {
                ArrayList<String[]> dataArray = new ArrayList<String[]>();
                tempArray = str.split(",");
                String tempAttr = tempArray[0] + "," + tempArray[1] + ","
                        + tempArray[2] + "," + tempArray[3];
                   
                String tempClass = tempArray[4];
                testMap.put(tempAttr, tempClass);
            }
//            System.out.println(testMap);
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public int a = 0;

    private void calcuCorrect() {
        for (String temp : testMap.keySet()) {
            String org = testMap.get(temp);
            String act = naiveBayesClassificate(temp);

            if (org.equals(act)) {
                a++;
            }
        }
        System.out.println((double) a / 100);
    }

    
    private void initAttrValue() {
        attrValue = new HashMap<>();
        ArrayList<String> tempValues;

        
        for (int j = 1; j < attrNames.length; j++) {
            
            tempValues = new ArrayList<>();
            for (int i = 1; i < data.length; i++) {
                if (!tempValues.contains(data[i][j])) {
                    
                    tempValues.add(data[i][j]);
                }
            }

            attrValue.put(data[0][j], tempValues);
        }

    }

    private void saveClass() {

        
        for (int i = 0; i < data.length; i++) {
           
            if (data[i][attrNames.length - 1].equals(RE)) {
                reClassData.add(data[i]);
            } else if (data[i][attrNames.length - 1].equals(VR)) {
                vrClassData.add(data[i]);
            } else if (data[i][attrNames.length - 1].equals(SP)) {
                spClassData.add(data[i]);
            } else if (data[i][attrNames.length - 1].equals(PO)) {
                poClassData.add(data[i]);
            } else if (data[i][attrNames.length - 1].equals(NR)) {
                nrClassData.add(data[i]);

            }
        }
    }

    private double computeConditionProbably(String condition, String classType, int currentClass) {
        
        ArrayList<String[]> classData = new ArrayList<>();
        int attrIndex;
        int count = 0;
        if (classType.equals(RE)) {
            classData = reClassData;
        } else if (classType.equals(VR)) {
            classData = vrClassData;
        } else if (classType.equals(SP)) {
            classData = spClassData;
        } else if (classType.equals(PO)) {
            classData = poClassData;
        } else if (classType.equals(NR)) {
            classData = nrClassData;
        }

        
        if (condition == null) {
            return 1.0 * classData.size() / (data.length - 1);
        }

      
        attrIndex = currentClass;

        for (String[] s : classData) {
            if (s[attrIndex].equals(condition)) {
                count++;
            }
        }

        return 1.0 * count / classData.size();
    }

    private double computeConditionProbably(String classType) {
       
        ArrayList<String[]> classData = new ArrayList<>();

        if (classType.equals(RE)) {
            classData = reClassData;
        } else if (classType.equals(VR)) {
            classData = vrClassData;
        } else if (classType.equals(SP)) {
            classData = spClassData;
        } else if (classType.equals(PO)) {
            classData = poClassData;
        } else {
            classData = nrClassData;
        }

       
        return 1.0 * classData.size() / (data.length - 1);

    }

    public String naiveBayesClassificate(String data) {
        
        String[] dataFeatures;

        dataFeatures = data.split(",");
        double xWhenRE = 1.0;
        double xWhenVR = 1.0;
        double xWhenSP = 1.0;
        double xWhenPO = 1.0;
        double xWhenNR = 1.0;

        double pRE = 1;
        double pVR = 1;
        double pSP = 1;
        double pPO = 1;
        double pNR = 1;
        for (int i = 0; i < dataFeatures.length; i++) {
            
            xWhenRE *= computeConditionProbably(dataFeatures[i], RE, i);
            xWhenVR *= computeConditionProbably(dataFeatures[i], VR, i);
            xWhenSP *= computeConditionProbably(dataFeatures[i], SP, i);
            xWhenPO *= computeConditionProbably(dataFeatures[i], PO, i);
            xWhenNR *= computeConditionProbably(dataFeatures[i], NR, i);

        }

        pRE = xWhenRE * computeConditionProbably(RE);
        pVR = xWhenVR * computeConditionProbably(VR);
        pSP = xWhenSP * computeConditionProbably(SP);
        pPO = xWhenPO * computeConditionProbably(PO);
        pNR = xWhenNR * computeConditionProbably(NR);

        return (findMax(pRE, pVR, pSP, pPO, pNR));
    }
    String returnValue;

    private String findMax(double pRE, double pVR, double pSP, double pPO, double pNR) {
        ArrayList<Double> max = new ArrayList<>();

        max.add(pRE);
        max.add(pVR);
        max.add(pSP);
        max.add(pPO);
        max.add(pNR);

        double maxP = max.get(0);
        for (int i = 1; i <= 4; i++) {
            if (maxP < max.get(i)) {
                maxP = max.get(i);
            }
        }

        if (maxP == pRE) {
            returnValue = RE;
        } else if (maxP == pVR) {
            returnValue = VR;
        } else if (maxP == pSP) {
            returnValue = SP;
        } else if (maxP == pPO) {
            returnValue = PO;
        } else {
            returnValue = NR;
        }
        return (returnValue);

    }

}
