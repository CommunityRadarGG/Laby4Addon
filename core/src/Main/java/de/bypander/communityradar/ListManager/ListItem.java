package de.bypander.communityradar.ListManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ListItem {
  private final String namespace;
  private final String url;
  private final HashMap<String, Player> playerMap;
  private String prefix;
  private final ListType listType;

  public ListItem(String namespace, String prefix, String url, ListType listType) {
    this.namespace = namespace.toLowerCase();
    this.prefix = prefix;
    this.url = url;
    this.listType = listType;
    playerMap = new HashMap<>();
    load();
  }

  /**
   * @param name Name of the player
   * @return if the player is in the list: true, otherwise false.
   */
  public Boolean inList(String name) {
    return playerMap.get(name.toLowerCase()) != null;
  }

  /**
   * @param name Name of the player
   * @return The player object with the given name in the list, otherwise null.
   */
  @Nullable
  public Player getPlayer(String name) {
    return playerMap.get(name.toLowerCase());
  }

  /**
   * @return The namespace of the list.
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return The prefix of the list.
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @return The listtype of the list.
   */
  public ListType getListType() {
    return listType;
  }

  /**
   * @return The URL/FilePath of the list.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return The hashmap with all playername and player pares.
   */
  public HashMap<String, Player> getPlayerMap() {
    return playerMap;
  }

  /**
   * @param prefix New prefix for the list.
   * @return Sets the prefix of a list.
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
    saveList();
  }

  /**
   * @param player Player object, that should be added.
   * @return if player is added: true, otherwise false;
   */
  public Boolean addPlayer(Player player) {
    if (listType != ListType.PRIVATE)
      return false;
    playerMap.put(player.name().toLowerCase(), player);
    saveList();
    return true;
  }

  /**
   * @param name Name of the player, that should be removed
   * @return True, if the player can be removed, False if the player can`t be removed, e.g. he is not in a list.
   */
  public Boolean removePlayer(String name) {
    if (!inList(name))
      return false;

    playerMap.remove(name.toLowerCase());
    saveList();
    return true;
  }

  /**
   * @param player Player that should be loaded from a public list.
   */
  private void loadPlayer(Player player) {
    playerMap.put(player.name().toLowerCase(), player);
  }

  /**
   * Saves the local list into a .json file.
   */
  private void saveList() {
    if (listType != ListType.PRIVATE)
      return;
    new Thread(() -> ListManger.get().saveList(this)).start();
  }

  /**
   * Downloads a Public list.
   */
  private void loadWebList() {
    Gson gson = new Gson();
    try {
      URL url = new URL(this.url);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      List<Player> list = gson.fromJson(reader, new TypeToken<List<Player>>() {}.getType());
      list.forEach(this::loadPlayer);
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * If the list is a public list, the list gets loaded from the api.
   */
  public void load() {
    if (listType == ListType.PUBLIC)
      loadWebList();
  }
}