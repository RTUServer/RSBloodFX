package kr.rtuserver.bloodfx;

import com.comphenix.protocol.ProtocolLibrary;
import kr.rtuserver.bloodfx.commands.Command;
import kr.rtuserver.bloodfx.configuration.EffectConfig;
import kr.rtuserver.bloodfx.configuration.ParticleConfig;
import kr.rtuserver.bloodfx.dependency.PlaceholderAPI;
import kr.rtuserver.bloodfx.listeners.EntityDamageByEntity;
import kr.rtuserver.bloodfx.listeners.PlayerJoinQuit;
import kr.rtuserver.bloodfx.listeners.ProjectileHit;
import kr.rtuserver.bloodfx.packet.BloodHeartParticle;
import kr.rtuserver.bloodfx.particle.ToggleManager;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import lombok.Getter;
import org.bukkit.permissions.PermissionDefault;

public class RSBloodFX extends RSPlugin {

    @Getter
    private static RSBloodFX instance;
    @Getter
    private EffectConfig effectConfig;
    @Getter
    private ParticleConfig particleConfig;
    @Getter
    private ToggleManager toggleManager;

    private BloodHeartParticle packetListener;
    private PlaceholderAPI placeholder;

    @Override
    public void enable() {
        instance = this;
        getConfigurations().initStorage("Toggle");

        registerPermission("rsbfx.toggle", PermissionDefault.TRUE);

        effectConfig = new EffectConfig(this);
        particleConfig = new ParticleConfig(this);

        toggleManager = new ToggleManager(this);

        registerEvent(new PlayerJoinQuit(this));
        registerEvent(new EntityDamageByEntity(this));
        registerEvent(new ProjectileHit(this));
        registerCommand(new Command(this));

        if (getFramework().isEnabledDependency("ProtocolLib")) {
            packetListener = new BloodHeartParticle(this);
            //ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
            BloodHeartParticle particle = new BloodHeartParticle(this); // its okay
            ProtocolLibrary.getProtocolManager().addPacketListener(null); // its okay
            //ProtocolLibrary.getProtocolManager().addPacketListener(particle); // crash in here!
        }
        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder = new PlaceholderAPI(this);
            placeholder.register();
        }
    }

    @Override
    public void disable() {
        if (getFramework().isEnabledDependency("ProtocolLib")) {
            ProtocolLibrary.getProtocolManager().removePacketListener(packetListener); // CRASH DUE TO THIS wtf
        }

        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder.unregister();
        }
    }
}
