/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booleanmodel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 * @author k152890
 */
public class searchQuery {
    public BooleanModel bm;
    public invertedIndex ii;
    public positionalIndex pi;
    public boolean errorOccured=false;
    searchQuery(){
        try {
            bm=new BooleanModel();
            bm.readFile();
            bm.writeOutput();
            ii=new invertedIndex(bm);
            ii.invertedIndexFunction();
            ii.writeOutput();
            pi=new positionalIndex(bm,ii);
            pi.positionalIndexFunction();
            pi.writeOutput();
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }   
    }
    public ArrayList<Integer> searchPositional(String temp){
    final long startTime = System.nanoTime();
    this.errorOccured=false;
        ArrayList resultt= new ArrayList<Integer>();
        temp=temp.toLowerCase();
//           System.out.println(temp);
           if(temp.contains("and")){
               temp=temp.replace("and","");
               temp=temp.replace("  "," ");
           }
//           System.out.println(temp);
           String array[]=temp.split(" ");
//       ArrayList tempList= new ArrayList<Integer>();
       ArrayList tempList2= new ArrayList<String>();
       
       for(int i=0;i<array.length;i++){
//           System.out.println("In for loop start");
           if(array[i].equalsIgnoreCase("/")){
                                                   System.out.println("In / if start");
               String w1=array[i-2];
               String w2=array[i-1];
               int diff=Integer.parseInt(array[i+1]);
               
               for(int j=0;j<resultt.size();){
//                                                          System.out.println("In for loop2 start");
                   int targetDoc=(Integer)resultt.get(j);
                   tempList2=pi.indexx.get(targetDoc);
//                   String targetContent=String.join(" ",tempList2);
//                   String targetArray[]=targetContent.split(" ");
                   int k=0;
                   boolean found=false;
                   k=tempList2.indexOf(array[i-2]);
                   while(k!=-1 && found==false){
//                                                                System.out.println("In while loop start");
                       String tmp=(String)tempList2.get(k+diff+1);
                       if(tmp.equalsIgnoreCase(array[i-1]))
                           found=true;
                       else
                           tempList2.remove(k);
                       
                       k=tempList2.indexOf(array[i-2]);
                   }
                   
                   if(found==false){
                       resultt.remove((Object)targetDoc);
                   }
                   else
                       j++;
               }
           
           }           
           else{
               System.out.println(array[i]);
                    if(ii.indexx.containsKey(array[i]) || bm.isInt(array[i])){ 
//                          System.out.println("In / else start");
                        
                        if(ii.indexx.containsKey(array[i])){  
                            if(i==0)
                                resultt.addAll(ii.indexx.get(array[i]));
                            else
                                resultt.retainAll(ii.indexx.get(array[i]));                        
                        }
                    }
                    else
                        this.errorOccured=true;
           }
       }
        final long endTime = System.nanoTime()- startTime;
        System.out.println("Positional Index Search time: "+endTime/1000000000.0+" seconds");
        
        
       return resultt;
    }
    public ArrayList<Integer> searchInverted(String temp){
        final long startTime = System.nanoTime();
        this.errorOccured=false;
        String array[]= temp.split(" ");
        ArrayList resultt= new ArrayList<Integer>();
        ArrayList tempList= new ArrayList<Integer>();
        ArrayList tempList2= new ArrayList<Integer>();
        
        int seq=0;
        int skip = 1;
//
        if(array[0].equalsIgnoreCase("NOT")){
//                          System.out.println("In 0th index if ");
            if(ii.indexx.containsKey(array[1]))
                resultt = ii.indexx.get(array[1]);
            else
                this.errorOccured=true;
            ArrayList<Integer> tempp = new ArrayList<Integer>();
            for (int i=1;i<=bm.docTitles.size();i++){
                if (!resultt.contains(i)) 
                    tempp.add(i);
            }
            resultt = tempp;
            skip = 2;
        }
        else{
            if(ii.indexx.containsKey(array[0]))
                resultt = ii.indexx.get(array[0]);
            else
                this.errorOccured=true;
        }
        
        for(int i=skip;i<array.length;i+=2){
//                          System.out.println("In for loop start");
            if(array[i].equalsIgnoreCase("OR")){
//                          System.out.println("In OR if");
                if(array[i+1].equalsIgnoreCase("NOT")){
//                          System.out.println("In NOT if");                    
                    for(int j=1;j<=bm.docTitles.size();j++)
                        tempList.add(j);
                    if(ii.indexx.containsKey(array[i+2])){
                        tempList.removeAll(ii.indexx.get(array[i+2]));
                        resultt.addAll(tempList);
                        i = i+2;
                    }
//                          System.out.println("In OR if end");
                }
                else{
//                          System.out.println("In OR else");
                    if(ii.indexx.containsKey(array[i+1])) 
                        resultt.addAll(ii.indexx.get(array[i+1]));
                } 
            }
            else if(array[i].equalsIgnoreCase("AND")){
                if(!array[i+1].equalsIgnoreCase("NOT")){
                    if(ii.indexx.containsKey(array[i+1])){
                        resultt.retainAll(ii.indexx.get(array[i+1]));
                    }
                    else 
                        this.errorOccured=true;
                } 
                else{
                    for(int j=1;j<=bm.docTitles.size();j++) 
                        tempList.add(j);
                    if(ii.indexx.containsKey(array[i+2])){
                        tempList.removeAll(ii.indexx.get(array[i+2]));
                        resultt.retainAll(tempList);
                        i = i+2;
                    }
                    else
                        this.errorOccured=true;
                }
            }
        }                            
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        for(int j=0;j<array.length;){
//            System.out.println("In for loopp!");
//                if(array[j].equalsIgnoreCase("AND") || array[j].equalsIgnoreCase("OR") || array[j].equalsIgnoreCase("NOT")){
//                    seq++;
//                    if(array[0].equalsIgnoreCase("NOT")){
//                       j=j+2;
//                    }
//                    if(array[j].equalsIgnoreCase("AND")){
//                                                                System.out.println("In ANDD!");
//                        if(array[j+1].equalsIgnoreCase("NOT")){
//                            if(ii.indexx.containsKey(array[j+2])){
//                                tempList=ii.indexx.get(array[j+2]);
//                                for(int k=1;k<=bm.docTitles.size();k++){
//                                    if(!tempList.contains(k))
//                                        resultt.add(k);
//                                    if(tempList.contains(k) && resultt.contains(k))
//                                        resultt.remove(k);
//                                }
//                            }
//                            else
//                                this.errorOccured=true;
//                        }
//                        else{
//                            if(ii.indexx.containsKey(array[j+1]) && ii.indexx.containsKey(array[j-1])){
//                                tempList=ii.indexx.get(array[j+1]);
//                                tempList2=ii.indexx.get(array[j-1]);
//                                int p1=0,p2=0;
//                                while(p1<tempList.size() && p2<tempList2.size()){
//                                    if(tempList.get(p1)==tempList2.get(p2)){
//                                        resultt.add(tempList.get(p1));
//                                        p1++;
//                                        p2++;
//                                    }
//                                    else if((Integer)tempList.get(p1)<(Integer)tempList2.get(p2))
//                                        p1++;
//                                    else
//                                        p2++;
//                                }
//                            }
//                            else
//                                this.errorOccured=true;                            
//                        }
//                    }
//                    else if(array[j].equalsIgnoreCase("OR")){
//                                                                    System.out.println("In ORRRR!");
//                        if(array[j+1].equalsIgnoreCase("NOT")){
//                            if(ii.indexx.containsKey(array[j+2])){
//                                tempList=ii.indexx.get(array[j+2]);
//                                for(int k=1;k<=bm.docTitles.size();k++){
//                                    if(!tempList.contains(k))
//                                        resultt.add(k);
//                                    if(tempList.contains(k) && resultt.contains(k))
//                                        resultt.remove(k);
//                                }
//                            }
//                            else
//                                this.errorOccured=true;
//                        }
//                        else{
//                            if(ii.indexx.containsKey(array[j+1]) || ii.indexx.containsKey(array[j-1])){
//                                if(ii.indexx.containsKey(array[j+1]))
//                                    resultt.add(ii.indexx.get(array[j+1]));
//
//                                if(ii.indexx.containsKey(array[j-1]))
//                                    resultt.add(ii.indexx.get(array[j-1]));
//                            }
//                            else
//                                this.errorOccured=true;                            
//                        }
//                    }
//                    j=j+2;
//                }
//                else if(ii.indexx.containsKey(array[j])){
//                    j++;
//                }
//                else
//                    this.errorOccured=true;
//            }
            
            ArrayList<Integer> resultt2 = new ArrayList<Integer>();
            for(int i=0;i<resultt.size();i++){
                if(!resultt2.contains(resultt.get(i))) 
                    resultt2.add((Integer)resultt.get(i));
            }
                     
        final long endTime = System.nanoTime()- startTime;
        System.out.println("Inverted Index Search time: "+endTime/1000000000.0+" seconds");
        
        return resultt;
    }
       public static void main(String[] args) {
           searchQuery sq= new searchQuery();
           Scanner in= new Scanner(System.in);
           System.out.println("Enter Query:");
           
           String query= in.nextLine();
           
           System.out.println(query);
           ArrayList<Integer> resultt2 = new ArrayList<Integer>();
           resultt2=sq.searchPositional(query);
         
           if(sq.errorOccured==false){
           System.out.println("Size: "+resultt2.size());
            
            for(int i=0;i<resultt2.size();i++){
                System.out.println((sq.bm.docTitles.get(resultt2.get(i)-1)).toUpperCase());
            }
           }
           else
              System.out.println("'"+query+"' not found in any document!");
           
          System.out.println("Enter Query:");
            
            
           query= in.nextLine();
           
           System.out.println(query);
           
           resultt2=sq.searchInverted(query);
           
           if(sq.errorOccured==false){
            System.out.println("Size: "+resultt2.size());
            
            for(int i=0;i<resultt2.size();i++){
                System.out.println((sq.bm.docTitles.get(resultt2.get(i)-1)).toUpperCase());
            }
           }
           else
               System.out.println("'"+query+"' not found in any document!");

           
           
           
           
       }
}
