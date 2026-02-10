package eu.pb4.stylednicknames.mixin;

import com.mojang.authlib.GameProfile;
import eu.pb4.stylednicknames.NicknameHolder;
import eu.pb4.stylednicknames.config.ConfigManager;
import eu.pb4.stylednicknames.config.data.ConfigData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "getTabListDisplayName", at = @At("TAIL"), cancellable = true)
    private void styledNicknames$replacePlayerListName(CallbackInfoReturnable<Component> cir) {
        try {
            if (ConfigManager.isEnabled()) {
                ConfigData data = ConfigManager.getConfig().configData;
                if (data.changePlayerListName) {
                    var holder = NicknameHolder.of(this);
                    if (holder != null && holder.styledNicknames$shouldDisplay()) {
                        cir.setReturnValue(PlayerTeam.formatNameForTeam(this.getTeam(), holder.styledNicknames$getOutput()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
