package org.powernukkitx.placeholderapi;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.types.Platform;
import cn.nukkit.plugin.PluginBase;
import org.powernukkitx.placeholderapi.command.PlaceholderCommand;
import org.powernukkitx.placeholderapi.placeholder.Placeholder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPI extends PluginBase {

    private static PlaceholderAPI INSTANCE;
    private final Map<String, Placeholder> placeholders = new HashMap<>();
    private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)(?:;([^%]*))?%");

    public static PlaceholderAPI get() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
        registerDefaults();
        getServer().getCommandMap().register("placeholder", new PlaceholderCommand());
    }

    public void register(String identifier, Placeholder placeholder) {
        placeholders.put(identifier.toLowerCase(), placeholder);
    }

    public boolean containsPlaceholder(String text) {
        return PLACEHOLDER_PATTERN.matcher(text).find();
    }

    public String processPlaceholders(Player player, String text) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String identifier = matcher.group(1).toLowerCase();
            String paramsRaw = matcher.group(2);

            Placeholder placeholder = placeholders.get(identifier);
            if (placeholder != null) {
                String[] params = paramsRaw != null ? paramsRaw.split(";") : new String[0];
                String replacement = placeholder.process(player, params);
                matcher.appendReplacement(result, replacement != null ? replacement : "");
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    private void registerDefaults() {
        this.register("player", (player, params) -> player.getName());
        this.register("player_displayname", (player, params) -> player.getDisplayName());
        this.register("player_uuid", (player, params) -> player.getUniqueId().toString());
        this.register("player_ping", (player, params) -> String.valueOf(player.getPing()));
        this.register("player_level", (player, params) -> player.getLevel().getName());
        this.register("player_health", (player, params) -> String.valueOf(player.getHealth()));
        this.register("player_max_health", (player, params) -> String.valueOf(player.getMaxHealth()));
        this.register("player_saturation", (player, params) -> String.valueOf(player.getFoodData().getSaturation()));
        this.register("player_food", (player, params) -> String.valueOf(player.getFoodData().getFood()));
        this.register("player_max_food", (player, params) -> String.valueOf(player.getFoodData().getMaxFood()));
        this.register("player_gamemode", (player, params) -> String.valueOf(player.getGamemode()));
        this.register("player_exp", (player, params) -> String.valueOf(player.getExperience()));
        this.register("player_exp_level", (player, params) -> String.valueOf(player.getExperienceLevel()));
        this.register("player_platform", (player, params) -> Platform.getPlatformByID(player.getLoginChainData().getDeviceOS()).getName());
        this.register("server_online", (player, params) -> String.valueOf(getServer().getOnlinePlayers().size()));
        this.register("server_max_players", (player, params) -> String.valueOf(getServer().getMaxPlayers()));
        this.register("server_motd", (player, params) -> getServer().getMotd());
        this.register("server_tps", (player, params) -> String.valueOf(getServer().getTicksPerSecond()));
        this.register("player_pos", (p, arg) -> {
            if (arg.length == 0) return p.getFloorX() + " " + p.getFloorY() + " " + p.getFloorZ();
            return switch (arg[0]) {
                case "x" -> String.valueOf(p.getFloorX());
                case "y" -> String.valueOf(p.getFloorY());
                case "z" -> String.valueOf(p.getFloorZ());
                default -> "unknown";
            };
        });
        this.register("time",(player, params) -> new SimpleDateFormat(params.length == 0 ? "yyyy-MM-dd HH:mm:ss" : params[0]).format(new Date()));
    }
}
