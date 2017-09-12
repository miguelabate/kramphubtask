package com.kramphub.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.kramphub.rest.client.JerseyClientGet;
import com.kramphub.rest.model.Product;
import com.kramphub.rest.model.ProductType;
import com.kramphub.utils.PropertiesFileReader;

public class SearchService {

	final static Logger logger = Logger.getLogger(SearchService.class);
	
	private ScheduledExecutorService delayer;
	private PropertiesFileReader propertiesReader;
	
	public SearchService() {
		super();
		delayer = Executors.newScheduledThreadPool(2);
		propertiesReader = new PropertiesFileReader();
	}

	public ArrayList<Product> doSearch(String query) {
		 CompletableFuture<ArrayList<Product>> bookSearchFuture
		  = CompletableFuture.supplyAsync(() -> {
			  BookSearchService bss= new BookSearchService(propertiesReader.getBookSearchServiceURL(),propertiesReader.getMaxResults());
			   ArrayList<Product> listOfBooks = bss.search(query);
			   logger.info("Results prods");
			  	return listOfBooks;
			  }).exceptionally(e->{
				  logger.error("Exception...");
		          return new ArrayList<Product>();
		        });
		 
		 CompletableFuture<ArrayList<Product>> albumSearchFuture
		  = CompletableFuture.supplyAsync(() -> {
			  	return new ArrayList<Product>();
			  }).exceptionally(e->{
				  logger.error("Exception...");
		          return new ArrayList<Product>();
		        });
		 
		 logger.info("create timeouts");
		 
		 CompletableFuture<Object> bookSearchFutureWithTimeout = CompletableFuture.anyOf(bookSearchFuture, timeoutAfter(propertiesReader.getTimeout(),TimeUnit.SECONDS));
		 CompletableFuture<Object> albumSearchFutureWithTimeout = CompletableFuture.anyOf(albumSearchFuture, timeoutAfter(propertiesReader.getTimeout(),TimeUnit.SECONDS));
		 
		 CompletableFuture<Void> combinedFuture 
		  = CompletableFuture.allOf(bookSearchFutureWithTimeout, albumSearchFutureWithTimeout);
		 
		 logger.info("Getting combined fut");
		 
		 try {
			combinedFuture.get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		 ArrayList<Product> result = new ArrayList<Product>();
		 try {
			Object booksResult = bookSearchFutureWithTimeout.get();
			if(booksResult instanceof String) {
			}else {
				result.addAll((Collection<? extends Product>) booksResult);
			}
			Object albumResults = albumSearchFutureWithTimeout.get();
			if(albumResults instanceof String) {
			}else {
				result.addAll((Collection<? extends Product>) albumResults);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		delayer.shutdownNow();
		return result;
	}
	
	private CompletableFuture<String> timeoutAfter(long timeout, TimeUnit unit) {
	    CompletableFuture<String> result = new CompletableFuture<String>();
	    delayer.schedule(() -> result.complete("Timeout"), timeout, unit);
	    return result;
	}
	
	private ArrayList<Product> buildMockResultProductList() {
		ArrayList<Product> result = new ArrayList<Product>();
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Iron maiden");
		Product p1 = new Product("Number of the beast", authors, ProductType.ALBUM);
		result.add(p1);
		
		authors = new ArrayList<String>();
		authors.add("Philip K dick");
		authors.add("Others");
		Product p2 = new Product("Deus Irae", authors, ProductType.BOOK);
		result.add(p2);
		
		return result;
	}
}
