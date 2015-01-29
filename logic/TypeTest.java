package com.lux.trump.logic;

public class TypeTest{
	// a for base card
	// b for hand card
	// c for play card	
	static String[] b1 = {"H5", "H5", "H7", "H7", "C5", "C5", "B", "L"};
	static String[] a1 = {"CA"};
	static String[] c1 = {"C5"};
	static String[] c12 = {"H5"};
	
	static String[] a2 = {"CA", "CA"};
	static String[] b2 = {"H5", "H5", "H7", "H7", "C5", "C5", "B", "L"};
	static String[] c2 = {"C5", "C5"};
	static String[] c22 = {"C5", "C6"};
	
	static String[] a3 = {"L", "L", "B", "B"};
	static String[] b3 = {"D5", "D5", "D7", "D8", "D7"};
	static String[] c3 = {"D5", "D5", "D7", "D7"};
	static String[] c32 = {"D5", "D5", "D7", "D8"};

	static String[] a4 = {"L", "L", "B", "B"};
	static String[] b4 = {"D5", "D5", "D8", "D8", "D7"};
	static String[] c4 = {"D5", "D5", "D8", "D7"};
	
	static String[] a5 = {"L", "L", "B", "B", "DK", "DK", "H6"};
	static String[] b5 = {"D5", "D5", "D8", "D8", "D7" ,"D8", "H3", "H4"};
	static String[] b52 = {"D5", "D5", "D8", "D2", "D7" ,"D8", "D3", "D4"};
	static String[] c5 = {"D5", "D5", "D8", "D8", "D7" ,"D8", "H3"};
	static String[] c52 = {"D5", "D5", "D8", "D8", "D7" ,"H4", "H3"};
	static String[] c53 = {"D5", "D5", "D8", "D2", "D7" ,"D4", "D3"};
	
	static String[] a6 = {"CA"};
	static String[] b6 = {"CA", "DA", "HA", "C2", "L", "B", "C3", "D3", "H3", "S3"};
	static String[] c6 = {"C2"};
	static String[] c62 = {"C3"};
	
	static String[] a7 = {"CA", "HA"};
	static String[] c7 = {"C2", "H2"};
	static String[] c72 = {"C3", "H3"};
	static String[] c73 = {"C2", "H3"};
	
	static String[] a8 = {"C4", "D4", "H4", "S4"};
	static String[] c8 = {"CA", "DA", "HA", "SA"};
	static String[] c82 = {"L", "B"};
	
  	public static void main(String[] args){
		testDDZ();
		testSJ();
 	}

