package de.bypander.communityradar.ListManager;

import com.google.gson.*;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;

import java.lang.reflect.Type;

public class TextComponentAdapter implements JsonSerializer<TextComponent>, JsonDeserializer<TextComponent>{

  /** {@inheritDoc} */
  @Override
  public TextComponent deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    return Component.text(json.getAsString());
  }

  /** {@inheritDoc} */
  @Override
  public JsonElement serialize(final TextComponent textComponent, final Type typeOfSrc, final JsonSerializationContext context) {
    return  new JsonPrimitive(textComponent.getText());
  }
}
