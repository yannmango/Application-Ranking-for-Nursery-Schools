import java.util.ArrayList;

public class attrNode {

	private String attrName;
	private String parentAttrValue;
	private attrNode[] childAttrNode;
	private ArrayList<String> childDataIndex;
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getParentAttrValue() {
		return parentAttrValue;
	}
	public void setParentAttrValue(String parentAttrValue) {
		this.parentAttrValue = parentAttrValue;
	}
	public attrNode[] getChildAttrNode() {
		return childAttrNode;
	}
	public void setChildAttrNode(attrNode[] childAttrNode) {
		this.childAttrNode = childAttrNode;
	}
	public ArrayList<String> getChildDataIndex() {
		return childDataIndex;
	}
	public void setChildDataIndex(ArrayList<String> childDataIndex) {
		this.childDataIndex = childDataIndex;
	}
	
}
