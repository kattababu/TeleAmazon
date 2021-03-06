/**
 * 
 */
package com.Nutch.Crawl.TelAmz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
//import org.joda.time.DateTime;
//import org.joda.time.Days;
//import org.joda.time.Hours;
//import org.joda.time.Minutes;
//import org.joda.time.Seconds;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.xsoup.Xsoup;

/**
 * @author surendra
 *
 */


public class TeleAmazonasSchedulesCNT {

	/**
	 * 
	 */
	public TeleAmazonasSchedulesCNT() {
		// TODO Auto-generated constructor stub
	}
	
	HTable ht=null;
	Scan sc=null;
	ResultScanner resc;
	String rownames=null,family=null,qualifier=null,content=null,Splitter_SK=null,Prg_SK=null,program_type=null,rtitle=null,rtitlemd=null,rtitlemd5=null;

	String results=null,resadd=null;
	
	MSDigest msd=new MSDigest();
	static FileOutputStream fos=null;
	static PrintStream ps=null;
	static File file=null;
	long Duration=0;
	static String rmtitle=null;
	static ArrayList<String> al=new ArrayList<String>();
	static String[] myArray=new String[1000];
	
	static ArrayList<String> al2=new ArrayList<String>();
	static String[] myArray2=new String[1000];
	
//	/String[] splitsspace=null;
	
	
	
	
	
	
	static 
	{
		FileStore.SchedulesTable("schedule");
	}
	
	
	
