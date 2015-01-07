package com.tomrochette.linkchecker;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkChecker
{
	public void crawl(String url) throws MalformedURLException, URISyntaxException, UnirestException
	{
		crawl(new URL(url));
	}

	public void crawl(final URL url) throws UnirestException, URISyntaxException
	{
		Scheduler scheduler = new Scheduler();
		Unirest.get(url.toString()).asStringAsync(new PageHandler(url, scheduler));
		scheduler.increment();
		scheduler.waitForCompletion();
	}
}
