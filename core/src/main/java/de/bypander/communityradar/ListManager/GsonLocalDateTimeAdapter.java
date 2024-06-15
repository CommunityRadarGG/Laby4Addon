package de.bypander.communityradar.ListManager;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class with an adapter for serialization and deserialization of the class {@link LocalDateTime} for the GSON library.
 */
public class GsonLocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
  /** {@inheritDoc} */
  @Override
  public LocalDateTime deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
  }

  /** {@inheritDoc} */
  @Override
  public JsonElement serialize(final LocalDateTime localDateTime, final Type typeOfSrc, final JsonSerializationContext context) {
    return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
