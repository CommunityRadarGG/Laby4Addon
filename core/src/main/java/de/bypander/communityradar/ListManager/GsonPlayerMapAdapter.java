package de.bypander.communityradar.ListManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonPlayerMapAdapter implements JsonSerializer<Map<String, Player>>, JsonDeserializer<Map<String, Player>> {

  /** {@inheritDoc} */
  @Override
  public Map<String, Player> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final JsonArray playerMapJsonArray = json.getAsJsonArray();
    final Map<String, Player> playerMap = new HashMap<>();

    playerMapJsonArray.forEach(jsonElement -> {
      final Player entry = context.deserialize(jsonElement, Player.class);
      playerMap.put(entry.name().toLowerCase(), entry);
    });
    return playerMap;
  }

  /** {@inheritDoc} */
  @Override
  public JsonElement serialize(final Map<String, Player> playerMap, final Type typeOfSrc, final JsonSerializationContext context) {
    return context.serialize(playerMap.values());
  }
}