package com.tomrochette.linkchecker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;

public class CrawlDB
{
	private static final Logger logger = LogManager.getLogger(CrawlDB.class);
	protected static CrawlDB instance;
	protected HashMap<String, Integer> results;

	public CrawlDB()
	{
		this.results = new HashMap<String, Integer>();
	}

	public static CrawlDB getInstance()
	{
		if (instance == null)
		{
			instance = new CrawlDB();
		}
		return instance;
	}

	public void add(URL url, int result)
	{
		this.results.put(url.toString(), result);
	}

	public Integer get(URL url)
	{
		return this.results.get(url.toString());
	}

	public int getUniqueUrlCount()
	{
		return this.results.size();
	}
}
