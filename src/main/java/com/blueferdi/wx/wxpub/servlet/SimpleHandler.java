/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blueferdi.wx.wxpub.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author ferdi
 */
public class SimpleHandler
{
    private final static String DEFAULT_RETURN = "还没有学习过哦";
    
    private final static String LEARN_SUCCESS = "已经学会了";
    
    private final static String BLACK_RETURN = "好歹说点啥";
    
    private final static Set<String> learns = new HashSet<String>();
    
    private Properties prop = new Properties();
    
    public void init() throws IOException
    {
        learns.add("learn");
        learns.add("学习");
        File file = new File("dictionary.properties");
        if(file.exists())
        {
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();
        }
    }
    
    public void destory() throws IOException
    {
        File file = new File("dictionary.properties");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        prop.store(fos, "");
        fos.close();
    }
    
    
    public String handle(String command)
    {
        
        String[] request = command.split(" ");
        
        if(request.length == 0 || request[0].trim().equals(""))
        {
            return BLACK_RETURN;
        }
        
        if(request.length >= 3  && learns.contains(request[0]))
        {
            prop.put(request[1].trim(), request[2].trim());
            return LEARN_SUCCESS;
        }
        else
        {
            return prop.getProperty(request[0], DEFAULT_RETURN);
        }
        
        
    }
}
