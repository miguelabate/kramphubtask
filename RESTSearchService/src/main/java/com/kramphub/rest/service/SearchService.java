package com.kramphub.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.service.exceptions.SearchTimeoutException;
import com.kramphub.utils.PropertiesFileReader;

/**
 * Service class in charge of initiating the async search for the products
 * (albums and books). Using CompletableFutures.
 * 
 * @author miguel
 *
 */
public class SearchService {

	final static Logger logger = Logger.getLogger(SearchService.class);

	private ScheduledExecutorService delayer;
	private PropertiesFileReader propertiesReader;

	public SearchService() {
		super();
		delayer = Executors.newScheduledThreadPool(2);
		propertiesReader = new PropertiesFileReader();
	}

	public List<Product> doSearch(String query) {
		logger.info("Creating search tasks");

		CompletableFuture<ArrayList<Product>> bookSearchFuture = createCompletableFutureToSearchProducts(
				new BookSearchService(propertiesReader.getBookSearchServiceURL(), propertiesReader.getMaxResults()),
				query);
		CompletableFuture<ArrayList<Product>> albumSearchFuture = createCompletableFutureToSearchProducts(
				new AlbumSearchService(propertiesReader.getAlbumSearchServiceURL(), propertiesReader.getMaxResults()),
				query);

		logger.info("Creating timeouts. Combining them with each search future.");

		CompletableFuture<Object> bookSearchFutureWithTimeout = CompletableFuture.anyOf(bookSearchFuture,
				timeoutAfter(propertiesReader.getTimeout(), TimeUnit.SECONDS));
		CompletableFuture<Object> albumSearchFutureWithTimeout = CompletableFuture.anyOf(albumSearchFuture,
				timeoutAfter(propertiesReader.getTimeout(), TimeUnit.SECONDS));

		// add them to a list to handle them later
		List<CompletableFuture<?>> completableFuturesSearchTasks = new ArrayList<CompletableFuture<?>>();
		completableFuturesSearchTasks.add(bookSearchFutureWithTimeout);
		completableFuturesSearchTasks.add(albumSearchFutureWithTimeout);

		// using the combined future (allOf) as a way of waiting for the services or
		// waiting for the timeout, whatever happens first.
		// The caveat is that I have to fetch the results from each completableFuture

		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(bookSearchFutureWithTimeout,
				albumSearchFutureWithTimeout);

		try {
			combinedFuture.get();
		} catch (Exception e) {
			logger.error("Error getting the value of the search.", e);
		}

		List<Product> result = new ArrayList<Product>();
		// iterate the futures list to collect the results or timeouts
		for (CompletableFuture<?> completableFuture : completableFuturesSearchTasks) {
			try {
				result.addAll((Collection<? extends Product>) completableFuture.get());
			} catch (ExecutionException e) {
				logger.error("Error Timeout Exception: " + e.getCause().getMessage(), e);
			} catch (InterruptedException e1) {
				logger.error("Error getting results", e1);
			}

		}

		delayer.shutdownNow();
		Collections.sort(result);
		return result;
	}

	/**
	 * Creates a completable future that timeouts
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	private CompletableFuture<String> timeoutAfter(long timeout, TimeUnit unit) {
		CompletableFuture<String> result = new CompletableFuture<String>();
		delayer.schedule(() -> result.completeExceptionally(new SearchTimeoutException("Search timeout")), timeout,
				unit);
		return result;
	}

	private CompletableFuture<ArrayList<Product>> createCompletableFutureToSearchProducts(ProductSearchService service,
			String query) {
		CompletableFuture<ArrayList<Product>> searchFuture = CompletableFuture.supplyAsync(() -> {
			ArrayList<Product> results = service.search(query);
			logger.info("Product search completed succesfully. Returned: " + results.size() + " results");
			return results;
		}).exceptionally(e -> {
			logger.error("Product search failed.");
			return new ArrayList<Product>();
		});
		return searchFuture;

	}

}
