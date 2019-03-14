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
public class positionalIndex {
    public Map<Integer, ArrayList<String>> indexx;
    BooleanModel bm;
    invertedIndex ii;
    
    
    
    public positionalIndex (BooleanModel bm , invertedIndex ii ) {
        this.bm=bm;
        this.ii=ii;
        
    }    
    
    public void positionalIndexFunction(){
        ArrayList resultt= new ArrayList<Integer>();
        
        indexx = new HashMap<Integer, ArrayList<String>>();
        int docIdIterator=1;
        for(int i=0;i<bm.fileContent.size();i++){
            String temp = bm.fileContent.get(i);
            String[] array = temp.split(" ");
            
            if(temp.equalsIgnoreCase("THE END"))
                docIdIterator++;            
            
            for(int j=0;j<array.length;j++){
                if(array[j].compareToIgnoreCase(" ")!=0 && array[j].compareToIgnoreCase("")!=0 && array[j].compareToIgnoreCase("THE")!=0 && array[j].compareToIgnoreCase("END")!=0){
                    if(this.indexx.containsKey(docIdIterator)){
                        ArrayList<String> postingExist= indexx.get(docIdIterator);
                        postingExist.add(array[j]);
                        indexx.put(docIdIterator,postingExist);
                    }
                    else{
                        ArrayList<String> postingNew= new ArrayList<String>();
                        postingNew.add(array[j]);
                        indexx.put(docIdIterator, postingNew);
                    }
                }
            }            
            
        }
    }
    
     public void writeOutput ( ) throws IOException {
        File file = new File("outputPositional.txt");
        
        if ( !file.exists() ) 
            file.createNewFile();
        
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
       
        for (Map.Entry<Integer,ArrayList<String>> entry : indexx.entrySet()) {
            bw.write(entry.getKey()+" "+entry.getValue().size()+" -> ");
            for(int i=0;i<entry.getValue().size();i++)
                bw.write(entry.getValue().get(i)+ " -> ");
            bw.newLine();  
        }
        bw.flush();
        bw.close();
     }


     
}
