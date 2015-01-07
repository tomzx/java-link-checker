package com.tomrochette.linkchecker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class PageHandler implements Callback<String>
{
	private static final Logger logger = LogManager.getLogger(PageHandler.class);

	protected final boolean internalLinksOnly = true;

	protected URL url;
	protected Scheduler scheduler;

	public PageHandler(URL url, Scheduler scheduler)
	{
		this.url = url;
		this.scheduler = scheduler;
	}

	@Override
	public void completed(HttpResponse<String> httpResponse)
	{
		logger.debug("PageHandler::completed(" + this.url.toString() + ")");
		CrawlDB.getInstance().add(this.url, httpResponse.getStatus());
		System.out.println(httpResponse.getStatus() + "\t" + this.url.toString());
		if (httpResponse.getStatus() != 200)
		{
			logger.error("Target url " + this.url + " did not return a 200, " + httpResponse.getStatus() + " was returned instead.");
			this.scheduler.decrement();
			return;
		}
		URLHelper urlHelper = new URLHelper();
		Document document = Jsoup.parse(httpResponse.getBody());
		Elements anchors = document.select("a[href]");
		for (Element element : anchors)
		{
			String link = element.attr("href");
			URL urlToTest = urlHelper.anchorToAbsoluteURL(this.url.toString(), link);
			Integer status = CrawlDB.getInstance().get(urlToTest);
			if (status != null)
			{
				System.out.println("\t" + status + "\t" + link + "\t" + urlToTest.toString() + "\t" + element.toString());
				continue;
			}
			if (urlHelper.isSameDomain(this.url, urlToTest))
			{
//				Unirest.get(urlToTest.toString()).asStringAsync(new PageHandler(urlToTest, this.scheduler));
				Unirest.head(urlToTest.toString()).asStringAsync(new AnchorHandler(urlToTest, element, this.scheduler));
				this.scheduler.increment();
			}
			else
			{
				if (this.internalLinksOnly)
				{
					continue;
				}
				Unirest.head(urlToTest.toString()).asStringAsync(new AnchorHandler(urlToTest, element, this.scheduler));
				this.scheduler.increment();
			}
		}
		this.scheduler.decrement();
	}

	@Override
	public void failed(UnirestException e)
	{
		logger.debug("PageHandler::failed", e);
		this.scheduler.decrement();
	}

	@Override
	public void cancelled()
	{
		logger.debug("PageHandler::cancelled");
		this.scheduler.decrement();
	}
}
