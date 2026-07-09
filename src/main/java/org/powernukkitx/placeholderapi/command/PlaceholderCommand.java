package org.powernukkitx.placeholderapi.command;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.utils.CommandLogger;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import org.powernukkitx.placeholderapi.PlaceholderAPI;


import java.util.Map;


public class PlaceholderCommand extends PluginCommand<PlaceholderAPI> {

    public PlaceholderCommand() {
        super("placeholder", PlaceholderAPI.get());
        this.setDescription("Checks a placeholder");
        this.setPermission("placeholderapi.command");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("placeholder", CommandParamType.ID)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        if(sender instanceof Player player) {
            log.addSuccess(PlaceholderAPI.get().processPlaceholders(player, list.getResult(0)));
        } else log.addError("This command can only be executed by a player.");
        log.output();
        return 1;
    }
}
