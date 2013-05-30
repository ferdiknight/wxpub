/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blueferdi.wx.wxpub.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WeixinServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    public static final String Token = "token";

    /**
     * 用来接收微信公众平台的验证
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String[] ArrTmp =
        {
            Token, timestamp, nonce
        };
        Arrays.sort(ArrTmp);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ArrTmp.length; i++)
        {
            sb.append(ArrTmp[i]);
        }
        String pwd = Encrypt(sb.toString());
        String echostr = request.getParameter("echostr");
        System.out.println("pwd==" + pwd);
        System.out.println("echostr==" + echostr);
        if (pwd.equals(signature))
        {
            if (!"".equals(echostr) && echostr != null)
            {
                response.getWriter().print(echostr);
            }
        }
    }

    /**
     * 用来向普通用户传送信息
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        StringBuffer sb = new StringBuffer();
        String line;
        Map<String, String> map = null;
//		NodeDao nd = new NodeDaoImpl();
//		List<TChannelnode> list = null;
        try
        {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            map = XmlUtil.xml2Map(new String(sb.toString().getBytes(), "UTF-8"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        sb = new StringBuffer();

        sb.append("<xml><ToUserName><![CDATA[").append(map.get("xml.FromUserName"))
                .append("]]></ToUserName><FromUserName><![CDATA[")
                .append(map.get("xml.ToUserName"))
                .append("]]></FromUserName><CreateTime>")
                .append(map.get("xml.CreateTime"))
                .append("</CreateTime><MsgType><![CDATA[text]]></MsgType>")
                .append("<Content><![CDATA[");
//			 	for(int i=0;i<list.size();i++){
//			 		TChannelnode node = list.get(i);
//			 		sb.append("名称：").append(node.getName()).append("\n")
//					  .append("地址：").append(node.getAddress()).append("\n");
//					if(i!= (list.size()-1)){
//						sb.append("\n");
//					}
//				}
        sb.append("]]></Content>")
                .append("<FuncFlag>0</FuncFlag></xml>");


        response.setCharacterEncoding(
                "UTF-8");
        response.getWriter()
                .print(sb.toString());
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
    }

    public String Encrypt(String strSrc)
    {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); //to HexString
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    public String bytes2Hex(byte[] bts)
    {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1)
            {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public boolean isChinese(String str)
    {
        boolean result = false;
        for (int i = 0; i < str.length(); i++)
        {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941)
            {//汉字范围 \u4e00-\u9fa5 (中文)  
                result = true;
            }
        }
        return result;
    }
    /*public static void main(String[] args) {
     StringBuffer sb = new StringBuffer();
     NodeDao nd = new NodeDaoImpl();
     List<TChannelnode> list = nd.getAllChannelnodes("1");
     System.out.println(list.size());
     for(TChannelnode node:list){
     sb.append("名称：").append(node.getName()).append("，")
     .append("地址：").append(node.getAddress()).append("\n");
     }
     sb.append("<xml><ToUserName><![CDATA[")
     .append("]]></ToUserName><FromUserName><![CDATA[")
     .append("]]></FromUserName><CreateTime>")
     .append("</CreateTime><MsgType><![CDATA[text]]></MsgType>")
     .append("<Content><![CDATA[")
     .append("对不起，没有查到您想要的信息！")
     .append("]]></Content>")
     .append("<FuncFlag>0</FuncFlag></xml>");
     WeixinServlet ws = new WeixinServlet();
     System.out.println(ws.isChinese("1"));
     System.out.println(sb.toString());
     }*/
}
