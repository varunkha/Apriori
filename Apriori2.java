import java.util.*;
import java.io.*;
import java.net.URL;

class rule{
	Set head,body;
	int size_head,size_body,size_rule;
	rule(Set head, Set body, int size_head, int size_body, int size_rule){
		this.head=head;
		this.body=body;
		this.size_head=size_head;
		this.size_body=size_body;
		this.size_rule=size_rule;
	}
	@Override
	public boolean equals(Object object) {
		rule obj = (rule) object;
		if(obj.head.equals(this.head) && obj.body.equals(this.body)) return true;
		return false;
	}
	@Override
	public int hashCode() {
		return 2*head.hashCode()+body.hashCode();
	}
	
    @Override
    public String toString() {
        return head+"->"+body;
    }
}

public class Apriori2 {

	static String[][] inp;
	static ArrayList<Set<String>>[] itemset;
	static ArrayList<Integer>[] itemset_count;;
	static int minSupport,minConfidence;
	static HashSet<rule>[] rules;
		
	public static void main(String[] args) {
		// Auto-generated method stub
		ArrayList<rule> arr_rule,arr_rule2;
		Scanner kbd = new Scanner(System.in);
		System.out.println("Enter Support");
		minSupport = Integer.parseInt(kbd.nextLine());
		System.out.println("Enter Confidence");
		minConfidence = Integer.parseInt(kbd.nextLine());
		System.out.println();
		frequentItems(minSupport);
		System.out.println();
		generateRules(minConfidence);
		int option=0,part,count=0;
		int option2=0,part2,count2=0;
		String items,formula,items2;
		int logical=0;
		while(true){
			System.out.println("\nEnter formula");
			formula = kbd.nextLine();
			if(formula.contains(" OR ")) logical=0;
			else if(formula.contains(" AND ")) logical=1;
			String[] str_array1 = formula.split("OR|AND");
			if(str_array1.length==1){
				arr_rule = runTemplate(str_array1[0].trim());
				System.out.println("Count:"+arr_rule.size());
			}
			else{
				arr_rule = runTemplate(str_array1[0].trim());
				arr_rule2 = runTemplate(str_array1[1].trim());
				if(logical==0){
					//OR
					arr_rule.addAll(arr_rule2);
					HashSet<rule> hs = new HashSet<rule>();
					hs.addAll(arr_rule);
					//hs.addAll(arr_rule2);
					System.out.println("Count:"+hs.size());
				} else{
					//AND
					try{
						/*for(int i=0;i<arr_rule.size();i++) System.out.println(arr_rule.get(i));
						System.out.println("Rule2");
						for(int i=0;i<arr_rule.size();i++) System.out.println(arr_rule2.get(i));*/
						arr_rule.retainAll(arr_rule2);
					}catch(Exception e){
						arr_rule=new ArrayList<rule>();
					}
					System.out.println("Count:"+arr_rule.size());
				}
			}
		}
	}
	
	private static ArrayList<rule> runTemplate(String inp){
		int part,option=0,count=0;
		String items;
		String[] str_array1;
		inp=inp.trim();
		if(inp.startsWith("SIZE")){
			str_array1 = inp.split(" ");
			if(str_array1[2].equals("HEAD")) part=1;
			else if(str_array1[2].equals("BODY")) part=2;
			else if(str_array1[2].equals("RULE")) part=3;
			else return null;
			return calcTemplate2(part,Integer.parseInt(str_array1[4]));
		}
		else{
			str_array1 = inp.split("\\(");
			if(str_array1.length==1){
				str_array1 = str_array1[0].split(" ");
				if(str_array1[0].equals("HEAD")) part=1;
				else if(str_array1[0].equals("BODY")) part=2;
				else if(str_array1[0].equals("RULE")) part=3;
				else return null;
				if(str_array1[2].equals("ANY")) option=1;
				else if(str_array1[2].equals("NONE")) count=0;
				else count=Integer.parseInt(str_array1[2]);
				items=str_array1[4];
			}
			else{
				items = str_array1[1].replace(")", "");
				str_array1 = str_array1[0].split(" ");
				if(str_array1[0].equals("HEAD")) part=1;
				else if(str_array1[0].equals("BODY")) part=2;
				else if(str_array1[0].equals("RULE")) part=3;
				else return null;
				if(str_array1[2].equals("ANY")) option=1;
				else if(str_array1[2].equals("NONE")) count=0;
				else count=Integer.parseInt(str_array1[2]);
			}
			if(option==1) return calcTemplate1_Any(part,items);
			else return calcTemplate1(part,count,items);
		}
	
	}
	