 	static void testDDZ(){
 		String filename = "D:/code/trumponline/trumponline/rule/ddz.xml";
		XmlRule xmlRule = new XmlRule(filename, "D", "6");
		RuleInfoDTO ruleInfoDTO = new RuleInfoDTO();
		xmlRule.initlizeRuleInfo(ruleInfoDTO);
		
		String [] card = { "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "CJ", "CQ", "CK", "CA"};
		int [] result = ruleInfoDTO.getcardpriority(card);
		for (int i:result){
			System.out.print(i+" ");
		}
		System.out.println();
		CheckTypeLegal checkTypeLegal = new CheckTypeLegal(ruleInfoDTO);
		
		testDDZcheck(checkTypeLegal);
		CardComparator comparator = new CardComparator(ruleInfoDTO);
		testDDZcomparator(comparator);
	//	System.out.println(CardType.getType(comparator.getcardpriority(c8), comparator.cardtype).name);
 	}
 	
	static void testDDZcheck(CheckTypeLegal checkTypeLegal) {
		System.out.println(checkTypeLegal.check(a6, b6, c6));
		System.out.println(checkTypeLegal.check(a6, b6, a6));
		
		System.out.println(checkTypeLegal.check(a7, b6, c7));
		System.out.println(checkTypeLegal.check(a7, b6, c72));
		System.out.println(checkTypeLegal.check(a7, b6, c73));
		
		System.out.println(checkTypeLegal.check(a7, b6, c8));
		System.out.println(checkTypeLegal.check(a7, b6, c82));
		System.out.println(checkTypeLegal.check(c82, b6, c8));
		
	}

	static void testDDZcomparator(CardComparator cardComparator){
		System.out.println(cardComparator.isBigThan(a6, a6, c6));
		System.out.println(cardComparator.isBigThan(a6, c6, a6));
		System.out.println(cardComparator.isBigThan(a6, a6, c62));
		System.out.println("------------");
		
		System.out.println(cardComparator.isBigThan(a7, a7, c7));
		System.out.println(cardComparator.isBigThan(a7, a7, c72));
		System.out.println(cardComparator.isBigThan(a7, a7, c73));
		System.out.println("------------");
		
		System.out.println(cardComparator.isBigThan(a8, a8, c8));
		System.out.println(cardComparator.isBigThan(a8, a8, c82));
		System.out.println(cardComparator.isBigThan(a8, c82, c8));
		System.out.println("------------");
		
		System.out.println(cardComparator.isBigThan(a6, a6, c8));
		System.out.println(cardComparator.isBigThan(a7, a7, c8));
		System.out.println(cardComparator.isBigThan(a6, a6, c82));
		System.out.println(cardComparator.isBigThan(a7, a7, c82));
		System.out.println(cardComparator.isBigThan(a6, c8, a6));
		System.out.println(cardComparator.isBigThan(a7, c8, a7));
		System.out.println(cardComparator.isBigThan(a6, c82, a6));
		System.out.println(cardComparator.isBigThan(a7, c82, a7));
		System.out.println("------------");
	}
	
 	static void testSJ(){
 		String filename = "D:/code/trumponline/trumponline/rule/shengji.xml";
		XmlRule xmlRule = new XmlRule(filename, "D", "6");
		RuleInfoDTO ruleInfoDTO = new RuleInfoDTO();
		xmlRule.initlizeRuleInfo(ruleInfoDTO);
		
		String [] card = { "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "CJ", "CQ", "CK", "CA"};
		int [] result = ruleInfoDTO.getcardpriority(card);
		for (int i:result){
			System.out.print(i+" ");
		}
		System.out.println();
		CheckTypeLegal checkTypeLegal = new CheckTypeLegal(ruleInfoDTO);
		
		testSJcheck(checkTypeLegal);
		CardComparator comparator = new CardComparator(ruleInfoDTO);
		//testSJComparator(comparator);
 	}
 	
	static void testSJcheck(CheckTypeLegal checkTypeLegal){

		boolean r = checkTypeLegal.check( a1, b1, c1);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a1, b1, c12);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check( a2, b2, c2);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a2, b2, c22);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a3, b3, c3);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check( a3, b3, c32);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a4, b4, c4);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check( a5, b5, c5);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a5, b5, c52);
		System.out.println(r + "\n-------");
		
		r = checkTypeLegal.check(a5, b52, c53);
		System.out.println(r + "\n-------");
	}
	static void testSJComparator(CardComparator comparator){
		String[] base = a2; // CA CA
		String[] base2 = {"CK", "CK", "CA"};
		String[] base3 = {"C5", "C5", "C7", "C7"};
		String[] base4 = {"C5", "C5", "C7", "C7", "CK", "CK", "CA"};
		String[] base5 = {"CK"};
		
		String[] a1 = {"CK", "CK"};
		String[] a2 = {"D2", "L"};
		String[] a3 = {"C5", "C5", "C7"};
		String[] a4 = {"CK", "CK", "CQ", "CQ"};
		String[] a5 = {"C2", "C2", "C3", "C3", "C4", "C4", "D8"};
		String[] a6 = {"CA"};
		
		String[] b2 = {"D5", "D5", "L"};
		String[] b1 = {"C10", "C10"};
		String[] b3 = {"D10", "D10", "DQ", "DQ"};
		String[] b4 = {"D10", "D10", "DJ", "DJ", "DK", "DQ", "L"};
		String[] b5 = {"C10"};
		
		String[] c1 = {"D2", "D2"};
		String[] c2 = {"D5", "D6", "D7"};
		String[] c3 = {"D10", "D10", "DJ", "DJ"};
  		String[] c4 = {"D10", "D10", "DJ", "DJ", "DK", "L", "L"};
  		String[] c5 = {"D4"};
  		
  		String[] d5 = {"HA"};
   		
		System.out.println(comparator.isBigThan(base, a1, base));
		System.out.println(comparator.isBigThan(base, base, a1));
		System.out.println(comparator.isBigThan(base, a1, b1));
		System.out.println(comparator.isBigThan(base, a1, c1));
		System.out.println(comparator.isBigThan(base, c1, a1));
		System.out.println(comparator.isBigThan(base, a1, a2));
		System.out.println(comparator.isBigThan(base, a2, a1));
		System.out.println("------");
		
		System.out.println(comparator.isBigThan(base2, a3, base2));
		System.out.println(comparator.isBigThan(base2, b2, base2));
		System.out.println(comparator.isBigThan(base2, c2, base2));
		System.out.println("------");
		
		System.out.println(comparator.isBigThan(base3, a4, base3));
		System.out.println(comparator.isBigThan(base3, b3, base3));
		System.out.println(comparator.isBigThan(base3, c3, base3));
		System.out.println("------");
		
		System.out.println(comparator.isBigThan(base4, a5, base4));
		System.out.println(comparator.isBigThan(base4, b4, base4));
		System.out.println(comparator.isBigThan(base4, c4, base4));
		System.out.println("------");

		System.out.println(comparator.isBigThan(base5, a6, base5));
		System.out.println(comparator.isBigThan(base5, b5, base5));
		System.out.println(comparator.isBigThan(base5, c5, base5));
		System.out.println(comparator.isBigThan(base5, d5, base5));
		System.out.println("------");
	}
}