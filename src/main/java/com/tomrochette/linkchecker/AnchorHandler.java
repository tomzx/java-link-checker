package com.tomrochette.linkchecker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.net.URL;

public class AnchorHandler implements Callback<String>
{
	private static final Logger logger = LogManager.getLogger(AnchorHandler.class);

	protected URL testedURL;
	protected Element element;
	protected Scheduler scheduler;

	public AnchorHandler(URL testedURL, Element element, Scheduler scheduler)
	{
		this.testedURL = testedURL;
		this.element = element;
		this.scheduler = scheduler;
	}

	@Override
	public void completed(HttpResponse<String> httpResponse)
	{
		logger.debug("AnchorHandler::completed(" + this.testedURL.toString() + ")");
		CrawlDB.getInstance().add(this.testedURL, httpResponse.getStatus());
		String link = this.element.attr("href");
		System.out.println("\t" + httpResponse.getStatus() + "\t" + link + "\t" + this.testedURL.toString() + "\t" + this.element.toString());
		this.scheduler.decrement();
	}

	@Override
	public void failed(UnirestException e)
	{
		logger.debug("AnchorHandler::failed", e);
		this.scheduler.decrement();
	}

	@Override
	public void cancelled()
	{
		logger.debug("AnchorHandler::cancelled");
		this.scheduler.decrement();
	}
}
