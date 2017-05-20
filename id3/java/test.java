

public class test {

	public static void main(String[] args){
//		String filePath = "/Users/yananchen/Desktop/input.txt";
		 String filePath="/Users/yananchen/Documents/train2.txt";
		
		id3Process tool = new id3Process(filePath);
		tool.startBuildingTree(true);
	}
}
