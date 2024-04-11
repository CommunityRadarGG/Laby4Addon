package de.bypander.communityradar.ListManager;

import java.util.List;

public record Player(String name, String notice, List<String> date, String uuid) {
}