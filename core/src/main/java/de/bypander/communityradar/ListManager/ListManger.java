package de.bypander.communityradar.ListManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.bypander.communityradar.listener.GsonLocalDateTimeAdapter;
import net.labymod.api.Laby;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Dear Labymod Team,
 * In this class, the user's filesystem is accessed, and potential data is stored or deleted. It is necessary to
 * store the local lists that the user creates or imports and load them back on restart.
 * Additionally, servers are accessed to load public lists or to load UUIDs for players.
 */
public class ListManger {

  private final List<ListItem> lists;
  private static ListManger listManger;
  private final String folderurl;

  public ListManger(String folderurl) {
    lists = new ArrayList<>();
    this.folderurl = folderurl;
    listManger = this;
    new Thread(this::loadLists).start();
  }

  /**
   * @param name Name of the player
   * @return if the player is in any list: true, otherwise false.
   */
  public Boolean inList(String name) {
    for (ListItem listItem : lists) {
      if (listItem.inList(name))
        return true;
    }
    return false;
  }

  /**
   * @param name Name of the player
   * @return If found: Prefix to add otherwise empty String.
   */
  public String getPrefix(String name) {
    for (ListItem listItem : lists) {
      if (listItem.inList(name))
        return listItem.getPrefix() + " ";
    }
    return "";
  }

  /**
   * @return List with all used namespaces.
   */
  public List<String> getNamespaces() {
    List<String> namespaces = new ArrayList<>();
    lists.forEach(element -> namespaces.add(element.getNamespace()));
    return namespaces;
  }

  /**
   * @param name Name of the player
   * @return The first player object with the given name in any list, otherwise null
   */
  @Nullable
  public Player getPlayer(String name) {
    for (ListItem listItem : lists) {
      if (listItem.inList(name))
        return listItem.getPlayer(name);
    }
    return null;
  }

  /**
   * @param namespace Namespace of the list
   * @return If ListItem with given namespace is found: ListItem, otherwise null.
   */
  @Nullable
  public ListItem getListItem(String namespace) {
    for (ListItem listItem : lists) {
      if (listItem.getNamespace().equals(namespace.toLowerCase()))
        return listItem;
    }
    return null;
  }

  /**
   * @param namespace NameSpace of the list, that the player should be added to.
   * @param name      Name of the player, that should be added.
   * @param notice    Possible reason, for that the player got added.
   * @return if player was added: true, otherwise false.
   */
  public Boolean addPlayer(String namespace, String name, String notice) {
    if (getPlayer(name) != null) return false;
    for (ListItem listItem : lists) {
      if (listItem.getNamespace().equals(namespace.toLowerCase())) {
        if (listItem.getListType() != ListType.PRIVATE)
          return false;
        LocalDateTime date = LocalDateTime.now();
        new Thread(() -> listItem.addPlayer(new Player(name, notice, getUUID(name), date, date , -1))).start();
        return true;
      }
    }
    return false;
  }

  /**
   * @param name Name of the player, who's UUID is searched.
   * @return If UUID found: UUID, otherwise null.
   */
  @Nullable
  private String getUUID(String name) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.minetools.eu/uuid/" + name).openStream()));
      JsonObject json = new Gson().fromJson(reader, JsonObject.class);

      if (json == null) throw new Exception("No response for name " + name);
      if (!json.has("id") || !json.has("status") || !json.get("status").getAsString().equals("OK"))
        throw new Exception("Invalid response: " + json);

