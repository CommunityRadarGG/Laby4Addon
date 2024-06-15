package de.bypander.communityradar.ListManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.labymod.api.client.component.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Dear Labymod Team,
 * In this class, a server is being accessed to resolve a player name to a given UUID. This is necessary to update the locally stored lists on startup.
 */
public class ListItem {
  private final String namespace;
  private final String url;
  private final HashMap<String, Player> playerMap;
  private TextComponent prefix;
  private final ListType listType;

  public ListItem(String namespace, TextComponent prefix, String url, ListType listType) {
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
  public TextComponent getPrefix() {
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
  public boolean setPrefix(TextComponent prefix) {
    if (listType != ListType.PRIVATE) return false;
    this.prefix = prefix;
    saveList();
    return true;
  }

  /**
   * @param prefix New prefix for the list.
   * @return Sets the prefix of a list in the configs.
   */
  public boolean setPrefixInConfig(TextComponent prefix) {
    if (listType != ListType.PUBLIC) return false;
    this.prefix = prefix;
    saveList();
    return true;
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
  private void loadPublicList() {
    Gson gson = new GsonBuilder().registerTypeAdapter(TextComponent.class, new TextComponentAdapter())
                                 .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                                 .setPrettyPrinting().create();
    try {
      URL url = new URL(this.url);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      List<Player> list = gson.fromJson(reader, new TypeToken<List<Player>>() {
      }.getType());
      list.forEach(this::loadPlayer);
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks all player names in the list and updates them if needed.
   */
  public void updateNames() {
    try {
      List<Map.Entry<String, String>> entryList = new ArrayList<>();
      for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
        Player p = entry.getValue();
        if (p.name().startsWith("!"))
          continue;

        String newName = "";
        newName = getNameFromUUID(p.uuid());

        if (!newName.isEmpty() && !p.name().equals(newName)) {
          entryList.add(Map.entry(entry.getKey(), newName));
        }
      }
      for (Map.Entry<String, String> entry : entryList) {
        String key = entry.getKey();
        String newName = entry.getValue();
        Player p = playerMap.get(key);
        playerMap.remove(key);
        assert p != null;
        p.setName(newName);
        p.setEntryUpdatedAt(LocalDateTime.now());
        playerMap.put(newName.toLowerCase(), p);
      }
      saveList();
    } catch (Exception e) {
      System.out.println("Failed to update List " + getNamespace() + ". " + e.toString());
    }
  }

  /**
   * @param uuid The uuid of the player, the name should be returned
   * @return Empty String, if no matching name is found. Or the matching name to the uuid.
   */
  private String getNameFromUUID(String uuid) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.minetools.eu/uuid/" + uuid).openStream()));
      JsonObject json = new Gson().fromJson(reader, JsonObject.class);

      if (json == null) throw new Exception("No response for uuid: " + uuid);
      if (!json.has("name") || !json.has("status") || !json.get("status").getAsString().equals("OK"))
        throw new Exception("Invalid response: " + json);

      return json.get("name").getAsString();
    } catch (Exception e) {
      System.out.println("Could not get name from minetools api: " + e);
    }
    return "";
  }

  /**
   * If the list is a public list, the list gets loaded from the api.
   * If it is a private list, all player names get updated with the uuid.
   */
  public void load() {
    if (listType == ListType.PUBLIC)
      loadPublicList();
  }
}