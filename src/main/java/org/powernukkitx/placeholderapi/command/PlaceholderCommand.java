package org.powernukkitx.placeholderapi.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import org.powernukkitx.placeholderapi.PlaceholderAPI;


import java.util.Map;


public class PlaceholderCommand extends PluginCommand<PlaceholderAPI> {

    public PlaceholderCommand() {
        super("placeholder", PlaceholderAPI.get());
        this.setDescription("Checks a placeholder");
        this.setPermission("placeholderapi.command");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("placeholder", CommandParamType.STRING)
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
