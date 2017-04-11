/**
 * 
 */
package com.Nutch.Crawl.TelAmz;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import us.codecraft.xsoup.Xsoup;

/**
 * @author surendra
 *
 */
public class TeleAmzRichMedia {

	/**
	 * 
	 */
	public TeleAmzRichMedia() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	HTable ht=null;
	Scan sc=null;
	ResultScanner resc;
	String rownames=null,family=null,qualifier=null,content=null,Splitter_SK=null,Splitter_Text=null;
	
	MSDigest msd=new MSDigest();
	String rtitle=null,rimages=null,rdimens=null;
	
	
	
	public void TeleARMCNT(String names)
	{
		try
		{
			Configuration config=HBaseConfiguration.create();
			ht=new HTable(config,"teleamz_webpage");
			sc=new Scan();
			resc=ht.getScanner(sc);
			for(Result res = resc.next(); (res != null); res=resc.next())
			{
				for(KeyValue kv:res.list())
				{
					
					rownames=Bytes.toString(kv.getRow());
					family=Bytes.toString(kv.getFamily());
					qualifier=Bytes.toString(kv.getQualifier());
					
					
					
					if(rownames.equals(names))	
					
					{
						if(family.equals("f")&& qualifier.equals("cnt"))
							
						{
						
						content=Bytes.toString(kv.getValue());
						Document document = Jsoup.parse(content);
						String url=Xsoup.compile("//meta[@property='og:url']/@content").evaluate(document).get();
						
						Elements el=Xsoup.compile("//div[@class='vc_row wpb_row vc_row-fluid']").evaluate(document).getElements();
						
						for(Element xel:el)
						{
						
						//System.out.println(xel.toString());
							
								
								String title=Xsoup.compile("//div[@class='wpb_wrapper']/h3/strong/text()|//div[@class='wpb_wrapper']/h3/text()").evaluate(xel).get();
								if(title!=null)
								{
								
									rtitle=title.replace(",", "").trim();
									
									String  images=Xsoup.compile("//img/@src").evaluate(xel).get();
									
									if(images!=null)
									{
									rimages=images.trim();
									
									String  width=Xsoup.compile("//img/@width").evaluate(xel).get();
									String  height=Xsoup.compile("//img/@height").evaluate(xel).get();
									
									if(width!=null && height!=null)
									{
										String dimens=width+"x"+height;
										rdimens=dimens.trim();
										//System.out.println(rtitle+"#<>#");
										//System.out.print(rimages+"#<>#");
										
								//	System.out.print(rdimens+"#<>#");
									RichMediaTab(rimages,rtitle,rdimens,url);
									
									
									}
									}
									
								
								}
								
						}
						
							
						
												}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.getMessage();
			//e.printStackTrace();
		}
		
		finally
		{
			try
			{
				ht.close();
				resc.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}
			
	
			
	
	
	
	public void RichMediaTab(String image_sk,String movie_sk,String dimens,String url) throws Exception
	
	{
		
		
		
		
		////////////////Rm_SK//////////////////////
		msd.MD5(image_sk.trim());
		
		System.out.print(msd.md5s.trim()+"#<>#");
		
		////////////////Program_SK//////////////////////
		
		msd.MD5(movie_sk.trim());
		System.out.print(msd.md5s.trim()+"#<>#");
		
		////////////////Program_Type//////////////////////
		
		System.out.print("movie".trim()+"#<>#");
		
		////////////////Media_Type//////////////////////
		
		System.out.print("image".trim()+"#<>#");
		
		////////////////Image_Type//////////////////////
		
		System.out.print("medium".trim()+"#<>#");
		
		////////////////Rm_Size//////////////////////
		
		System.out.print("#<>#");
		
		////////////////Rm_Dimensions//////////////////////
		
		System.out.print(dimens.trim()+"#<>#");
		
		////////////////Rm_Description//////////////////////
		
		System.out.print("#<>#");
		
		////////////////Rm_Image_Url//////////////////////
		
		System.out.print(image_sk.trim()+"#<>#");
		
		////////////////Rm_Reference_Url//////////////////////
		
		System.out.print(url.trim()+"#<>#");
		
		////////////////Aux_Info/////////////////////
		
		System.out.print("#<>#");
		
		////////////////Created_At//////////////////////
		
		System.out.print("#<>#");
		
		////////////////Modified_At//////////////////////
		
		System.out.print("#<>#");
		
		////////////////Last_Seen//////////////////////
		
		System.out.print("#<>#");
		
		//////////////// New Line/////////////////////
		
		System.out.print("\n");
		
		
	}

}
