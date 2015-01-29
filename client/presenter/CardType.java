package com.lux.trump.client.presenter;



import java.util.ArrayList;


public class CardType {
  public ArrayList<TypeItem> itemlist = new ArrayList<TypeItem>();
  String name;
  boolean color_consistency;
  public CardType(String expression, boolean color_consistency, String name){
    this.name = name;
    this.color_consistency = color_consistency;
    char exp[] = expression.toCharArray();
    int i=0;
    
    // get the repeat times of each item
    String num = "";
    while (exp[++i] != '}'){
      num += exp[i];
    }
    i++;
    int n = Integer.valueOf(num).intValue();

    // get each item
    while (i<exp.length){
      i++;   // skip the '('
      String item = "";
      while (exp[i] != ')'){
        item += exp[i++];
      }
      i++;
      itemlist.add(new TypeItem(item, exp[i++], n));
    }
  }
  
  boolean isMatch(String[] card){
    int [] cardnum = getCardNum(card);
    
    return isPartMatch(cardnum, this.itemlist, -1);
  }
  
  private int[] getCardNum(String[] card) {
    int[] num = new int[card.length];
    for (int i=0; i<card.length; i++){
      String s = card[i].substring(1);
      switch (s){
        case "A":
          num[i] = 14;
          break;
        case "K":
          num[i] = 13;
          break;
        case "Q":
          num[i] = 12;
          break;
        case "J":
          num[i] = 11;
          break;
        case "10":
          num[i] = 10;
          break;
        default:
          num[i] = Integer.valueOf(s).intValue();
          break;
      }
      if (this.color_consistency == false){
        switch(card[i].substring(0, 1)) {
          case "S":
            num[i] += 0;
            break;
          case "H":
            num[i] += 13;
            break;
          case "C":
            num[i] += 26;
            break;
          case "D":
            num[i] += 39;
            break;
          default:
            break;
        }
      }
    }
    return num;
  }

  boolean isPartMatch(int[] card, ArrayList<TypeItem> list, int n){
    if (list.size() == 0 && card.length == 0)
      return true;
    if (list.size() == 0 && card.length != 0)
      return false;
    if (list.size() != 0 && card.length == 0)
      return false;
    
    int num = -1;
    for (int i=1; i<card.length+1; i++){
      int[] subCard = new int[i];
      for (int j=0; j<subCard.length; j++)
        subCard[j] = card[j];
      
      if (list.get(0).isMatch(subCard, n) != 0){
        num = list.get(0).isMatch(subCard, n);
        int[] lastcard = new int[card.length - i];
        for (int j=0; j<lastcard.length; j++){
          lastcard[j] = card[i+j];
        }
        @SuppressWarnings("unchecked")
        ArrayList<TypeItem> newlist = (ArrayList<TypeItem>) list.clone();
        newlist.remove(0);
        if (isPartMatch(lastcard, newlist, num))
          return true;
      }
    }
    return false;
  }
}