/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booleanmodel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Dell
 */
public class BooleanModel {
    ArrayList<String> fileContent;
    ArrayList<String> stopWords;
    ArrayList<String> docTitles;
    
    public void readFile ( ) throws FileNotFoundException {
        fileContent = new ArrayList<String>();
        fileContent = fetchFromFile(new File("WS Complete Works.txt"));
        
        
    }
    
    private ArrayList<String> fetchFromFile ( File file ) throws FileNotFoundException {
        Scanner in = new Scanner(file);
        updateStopWord();
        String temp;
        boolean ignoreText=false; // << >> middle text removal
        ArrayList<String> fileContent = new ArrayList<String>();
        while ( in.hasNext() ) {
            temp = in.nextLine();
            if(isInt(temp)){                        // if year(1609,1987) then it passses
                while(in.hasNext()){
                    temp = in.nextLine();
                    if(temp.equalsIgnoreCase("THE END")){
                        fileContent.add(temp);
                        break;
                    }
                    if(temp.length()>0){
                        if(!isInt(temp)){
                            if(temp.contains("<<"))
                                ignoreText=true;
                            if(temp.contains(">>"))
                                ignoreText=false;
                            
                           if(ignoreText==false){
//                             temp=temp.replaceAll("'s","");
                               temp=temp.replaceAll("[^a-zA-Z ]","");
                               temp=temp.toLowerCase();
                               String noStopWords[]=temp.split(" ");
                               String finalTemp="";
                               for(int i=0;i<noStopWords.length;i++){
                                   if(!stopWords.contains(noStopWords[i]))
                                       finalTemp=finalTemp.concat(noStopWords[i]+" ");
                               }
                               fileContent.add(finalTemp);
                           }
                        }
                    }
                }
                
            }
            
        }
        return fileContent;
    }
    public static boolean isInt(String str) {
    try { 
        Integer.parseInt(str); 
    } catch(Exception e) {
        return false;
    }
    return true;
}
    public void updateStopWord ( ) throws FileNotFoundException {
        File file = new File("Stopword-List.txt");
        Scanner in = new Scanner(file);
        
        stopWords = new ArrayList<String>();
        while ( in.hasNext() ){ 
            stopWords.add(in.nextLine());
        }
    }    
    public void writeOutput ( ) throws IOException {
        File file = new File("output.txt");
        
        if ( !file.exists() ) 
            file.createNewFile();
        
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (int i=0;i<fileContent.size();i++ ) {
                bw.write(fileContent.get(i));
                bw.newLine();
            }
        
        
        bw.flush();
        bw.close();
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BooleanModel bm=new BooleanModel();
//            bm.updateStopWord();
            bm.readFile();
            bm.writeOutput();
            
            invertedIndex ii = new invertedIndex(bm);
            ii.invertedIndexFunction();
            ii.writeOutput();
            
//            for(int i=0;i<bm.fileContent.size();i++){
//           
            //System.out.println(bm.fileContent.get(i));
//            }
            
            // TODO code application logic here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
