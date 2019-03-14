/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booleanmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Dell
 */
public class invertedIndex {
    public Map<String, ArrayList<Integer>> indexx;
    BooleanModel bm;
    
    public invertedIndex(BooleanModel bm){
        this.bm=bm;
        bm.docTitles= new ArrayList<String>();
    }
    
    public void invertedIndexFunction(){
        indexx = new HashMap<String, ArrayList<Integer>>();

        int docIdIterator=1;
        int titleCounter=0;
        for(int i=0;i<bm.fileContent.size();i++){
           
            String temp = bm.fileContent.get(i);
//            temp=temp.toLowerCase();
            if(titleCounter!=docIdIterator){
                titleCounter++;
                bm.docTitles.add(temp);
            }
            
            if(temp.equalsIgnoreCase("THE END"))
                docIdIterator++;
                
            
            String[] array = temp.split(" ");
            
            for(int j=0;j<array.length;j++){
                if(array[j].compareToIgnoreCase(" ")!=0 && array[j].compareToIgnoreCase("")!=0 && array[j].compareToIgnoreCase("THE")!=0 && array[j].compareToIgnoreCase("END")!=0){
                    if(this.indexx.containsKey(array[j])){
                        ArrayList<Integer> postingExist= indexx.get(array[j]);
                        postingExist.add(docIdIterator);
                        indexx.put(array[j],postingExist);
                    }
                    else{
                        ArrayList<Integer> postingNew= new ArrayList<Integer>();
                        postingNew.add(docIdIterator);
                        indexx.put(array[j], postingNew);
                    }
                }
            }
        }
        for(Map.Entry<String,ArrayList<Integer>>mEntry:indexx.entrySet()){
            mEntry.setValue(duplicateHandler(mEntry.getValue()));
            Collections.sort(mEntry.getValue());
        }
        
        Map<String, ArrayList<Integer>> map = new TreeMap<String, ArrayList<Integer>>(indexx);
        indexx = map;
    }
    
    private ArrayList<Integer> duplicateHandler(ArrayList<Integer> postingList) {
        ArrayList<Integer> mPosting = new ArrayList<Integer>();
        for(int i=0;i<postingList.size();i++){
            if(!mPosting.contains(postingList.get(i))) 
                mPosting.add(postingList.get(i));
        }
        return mPosting;
    }
    
     public void writeOutput ( ) throws IOException {
        File file = new File("outputInverted.txt");
        
        if ( !file.exists() ) 
            file.createNewFile();
        
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
       
        for (Map.Entry<String, ArrayList<Integer>> entry : indexx.entrySet()) {
            bw.write(entry.getKey()+" "+entry.getValue().size()+" -> ");
            for(int i=0;i<entry.getValue().size();i++)
                bw.write(entry.getValue().get(i)+ " -> ");
            bw.newLine();  
        }
        
        
        
        bw.flush();
        bw.close();
    }       



    
}
