package com.lux.trump.shared;

public class CardTypeTest{
	public static void test(){
		String[] test = {"{1}(a)F", "{1}(aa)F", "{1}(aaa)F", "{5}(a)S", "{2}(aaa)S(b)V", "{2}(aaa)S(bb)V", "{3}(aa)S"};
		String[] name = {"single", "double", "triple", "single_flush", "plane_1", "plane_2", "double_flush"};
		CardType[] cardtype = new CardType[7];
		for (int i=0; i<test.length; i++){
			cardtype[i] = new CardType(test[i], true, name[i]);
			System.out.println("type "+i+" :");
			for (int j=0; j<cardtype[i].itemlist.size();j++){
				System.out.println("	"+cardtype[i].itemlist.get(j).item + " " + cardtype[i].itemlist.get(j).type + " " + cardtype[i].itemlist.get(j).length);
			}
		}
		String[] s1 = {"S5"};
		System.out.println(getType(s1, cardtype));
		String[] s2 = {"S5", "P5"};
		System.out.println(getType(s2, cardtype));
		String[] s3 = {"S5", "S5", "P5"};
		System.out.println(getType(s3, cardtype));
		String[] s4 = {"S5", "P6", "S7", "S8", "P9", "S10", "PJ", "SQ", "SK", "SA"};
		System.out.println(getType(s4, cardtype));
		String[] s5 = {"S5", "S5", "P6", "P6", "D7", "D7", "C8", "C8"};
		System.out.println(getType(s5, cardtype));
		String[] s6 = {"S5", "S5", "P5", "S6", "S6", "C6", "P7", "CA"};
		System.out.println(getType(s6, cardtype));
	}
	
	public static String getType(String[] string, CardType[] cardtype){
		for (int i=0; i<cardtype.length; i++){
			if (cardtype[i].isMatch(string)){
				return cardtype[i].name;
			}
		}
		return "no such type";
	}
}