	public void TeleAScheduleCNT(String names)
	{
		try
		{
			
		fos = new FileOutputStream(FileStore.fileSched,true);
		ps = new PrintStream(fos);
		System.setOut(ps);
			
	
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
						
						
						Elements els=Xsoup.compile("//div[@class='divParrilla']//table//tbody/tr").evaluate(document).getElements();
						
						for(Element el:els)
						{
							
							
							
							///////////// Channel_SK///////////////////////
							//System.out.println(TeleAmazonasChanCNT.c_sk);
							
							String Ch_sk=TeleAmazonasChanCNT.c_sk;
							
							
							
							///////////////////////// Program_Sk//////////////////////
							
							//String url=Xsoup.compile("//meta[@property='og:url']/@content").evaluate(document).get();
							
							
							String prg_url=Xsoup.compile("//a/@href").evaluate(el).get();
							
							
							
							
							if(!prg_url.isEmpty())
							{
											
								String title=Xsoup.compile("//a/@title").evaluate(el).get();
								
								
								
								
								
						if(prg_url.contains("javascript://") || prg_url.endsWith("jarabe-de-pico/") || prg_url.endsWith("en-corto/") || prg_url.endsWith("peliculas/"))
						{
							if(prg_url.endsWith("peliculas/"))
									{
								String title2=Xsoup.compile("//a//span/text()").evaluate(el).get();
								
								if(title2!=null)
								{
									
								rtitle=title2.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").replace(";", "").trim();
								
								 
								msd.MD5(rtitle.trim());
								Prg_SK=msd.md5s.trim();
								}
								
								
									}
							
							
							
							else
							{
							
							
							
							
							
							if(title!=null)
							{
								
							rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
							
							
							msd.MD5(rtitle.trim());
							Prg_SK=msd.md5s.trim();
							}
							}
							
							
						}
						
						
						else if( prg_url.contains("/deportes/") || prg_url.contains("/noticias/") || prg_url.contains("/actualidad/") )
						{
							//SplitUrls(pr_url);
							//break;
							if(title!=null)
							{
								
							rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
							
							
							msd.MD5(rtitle.trim());
							Prg_SK=msd.md5s.trim();
							}
							
						}
						else
						{
							
							SplitUrlsSD(prg_url);
							Prg_SK=Splitter_SK;
							//System.out.println(Splitter_SK);

						}
						
							
						
						//	System.out.println("\n\n");
							

							
							
							
							
							
							
							//////////////////////////////////////////
							
							//System.out.println(msd.md5s.trim());
							
							
							//////////////////////////Durations////////////////////////
							
							
							String durs=Xsoup.compile("//td/span/text()").evaluate(el).get();
							
								
							
							String durse=Xsoup.compile("//td[2]/span/text()").evaluate(el).get();
							
							///////////////////////////////////////////////////////
							
							
							SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
							Date date1 = format.parse(durs);
							Date date2 = format.parse(durse);
							
							long difference = date2.getTime() - date1.getTime(); 
							
							int days = (int) (difference / (1000*60*60*24));  
					        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60)); 
					        long min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
					        hours =  (hours < 0 ? -hours : hours);
					         if(durs.contains("PM") && durse.contains("AM") && min==0)
							{
							
					       Duration= (24-hours)*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					      }
					        else if(durs.contains("PM") && durse.contains("AM") && min!=0)
							{
					        	long mins=( min < 0 ? ((min-0)) : min);
					        	long hourss=(24-hours)*60;
							
					       Duration= hourss+mins;
					    	  
							}
					        
					        else

					        {
					        	 Duration= hours*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					        	
					        }
					        
					        
					           ///////////////////////////////////////////////// StartDateTime////////////////////
							
					        String startdate=Xsoup.compile("/@id").evaluate(el).get();
					        
					        if(!startdate.contains("2017"))
					        {
					        	//SplitTime(startdate);
					        	
					        	String rest=startdate.substring(0, startdate.length()-8);
					        	String rest2=startdate.substring(rest.length()-1,startdate.length());
					        	//System.out.println(rest2);
					        	String resadd=rest+"04-2017"+rest2;
					        	//System.out.println(resadd);
					        	
					        	SplitTime(resadd);
					        	//System.out.println(results);
					        	
					        	
					        }
					        else
					        {
					        	SplitTime(startdate);
					        	//System.out.println(results);
					        }
					        
					        if(Prg_SK!=null && !prg_url.endsWith("peliculas/") && !rtitle.isEmpty() && Duration!=0)
							{
							
								
					        	program_type="tvshow";
								SchedulesTab(Ch_sk,Prg_SK,program_type,results,Duration);
							
							}
							else if(Prg_SK!=null && prg_url.endsWith("peliculas/")&& !rtitle.isEmpty() && Duration!=0)
							{
								program_type="movie";
								
								
								SchedulesTab(Ch_sk,Prg_SK,program_type,results,Duration);
								
								
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
			//e.getMessage();
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				ht.close();
				resc.close();
				ps.close();
				fos.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}
	
	////////////////////////////////////////////////////////////////////// RichMedia FOR Schedule Case//////////////////////////////////
	
	
	public void TeleAScheduleRMCNT(String names)
	{
		try
		{
			
			fos = new FileOutputStream(FileStore.fileRM,true);
			ps = new PrintStream(fos);
			System.setOut(ps);
			
	
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
						
						
						
						Elements els=Xsoup.compile("//div[@class='divParrilla']//table//tbody/tr").evaluate(document).getElements();
						
						for(Element el:els)
						{
							
							
							
							String Sc_Image=Xsoup.compile("//img/@src| //img  /@src").evaluate(el).get();
							String mImage=null;
							if(Sc_Image!=null)
							{
								mImage=Sc_Image;
								
								
								
								/*
								
								//System.out.println(mImage);
								///////////////////////// Program_Sk//////////////////////
								String title=Xsoup.compile("//a/@title").evaluate(el).get();
								
								String rtitle=title.replaceAll("[,(,)]","").trim();
								
								//System.out.println(rtitle);
								*/
								
								
								String prg_url=Xsoup.compile("//a/@href").evaluate(el).get();
								if(!prg_url.isEmpty())
								{
								
								//System.out.println(prg_url);
									String title=Xsoup.compile("//a/@title").evaluate(el).get();
									
									if(prg_url.contains("javascript://") || prg_url.endsWith("jarabe-de-pico/") || prg_url.endsWith("en-corto/") || prg_url.endsWith("peliculas/"))
									{
										if(prg_url.endsWith("peliculas/"))
												{
											String title2=Xsoup.compile("//a//span/text()").evaluate(el).get();
											
											if(title2!=null)
											{
												
											rtitle=title2.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").replace(";", "").trim();
											
											
											msd.MD5(rtitle.trim());
											Prg_SK=msd.md5s.trim();
											}
											
											
												}
										else
										{
										
										
										
										
										if(title!=null)
										{
											
										rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
										
										msd.MD5(rtitle.trim());
										Prg_SK=msd.md5s.trim();
										}
										}
										
										
									}
									
									else if( prg_url.contains("/deportes/") || prg_url.contains("/noticias/") || prg_url.contains("/actualidad/") )
							{
								//SplitUrls(pr_url);
								//break;
										if(title!=null)
										{
											
										rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
										
										msd.MD5(rtitle.trim());
										Prg_SK=msd.md5s.trim();
										}
										
								
							}
							else
							{
								
								SplitUrlsSD(prg_url);
								Prg_SK=Splitter_SK;
								//System.out.println(Splitter_SK);

							}
							

								
								String url=Xsoup.compile("//meta[@property='og:url']/@content").evaluate(document).get();
								
								
								 if(Prg_SK!=null && !prg_url.endsWith("peliculas/") && !rtitle.isEmpty())
										{
								
									 
									program_type="tvshow";
								
								ScheduleRMTab(mImage,Prg_SK,program_type,url);
								}
								else  if(Prg_SK!=null && prg_url.endsWith("peliculas/") && !rtitle.isEmpty())
									
								{
									program_type="movie";
									
									ScheduleRMTab(mImage,Prg_SK,program_type,url);
									
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
			//e.getMessage();
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				ht.close();
				resc.close();
				ps.close();
				fos.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}
	
	
	
	
	
	
	//////////////////////////////////////////////////// TVSHOW SCHEDULES///////////////////////////////////
	
	
	
	public void TeleAScheduleTvShowCNT(String names)
	{
		try
		{
			
	fos = new FileOutputStream(FileStore.fileTvshow,true);
		ps = new PrintStream(fos);
			System.setOut(ps);
			
	
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
						
						
						Elements els=Xsoup.compile("//div[@class='divParrilla']//table//tbody/tr").evaluate(document).getElements();
						
						for(Element el:els)
						{
							
							
							
							
							
							
							///////////////////////// Program_Sk//////////////////////
							
							
							
							
							String prg_url=Xsoup.compile("//a/@href").evaluate(el).get();
							if(!prg_url.isEmpty())
							{
							
							
								String title=Xsoup.compile("//a/@title").evaluate(el).get();
							//System.out.println(prg_url);
						if(prg_url.contains("javascript://") || prg_url.endsWith("jarabe-de-pico/") || prg_url.endsWith("en-corto/") )
						{
							
							
							rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
							msd.MD5(rtitle.trim());
							Prg_SK=msd.md5s.trim();
							
														
							
						}
						else if( prg_url.contains("/deportes/") || prg_url.contains("/noticias/") || prg_url.contains("/actualidad/") )
						{
							//SplitUrls(pr_url);
							//break;
							//String title=Xsoup.compile("//a/@title").evaluate(el).get();
							
							rtitle=title.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").trim();
							msd.MD5(rtitle.trim());
							Prg_SK=msd.md5s.trim();
							
							
						}
						else
						{
							
							//SplitUrlsSD(prg_url);
							Prg_SK=null;
							//System.out.println(Splitter_SK);

						}
						
							
						//	System.out.println("\n\n");
							

							
							
							
							
							
							
							//////////////////////////////////////////
							
							//System.out.println(msd.md5s.trim());
							
							
							//////////////////////////Durations////////////////////////
							
							
							String durs=Xsoup.compile("//td/span/text()").evaluate(el).get();
							
								
							
							String durse=Xsoup.compile("//td[2]/span/text()").evaluate(el).get();
							
							///////////////////////////////////////////////////////
							
							
							SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
							Date date1 = format.parse(durs);
							Date date2 = format.parse(durse);
							
							long difference = date2.getTime() - date1.getTime(); 
							
							int days = (int) (difference / (1000*60*60*24));  
					        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60)); 
					        long min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
					        hours =  (hours < 0 ? -hours : hours);
					         if(durs.contains("PM") && durse.contains("AM") && min==0)
							{
							
					       Duration= (24-hours)*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					      }
					        else if(durs.contains("PM") && durse.contains("AM") && min!=0)
							{
					        	long mins=( min < 0 ? ((min-0)) : min);
					        	long hourss=(24-hours)*60;
							
					       Duration= hourss+mins;
					    	  
							}
					        
					        else

					        {
					        	 Duration= hours*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					        	
					        }
					        
					         String url=Xsoup.compile("//meta[@property='og:url']/@content").evaluate(document).get();
					         
					        		
								
					      
					        if(Prg_SK!=null)
					        {
					        	if(prg_url.contains("javascript://"))
					        	{
					        
					        	SchedulesTvshowMovieTabs(Prg_SK,rtitle,url);
					        	}
					        	else
					        	{
					        		SchedulesTvshowMovieTabs(Prg_SK,rtitle,url,prg_url);
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
			//e.getMessage();
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				ht.close();
				resc.close();
				ps.close();
				fos.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}

	///////////////////////////////////////////////////////// Movie Schedules/////////////////////////////////////////
	
	
	
	public void TeleAScheduleMovieCNT(String names)
	{
		try
		{
			
			//String regex=null;
	fos = new FileOutputStream(FileStore.fileM,true);
	ps = new PrintStream(fos);
	System.setOut(ps);
			
	
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
						
						
						
												
						
						Elements els=Xsoup.compile("//div[@class='divParrilla']//table//tbody/tr").evaluate(document).getElements();
						
						for(Element el:els)
						{
							
							
							
							
							
							
							///////////////////////// Program_Sk//////////////////////
							
							
							
							
							String prg_url=Xsoup.compile("//a/@href").evaluate(el).get();
							if(!prg_url.isEmpty())
							{
							
							
							//System.out.println(prg_url);
						if(prg_url.contains("javascript://") || prg_url.endsWith("peliculas/"))
						{
							
							//System.out.println("The Urls:"+prg_url);
							
							
							String title2=Xsoup.compile("//a//span/text()").evaluate(el).get();
							
							if(title2!=null)
							{
								
								//

								
								
							rtitle=title2.replaceAll("\\(.*\\)","").replace("GYE", "").replace(",", "").replace("LUNES", "").replace("MARTES", "").replace("MIÉRCOLES", "").replace("JUEVES", "").replace("VIERNES", "").replace("SÁBADO", "").replace("DOMINGO", "").replace(";", "").trim();
							
							
							//System.out.println("Welcome to SCHEDule Movies:"+rtitle);
							
							SchedulesTitle(rtitle);
							
								msd.MD5(rtitle.trim());
								Prg_SK=msd.md5s.trim();
								

								
							
							
							
							
							//
							//System.out.println("Remaing"+rtitle);
							
							}
														
							
						}
													
						//	System.out.println("\n\n");
							

							
							
							
							//////////////////////////Durations////////////////////////
							
							
							String durs=Xsoup.compile("//td/span/text()").evaluate(el).get();
							
								
							
							String durse=Xsoup.compile("//td[2]/span/text()").evaluate(el).get();
							
							///////////////////////////////////////////////////////
							
							
							SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
							Date date1 = format.parse(durs);
							Date date2 = format.parse(durse);
							
							long difference = date2.getTime() - date1.getTime(); 
							
							int days = (int) (difference / (1000*60*60*24));  
					        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60)); 
					        long min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
					        hours =  (hours < 0 ? -hours : hours);
					         if(durs.contains("PM") && durse.contains("AM") && min==0)
							{
							
					       Duration= (24-hours)*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					      }
					        else if(durs.contains("PM") && durse.contains("AM") && min!=0)
							{
					        	long mins=( min < 0 ? ((min-0)) : min);
					        	long hourss=(24-hours)*60;
							
					       Duration= hourss+mins;
					    	  
							}
					        
					        else

					        {
					        	 Duration= hours*60+( min < 0 ? ((min-1)+60) : min);//+( sec < 0 ? -sec : sec);
					        	
					        }
					        
					         String url=Xsoup.compile("//meta[@property='og:url']/@content").evaluate(document).get();
					         
					        		
								
					      
					        if(Prg_SK!=null  && prg_url.endsWith("peliculas/")&& !rtitle.isEmpty())
					        {
					        	
					        	
					        	
					        	//System.out.println(rtitlemd);
					        	
					        	//SchedulesTvshowMovieTabs(Prg_SK,rtitle,Duration,url,prg_url);
					        
					        	/////////////////////////////////////////////////////// Modification Code///////////////////////////////
					        	
					        	if(!rtitlemd5.equals(rtitlemd))
					        	{
					        	SchedulesTvshowMovieTabs(Prg_SK,rtitle,url,prg_url);
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
				ps.close();
				fos.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}


	
	//////////////////////////////////////////
	
	
	
	
	
	public void TeleAMovieCNT(String names)
	{
		try
		{
			
	//fos = new FileOutputStream(FileStore.fileM,true);
		//ps = new PrintStream(fos);
			//System.setOut(ps);
			
	
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
						
						
						Elements elsm=document.select("div.wpb_text_column");
						
						
						
						for(Element elm:elsm)
						{
							Element eltitlem=elm.select("h3").first();
							if(eltitlem!=null)
							{
							
							String titlem=eltitlem.text();
							
							
							
							//rmtitle=titlem.replaceAll("[,/]", "").replace("-", " ").replace("Í", "I").trim();
							
							rmtitle=titlem.replaceAll("[,/]", "").trim();
						//System.out.println("Welcome Movie Names:"+rmtitle);
						
							al.add(rmtitle);
						
							myArray= al.toArray(new String[0]);	
						//String regex=rmtitle;

							}
						}
						
						
										
												
						
						
						
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			//e.getMessage();
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				
				
				
				
				
				
				
				ht.close();
				resc.close();
				ps.close();
				fos.close();
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
						
	
	}



	
//////////////////////////// Tabs/////////////////////////////////
	
	
	public void SchedulesTab(String C_Sk,String title,String p_type,String Start_Date,long duration)
	{
		////////////////////// Schedule_Channel_SK/////////////////////
		System.out.print(C_Sk.trim()+"#<>#");
		
//////////////////////Schedule_Program_SK/////////////////////
		System.out.print(title.trim()+"#<>#");
		
	
//////////////////////Schedule_Program_Type/////////////////////
		System.out.print(p_type.trim()+"#<>#");
		
		
//////////////////////Schedule_Start_Datetime/////////////////////
		System.out.print(Start_Date.trim()+"#<>#");
		
		
//////////////////////Schedule_Durations/////////////////////
		
		
		 long durations=(duration*60);
		 int i=(int)durations;
		
		System.out.print(i+"#<>#");
		
//////////////////////Schedule_Attributes/////////////////////
		System.out.print("#<>#");
		
//////////////////////Schedule_Created_At/////////////////////
		System.out.print("#<>#");
		
		
//////////////////////Schedule_Modified_At/////////////////////
		System.out.print("#<>#");
		
//////////////////////Schedule_Last_Seen/////////////////////
		System.out.print("#<>#");
		
		
		/////////////////////////New_Line/////////////////
		
		System.out.print("\n");




		



	
	
	
	}
	
	
	
	public void ScheduleRMTab(String image_sk,String title,String p_type,String url) throws Exception
	
	{
		
		
		
		
		////////////////Rm_SK//////////////////////
		msd.MD5(image_sk.trim());
		
		System.out.print(msd.md5s.trim()+"#<>#");
		
		////////////////Program_SK//////////////////////
		
		//msd.MD5(title.trim());
		//SplitUrl(url);
		System.out.print(title.trim()+"#<>#");
		
		////////////////Program_Type//////////////////////
		
		System.out.print(p_type.trim()+"#<>#");
		
		////////////////Media_Type//////////////////////
		
		System.out.print("image".trim()+"#<>#");
		
		////////////////Image_Type//////////////////////
		
		System.out.print("small".trim()+"#<>#");
		
		////////////////Rm_Size//////////////////////
		
		System.out.print("#<>#");
		
		////////////////Rm_Dimensions//////////////////////
		
		System.out.print("#<>#");
		
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
	
	public void SplitUrlsSD(String names)
	{
		String[] splits=names.split("\\/");
		Splitter_SK=splits[splits.length-1];
		
	}

	
	
	/*
	
	public void SplitTime(String names)
	{
		String[] durst=names.split("\\:");
		String result=durst[durst.length-1];
		int res=Integer.parseInt(result);
		//System.out.println(result);
		String result2=durst[durst.length-2];
		int res2=Integer.parseInt(result2);
		results=res2*60+res;
		//System.out.println(results);
	}
	
	
	
	public void SplitTime2(String names)
	{
		String[] durst=names.split("\\:");
		String result=durst[durst.length-1];
		int res=Integer.parseInt(result);
		//System.out.println(result);
		String result2=durst[durst.length-2];
		int res2=Integer.parseInt(result2);
		results2=res2*60+res;
		//System.out.println(results);
	}
	*/
	
	public void SplitTime(String names)throws Exception
	{
		
		Locale locs=new Locale("es","SPANISH");
		Date  formatter = new SimpleDateFormat("EEEE-dd-MM-yyyy-hh-mm-a",locs).parse(names);
		   SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  results =formatter2.format(formatter);
	  
	
	}
	
	
	
	
	//////////////////////////////////////////////////// TVSHOW TABLE AND MOVIE TABLE DETAILS////////////////////////////////
	
	
	
	public void SchedulesTvshowMovieTabs(String Tv_Sk,String title,String url) throws Exception
	{
		///////////////////////TvShow_Sk////////////////////////////
		
		System.out.print(Tv_Sk.trim()+"#<>#");
		
		
		
///////////////////////TvShow_title////////////////////////////
		
		System.out.print(title.trim()+"#<>#");
		
		
		
///////////////////////TvShow_Original_Title////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Other_Titles////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Description////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Genres////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Sub_Genres////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Category////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Duration////////////////////////////
		
			
		
		
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_Lanaguages////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_Original_Lanaguages////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_MetaData_Language////////////////////////////
		
		System.out.print("Spanish".trim()+"#<>#");
		
		
		
///////////////////////TvShow_Aka////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Production_Country////////////////////////////
		SplitUrlsPDC(url);
		
		System.out.print(Splitter_SK.substring(0, 1).toUpperCase()+Splitter_SK.substring(1).trim()+"#<>#");
		
		
		
		
		
///////////////////////TvShow_Aux_Info////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Reference_Url////////////////////////////
		
		System.out.print(url.trim()+"#<>#");
		
		
		
		
		
		
///////////////////////TvShow_Created_At////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Modified_At////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Last_Seen////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////New Line////////////////////////////
		
		System.out.print("\n");
		
		
	}
	
	///////////////////////////////////////////////////////////////
	
	
	public void SchedulesTvshowMovieTabs(String Tv_Sk,String title,String url,String prg_url) throws Exception
	{
		///////////////////////TvShow_Sk////////////////////////////
		
		System.out.print(Tv_Sk.trim()+"#<>#");
		
		
		
///////////////////////TvShow_title////////////////////////////
		
		System.out.print(title.trim()+"#<>#");
		
		
		
///////////////////////TvShow_Original_Title////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Other_Titles////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Description////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Genres////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////TvShow_Sub_Genres////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Category////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Duration////////////////////////////
		
			
		
		
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_Lanaguages////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_Original_Lanaguages////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
///////////////////////TvShow_MetaData_Language////////////////////////////
		
		System.out.print("Spanish".trim()+"#<>#");
		
		
		
///////////////////////TvShow_Aka////////////////////////////
		
		System.out.print("#<>#");
		
		
		
///////////////////////TvShow_Production_Country////////////////////////////
		SplitUrlsPDC(url);
		
		System.out.print(Splitter_SK.substring(0, 1).toUpperCase()+Splitter_SK.substring(1).trim()+"#<>#");
		
		
		
		
		
///////////////////////TvShow_Aux_Info////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Reference_Url////////////////////////////
		
		System.out.print(prg_url.trim()+"#<>#");
		
		
		
		
		
		
///////////////////////TvShow_Created_At////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Modified_At////////////////////////////
		
		System.out.print("#<>#");
		
		
		
		
		
///////////////////////TvShow_Last_Seen////////////////////////////
		
		System.out.print("#<>#");
		
		
///////////////////////New Line////////////////////////////
		
		System.out.print("\n");
		
		
	}
	

	
	
	
	public void SplitUrlsPDC(String names)
	{
		String[] splits=names.split("\\/");
		String spname=splits[splits.length-1];
		
		
		String[] splits1=spname.split("\\-");
		Splitter_SK=splits1[splits1.length-1];
		
		
	}

	
	

	public void SchedulesTitle(String names)
	{
		//String regex=null;
	
	for(String namesm:myArray)
	{
		
		 
		//regex=namesm;
		
		 
		 /*
		 Pattern pattern = Pattern.compile(names);
		 Matcher matcher = pattern.matcher(regex);
		 if(matcher.find()){
			  
			 
		
			rtitlemd=matcher.group();
			//System.out.println(rtitlemd);
			 
			}
		  */
		 
		 
		   if(names.matches(namesm))
		 {
			   //System.out.println("Welcome to India"+namesm);
			   //System.out.println("Welcome to Hyderabad"+names);
			   
			 rtitlemd=names;
			 
			 //System.out.println(rtitlemd);
			// System.exit(0);
		 }
		
		   else
		   {
				   //System.out.println("Welcome to India"+namesm);
				   //System.out.println("Welcome to Hyderabad"+names);
				   
				 rtitlemd5=names;
				 
				 //System.out.println(rtitlemd);
				// System.exit(0);
			 }
			
				
	}
	
	//System.out.println(regex);
	}
	

}