	private static ArrayList<rule> calcTemplate1(int part, int count, String items) {
		// TODO Auto-generated method stub
		ArrayList<rule> arr_rule = new ArrayList<rule>();
		items = items.replaceAll("\\s", "");
		String items_array[] = items.split(",");
		
		int sum=0;
		Iterator it = rules[1].iterator();
		int n;
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1){
				n=0;
				for(String s : items_array)
					if(rule1.head.contains(s)) 
						n++;
				if(n==count) arr_rule.add(rule1); 
			}
			else if(part==2){
				n=0;
				for(String s : items_array)
					if(rule1.body.contains(s) && count==1) 
						n++;
				if(n==count) arr_rule.add(rule1); 
			}
			if(part==3){
				n=0;
				for(String s : items_array)
					if((rule1.body.contains(s) || rule1.head.contains(s)) && count==1) 
						n++;
				if(n==count) arr_rule.add(rule1); 
			}
		}
		it = rules[2].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1){
				n=0;
				for(String s : items_array)
					if(rule1.head.contains(s) && count==1) 
						n++;
				if(n==count) arr_rule.add(rule1);  
			}
			else if(part==2){
				n=0;
				for(String s : items_array)
					if(rule1.body.contains(s) && count==1) 
						n++;
				if(n==count) arr_rule.add(rule1); 
			}
			if(part==3){
				n=0;
				for(String s : items_array)
					if((rule1.body.contains(s) || rule1.head.contains(s)) && count==1) 
						n++;
				if(n==count) arr_rule.add(rule1); 
			}
		}
		return  arr_rule;
	}

	private static ArrayList<rule> calcTemplate1_Any(int part, String items) {
		// TODO Auto-generated method stub
		ArrayList<rule> arr_rule = new ArrayList<rule>();
		items = items.replaceAll("\\s", "");
		String items_array[] = items.split(",");
		
		int sum=0;
		Iterator it = rules[1].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1){
				for(String s : items_array)
					if(rule1.head.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
			else if(part==2){
				for(String s : items_array)
					if(rule1.body.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
			if(part==3){
				for(String s : items_array)
					if(rule1.head.contains(s)||rule1.body.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
		}
		it = rules[2].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1){
				for(String s : items_array)
					if(rule1.head.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
			else if(part==2){
				for(String s : items_array)
					if(rule1.body.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
			if(part==3){
				for(String s : items_array)
					if(rule1.head.contains(s)||rule1.body.contains(s)) {
						arr_rule.add(rule1);
						break;
					}
			}
		}
		return arr_rule;			
	}

	private static ArrayList<rule> calcTemplate2(int part, int count) {
		ArrayList<rule> arr_rule = new ArrayList<rule>();
		int sum=0;
		Iterator it = rules[1].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1 && rule1.size_head>=count) arr_rule.add(rule1);
			else if(part==2 && rule1.size_body>=count) arr_rule.add(rule1);
			if(part==3 && rule1.size_rule>=count) arr_rule.add(rule1);
		}
		it = rules[2].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			if(part==1 && rule1.size_head>=count) arr_rule.add(rule1);
			else if(part==2 && rule1.size_body>=count) arr_rule.add(rule1);
			if(part==3 && rule1.size_rule>=count) arr_rule.add(rule1);
		}
		return arr_rule;	
	}

	private static void generateRules(int minConfidence2) {
		minConfidence = minConfidence2;
		rules = new LinkedHashSet[3];
		rules[0] = new LinkedHashSet<rule>();
		rules[1] = new LinkedHashSet<rule>();
		rules[2] = new LinkedHashSet<rule>();
		generateRules_2();
		generateRules_3();
	}
	
	private static void generateRules_2() {
		int support_total = 0, support1 = 0, support2 = 0, confidence1 = 0, confidence2 = 0;
		for(int i=0;i<itemset[2].size();i++){
			Set<String> s = itemset[2].get(i);
			String[] s_array = s.toArray(new String[0]);
			support_total = itemset_count[2].get(i);
			
			Set<String> head1 = new LinkedHashSet<String>();
			head1.add(s_array[0]);
			Set<String> body1 = new LinkedHashSet<String>();
			body1.add(s_array[1]);
			rule rule1 = new rule(head1,body1,1,1,2);
			Set<String> head2 = new LinkedHashSet<String>();
			head2.add(s_array[1]);
			Set<String> body2 = new LinkedHashSet<String>();
			body2.add(s_array[0]);
			rule rule2 = new rule(head2,body2,1,1,2);
			
			support1 = itemset_count[1].get(itemset[1].indexOf(head1));
			support2 = itemset_count[1].get(itemset[1].indexOf(head2));
			//System.out.println(support1 + ": " + support2 + ": " + support_total);
			confidence1 = support_total*100/support1;
			confidence2 = support_total*100/support2;
			
			if(confidence1>=minConfidence) rules[1].add(rule1);
			if(confidence2>=minConfidence) rules[1].add(rule2);
		}
		System.out.println("Confidence:" + minConfidence);
		System.out.println("Length of Rule:2");
		System.out.println("Count:"+rules[1].size());
		/*Iterator it = rules[1].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			System.out.println(rule1.head + " -> " + rule1.body);
		}*/
	}
	
	private static void generateRules_3() {
		int support_total = 0;
		int support1 = 0, support2 = 0, support3 = 0, support4 = 0, support5 = 0, support6 = 0;
		int confidence1 = 0, confidence2 = 0, confidence3 = 0, confidence4 = 0, confidence5 = 0, confidence6 = 0;
		for(int i=0;i<itemset[3].size();i++){
			Set<String> s = itemset[3].get(i);
			String[] s_array = s.toArray(new String[0]);
			Set<String> s1 = new LinkedHashSet<String>();
			Set<String> s2 = new LinkedHashSet<String>();
			Set<String> s3 = new LinkedHashSet<String>();
			Set<String> s4 = new LinkedHashSet<String>();
			Set<String> s5 = new LinkedHashSet<String>();
			Set<String> s6 = new LinkedHashSet<String>();
			s1.add(s_array[0]);
			s2.add(s_array[1]);
			s3.add(s_array[2]);
			s4.add(s_array[0]);
			s4.add(s_array[1]);
			s5.add(s_array[0]);
			s5.add(s_array[2]);
			s6.add(s_array[1]);
			s6.add(s_array[2]);
			support_total = itemset_count[3].get(i);
			support1 = itemset_count[1].get(itemset[1].indexOf(s1));
			support2 = itemset_count[1].get(itemset[1].indexOf(s2));
			support3 = itemset_count[1].get(itemset[1].indexOf(s3));
			support4 = itemset_count[2].get(itemset[2].indexOf(s4));
			support5 = itemset_count[2].get(itemset[2].indexOf(s5));
			support6 = itemset_count[2].get(itemset[2].indexOf(s6));
			confidence1 = support_total*100/support1;
			confidence2 = support_total*100/support2;
			confidence3 = support_total*100/support3;
			confidence4 = support_total*100/support4;
			confidence5 = support_total*100/support5;
			confidence6 = support_total*100/support6;
			rule rule1 = new rule(s1,s6,1,2,3);
			rule rule2 = new rule(s2,s5,1,2,3);
			rule rule3 = new rule(s3,s4,1,2,3);
			rule rule4 = new rule(s4,s3,2,1,3);
			rule rule5 = new rule(s5,s2,2,1,3);
			rule rule6 = new rule(s6,s1,2,1,3);
			if(confidence1>=minConfidence) rules[2].add(rule1);
			if(confidence2>=minConfidence) rules[2].add(rule2);
			if(confidence3>=minConfidence) rules[2].add(rule3);
			if(confidence4>=minConfidence) rules[2].add(rule4);
			if(confidence5>=minConfidence) rules[2].add(rule5);
			if(confidence6>=minConfidence) rules[2].add(rule6);
		}
		System.out.println("Length of Rule:3");
		System.out.println("Count:"+rules[2].size());
		/*Iterator it = rules[2].iterator();
		while(it.hasNext()) {
			rule rule1 = (rule) it.next();
			System.out.println(rule1.head + " -> " + rule1.body);
		}*/
	}
	
	public static void frequentItems(int minSupport2){
		System.out.println("Support:" + minSupport2);
		background();
		minSupport = minSupport2;
		set_length1();
		int n = set_length_n(2);
	}
	
	public static void background(){
		inp = new String[100][102];
		itemset = new ArrayList[102];
		itemset_count = new ArrayList[102];
		minSupport=50;
		
		for(int i=0;i<102;i++) {
			itemset[i] = new ArrayList<Set<String>>();
			itemset_count[i] = new ArrayList<Integer>();
		}
		
		try {
			URL path = Apriori2.class.getResource("gene_expression.txt");
			File f = new File(path.getFile());
			Scanner kbd = new Scanner(f);
			int i=0;
			while(kbd.hasNextLine()){
				if(i==100) break;
				inp[i] = kbd.nextLine().toUpperCase().split("\\s");
				for(int j=1;j<101;j++){
					inp[i][j] = "G" + j + "_" + inp[i][j]; 
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void set_length1(){
		String cmp;
		String cmp2;
		int up,down,all=0,aml=0,breast_cancer=0,colon_cancer=0;
		
		for(int i=1;i<102;i++){
			up=0;
			down=0;
			cmp = "G" + i + "_UP";
			cmp2 = "G" + i + "_DOWN";
			if(i==101) {
				for(int j=0;j<100;j++){
					if(inp[j][i].equals("ALL")) all++;
					else if(inp[j][i].equals("AML")) aml++;
					else if(inp[j][i].equals("BREAST_CANCER")) breast_cancer++;
					else if(inp[j][i].equals("COLON_CANCER")) colon_cancer++;
				}
				if(all>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add("ALL");
					itemset[1].add(s);
					itemset_count[1].add(all);
				}
				if(aml>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add("AML");
					itemset[1].add(s);
					itemset_count[1].add(aml);
				}
				if(breast_cancer>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add("BREAST_CANCER");
					itemset[1].add(s);
					itemset_count[1].add(breast_cancer);
				}
				if(up>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add("COLON_CANCER");
					itemset[1].add(s);
					itemset_count[1].add(colon_cancer);
				}
			}
			else {
				for(int j=0;j<100;j++){
					if(inp[j][i].equals(cmp)) up++;
					else down++;
				}
				if(up>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add(cmp);
					itemset[1].add(s);
					itemset_count[1].add(up);
				}
				if(down>=minSupport) {
					Set<String> s = new LinkedHashSet<String>();
					s.add(cmp2);
					itemset[1].add(s);
					itemset_count[1].add(down);
				}
			}
		}
	}
	
	public static int set_length_n(int n){
		System.out.println("Length of itemset:"+(n-1));
		System.out.println("itemset size:"+itemset[n-1].size());

		//for(int i=0;i<itemset[n-1].size();i++) System.out.println((Set) itemset[n-1].get(i));
		Set s;
		int count=0,count_intra=0;
		if(itemset[n-1].size()==0) return n-2;
		for(int i=0;i<itemset[n-1].size()-1;i++){
			for(int j=i+1;j<itemset[n-1].size();j++){
				s = new LinkedHashSet<String>();
				s.addAll((Set) itemset[n-1].get(i));
				s.addAll((Set) itemset[n-1].get(j));
				
				if(s.size()!=n) continue;
				count=0;
				for(int row=0;row<100;row++){
					count_intra=0;
					for(int column=1;column<102;column++){
						if(s.contains(inp[row][column])) count_intra++;
					}
					if(count_intra==n) count++;
				}
				if(count>=minSupport && !itemset[n].contains(s)) {
					itemset[n].add(s);
					itemset_count[n].add(count);
				}
			}
		}
		return set_length_n(n+1);
	}

}
