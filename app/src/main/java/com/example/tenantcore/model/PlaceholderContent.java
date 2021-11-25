package com.example.tenantcore.model;

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlaceholderContent {
  public static final List<PlaceholderItem> ITEMS = new ArrayList<>();
  public static final Map<Integer, PlaceholderItem> ITEM_MAP = new HashMap<>();
  private static final int COUNT = 25;

  static {
    // For all random values
    Random random = new Random();

    // Number of requests info
    int maxRequests = 25;
    int requestNum = 0;

    // Max/Min request description words
    int minRequestDesc = 10;
    int maxRequestDesc = 25;

    // Max/Min request due date
    long minRequestDate = 1639526400000L;
    long maxRequestDate = 1671062400000L;

    // The request to add
    Request newRequest;

    // Add some sample items.
    for (int i = 1; i <= COUNT; i++) {
      List<Request> requestList = new ArrayList<>();
      requestNum = random.nextInt(maxRequests);

      // Adding random numbers of requests for each Tenant (for now it's called PlaceHolderItem)
      for(; requestNum > 0; requestNum--){

        // Adding random values for the new request
        LoremIpsum loremIpsum = new LoremIpsum(random.nextInt(maxRequestDesc-minRequestDesc)+minRequestDesc);
        newRequest = new Request("Request "+requestNum, loremIpsum.toString(), new Date(ThreadLocalRandom.current().nextLong(minRequestDate, maxRequestDate)));
newRequest.setPriority(Priority.values()[random.nextInt(Priority.HIGH.ordinal()]);

        // Adding each request of PlaceHolderItem to list
        requestList.add(newRequest);
      }

      addItem(createPlaceholderItem(i, requestList));

    }
  }

  private static void addItem(PlaceholderItem item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.id, item);
  }

  private static PlaceholderItem createPlaceholderItem(int position, List<Request> requests) {
    return new PlaceholderItem(position, "Item " + position, makeDetails(position), requests);
  }

  private static String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position);
    for (int i = 0; i < position; i++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }

  public static class PlaceholderItem {
    private final int id;
    private final String content;
    private final String details;
    private final List<Request> requests;

    public PlaceholderItem(int id, String content, String details, List<Request> requests) {
      this.id = id;
      this.content = content;
      this.details = details;
      this.requests = requests;
    }

    public int getId() {
      return id;
    }

    public List<Request> getRequests() {
      return requests;
    }

    public String getContent() {
      return content;
    }

    public String getDetails() {
      return details;
    }

    @Override
    public String toString() {
      return content;
    }
  }
}
