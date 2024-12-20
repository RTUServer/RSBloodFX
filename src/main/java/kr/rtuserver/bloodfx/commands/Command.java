package kr.rtuserver.bloodfx.commands;

import kr.rtuserver.bloodfx.RSBloodFX;
import kr.rtuserver.bloodfx.configuration.EffectConfig;
import kr.rtuserver.bloodfx.configuration.ParticleConfig;
import kr.rtuserver.bloodfx.particle.ToggleManager;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.utility.player.PlayerChat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command extends RSCommand {

    private final EffectConfig effectConfig;
    private final ParticleConfig particleConfig;
    private final ToggleManager toggleManager;

    public Command(RSBloodFX plugin) {
        super(plugin, "rsbfx", true);
        this.effectConfig = plugin.getEffectConfig();
        this.particleConfig = plugin.getParticleConfig();
        this.toggleManager = plugin.getToggleManager();
    }

    @Override
    public boolean execute(RSCommandData data) {
        PlayerChat chat = PlayerChat.of(getPlugin());
        String on = getCommand().get(getSender(), "toggle.enable");
        String off = getCommand().get(getSender(), "toggle.disable");
        if (data.equals(0, on)) {
            if (hasPermission("rsbfx.toggle")) {
                if (getSender() instanceof Player player) {
                    toggleManager.on(player.getUniqueId());
                    chat.announce(getSender(), getMessage().get(getSender(), "toggle.enable"));
                    return true;
                } else chat.announce(getAudience(), getCommon().getMessage(getSender(), "onlyPlayer"));
            } else chat.announce(getAudience(), getCommon().getMessage(getSender(), "noPermission"));
        }
        if (data.equals(0, off)) {
            if (hasPermission("rsbfx.toggle")) {
                if (getSender() instanceof Player player) {
                    toggleManager.off(player.getUniqueId());
                    chat.announce(getSender(), getMessage().get(getSender(), "toggle.disable"));
                    return true;
                } else chat.announce(getAudience(), getCommon().getMessage(getSender(), "onlyPlayer"));
            } else chat.announce(getAudience(), getCommon().getMessage(getSender(), "noPermission"));
        }
        return false;
    }

    @Override
    public void reload(RSCommandData data) {
        effectConfig.reload();
        particleConfig.reload();
    }

    @Override
    public void wrongUsage(RSCommandData data) {
        PlayerChat chat = PlayerChat.of(getPlugin());
        if (hasPermission("rsbfx.toggle")) {
            chat.send(getAudience(), getMessage().get(getSender(), "wrongUsage.toggle.enable"));
            chat.send(getAudience(), getMessage().get(getSender(), "wrongUsage.toggle.disable"));
        }
    }

    @Override
    public List<String> tabComplete(RSCommandData data) {
        String on = getCommand().get(getSender(), "toggle.enable");
        String off = getCommand().get(getSender(), "toggle.disable");
        if (data.length(1)) {
            List<String> list = new ArrayList<>();
            if (hasPermission("rsbfx.toggle")) {
                list.add(on);
                list.add(off);
            }
            return list;
        }
        return List.of();
    }
}
