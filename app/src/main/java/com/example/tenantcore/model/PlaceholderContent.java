package com.example.tenantcore.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContent {
  public static final List<PlaceholderItem> ITEMS = new ArrayList<>();
  public static final Map<Integer, PlaceholderItem> ITEM_MAP = new HashMap<>();
  private static final int COUNT = 25;

  static {
    // Add some sample items.
    for (int i = 1; i <= COUNT; i++) {
      addItem(createPlaceholderItem(i));
    }
  }

  private static void addItem(PlaceholderItem item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.id, item);
  }

  private static PlaceholderItem createPlaceholderItem(int position) {
    return new PlaceholderItem(position, "Item " + position, makeDetails(position));
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

    public PlaceholderItem(int id, String content, String details) {
      this.id = id;
      this.content = content;
      this.details = details;
    }

    public int getId() {
      return id;
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