      String uuid = json.get("id").getAsString();
      return Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})").matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
    } catch (Exception e) {
      System.out.println("Could not get uuid from minetools api: " + e);
    }
    return null;
  }

  /**
   * Used to save a ListItem local, so that it gets reloaded on restart.
   * @param listItem ListItem, that should be locally saved.
   */
  public void saveList(ListItem listItem) {
    if (listItem.getListType() != ListType.PRIVATE)
      return;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).setPrettyPrinting().create();
    String json = gson.toJson(listItem);
    try (FileWriter writer = new FileWriter(folderurl + listItem.getNamespace() + ".json")) {
      writer.write(json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a private list, that gets local stored.
   *
   * @param namespace Namespace of the list.
   * @param prefix    Prefix that should be added before the player name.
   * @return If list gets added: true, otherwise false.
   */
  public Boolean addPrivateList(@NotNull String namespace, @NotNull String prefix) {
    if (namespace.toLowerCase().endsWith("settings"))
      return false;
    for (String s : getNamespaces()) {
      if (s.equals(namespace.toLowerCase())) return false;
    }
    lists.add(0, new ListItem(namespace, prefix, folderurl + namespace + ".json", ListType.PRIVATE));
    new Thread(() -> saveList(getListItem(namespace))).start();
    return true;
  }

  /**
   * Add a public list with a .json api. Those need to be added everytime on startup.
   *
   * @param namespace Namespace of the list.
   * @param prefix    Prefix that should be added before the player name.
   * @return If list gets added: true, otherwise false.
   */
  public Boolean addPublicList(@NotNull String namespace, @NotNull String prefix, @NotNull String url) {
    for (String s : getNamespaces()) {
      if (s.equals(namespace.toLowerCase())) return false;
    }
    lists.add(new ListItem(namespace, prefix, url, ListType.PUBLIC));
    return true;
  }

  /**
   * Used to add an existing list to the lists
   *
   * @param listItem ListItem, that should be added.
   */
  private void addList(ListItem listItem) {
    if (listItem == null) return;
    lists.add(listItem);
    if (listItem.getListType() == ListType.PRIVATE)
      new Thread(listItem::updateNames).start();
  }

  /**
   * @param name Name of the player, that should be removed
   * @return True, if the player can be removed, False if the player can`t be removed, e.g. he is not in a list.
   */
  public Boolean removePlayer(String name) {
    if (!inList(name))
      return false;

    for (ListItem item : lists) {
      if (item.getListType() != ListType.PRIVATE)
        continue;
      if (item.inList(name)) {
        return item.removePlayer(name);
      }
    }
    return false;
  }

  /**
   * Unloads the list and removes the private save file
   *
   * @param namespace Namespace of the list, that should be removed.
   * @return If list is removed: true, otherwise: false.
   */
  public Boolean removeList(String namespace) {
    ListItem item = getListItem(namespace);
    if (item == null) return false;
    if (item.getListType() == ListType.PRIVATE) {
      File file = new File(item.getUrl());
      if (file.exists()) {
        file.delete();
      }
      lists.remove(item);
      return true;
    }
    return false;
  }

  /**
   * Loads all local stored lists.
   */
  private void loadLists() {
    for (String s : getJsonURLs(folderurl)) {
      ListItem item = loadListItemFromFile(s.substring(8));
      if (item == null)
        continue;
      addList(item);
    }
  }

  /**
   * @param fileurl Fileurl of the location the ListItem should be loaded from.
   * @return if found: ListItem, otherwise null.
   */
  @Nullable
  private ListItem loadListItemFromFile(String fileurl) {
    if (fileurl.endsWith("settings.json"))
      return null;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
    try {
      FileReader reader = new FileReader(fileurl.replace("%20", " "));
      System.out.println("Loaded list from Path: " + fileurl.replace("%20", " "));
      return gson.fromJson(reader, new TypeToken<ListItem>() {}.getType());
    } catch (IOException | IllegalStateException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param folderPath
   * @return List with the filepath of all local stored lists (json files).
   */
  private List<String> getJsonURLs(String folderPath) {
    try {
      List<Path> jsonFiles = Files.walk(Paths.get(folderPath))
        .filter(Files::isRegularFile)
        .filter(path -> path.toString().endsWith(".json"))
        .toList();

      return jsonFiles.stream()
        .map(Path::toUri)
        .map(URI::toString)
        .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<String>();
  }

  /**
   * @return listManager
   */
  public static ListManger get() {
    return listManger;
  }
}