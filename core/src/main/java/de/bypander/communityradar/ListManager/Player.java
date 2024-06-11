package de.bypander.communityradar.ListManager;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Player {
  private String name;
  private final String cause;
  private final String uuid;
  private final LocalDateTime entryCreatedAt;
  private LocalDateTime entryUpdatedAt;
  private final int expiryDays;

  public Player(String name, String cause, String uuid, LocalDateTime entryCreatedAt, LocalDateTime entryUpdatedAt, int expiryDays) {
    this.name = name;
    this.cause = cause;
    this.uuid = uuid;
    this.entryCreatedAt = entryCreatedAt;
    this.entryUpdatedAt = entryUpdatedAt;
    this.expiryDays = expiryDays;
  }

  public LocalDateTime entryCreatedAt() {
    return entryCreatedAt;
  }

  public LocalDateTime entryUpdatedAt() {
    return entryUpdatedAt;
  }

  public int expiryDays() {
    return expiryDays;
  }

  public void setEntryUpdatedAt(LocalDateTime entryUpdatedAt) {
    this.entryUpdatedAt = entryUpdatedAt;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public String cause() {
    return cause;
  }

  public String uuid() {
    return uuid;
  }

}