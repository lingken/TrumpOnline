package com.lux.trump.client.presenter;

import java.io.Serializable;


public class TypeItem implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = -3374035041368758557L;
  public String item;
  char type;
  int length;
  public TypeItem(String item, char type, int length){
    this.item = item;
    this.type = type;
    this.length = length;
  }
  
  int isMatch(int[]card, int n){
    if (card.length < item.length())
      return 0;
    switch (this.type){
      case 'F':
      //  System.out.println(n+" "+card.length + " "+ item.length() + " " + this.length);
        if (n != -1 && (card.length != n || n != item.length())){
          return 0;
        }
        if (n == -1 && card.length != item.length()*this.length)
          return 0;
        
        for (int i=0; i<card.length; i++){
          if (card[i] != card[0])
            return 0;
        }
        return card.length;
      case 'V':
        if (n != -1 && (card.length != n*item.length() || n < this.length*item.length()))
          return 0;
        if (n == -1 && card.length < this.length*item.length() )
          return 0;
        for (int i=0; i<card.length; ){
          int j=0;
          while (j<item.length()){
            if (card[i] != card[i-j])
              return 0;
            i++;
            j++;
          }
        }
        return n;
      case 'S':
        if (n != -1 && (card.length != n*item.length() || n < this.length)){
          return 0;
        }
        for (int i=0; i<card.length;){
          int j=0;
          int s = i/item.length();
          if (card[0]+s != card[i])
            return 0;
          while (j<item.length()){
            if (card[i] != card[i-j])
              return 0;
            i++;
            j++;
            if (i == card.length)
              break;
          }
        }
        return card.length/item.length();
      default:
        break;
    }
    return n;
  }
}