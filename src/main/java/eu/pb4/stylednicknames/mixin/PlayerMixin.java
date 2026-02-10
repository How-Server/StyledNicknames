package eu.pb4.stylednicknames.mixin;

import eu.pb4.stylednicknames.NicknameHolder;
import eu.pb4.stylednicknames.NicknameCache;
import eu.pb4.stylednicknames.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements NicknameCache {
    @Unique private boolean styledNicknames$ignoreNextCall = false;
    @Unique private Component styledNicknames$cachedName = null;
    @Unique private int styledNicknames$cachedAge = -999;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyArg(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"))
    private Component styledNicknames$replaceName(Component text) {
        try {
            if (ConfigManager.isEnabled() && ConfigManager.getConfig().configData.changeDisplayName) {
                if (this.styledNicknames$cachedAge == this.tickCount) {
                    return this.styledNicknames$cachedName;
                }

                if (this.level().getServer() != null && !this.level().getServer().isSameThread()) {
                    return text;
                }

                if (!this.styledNicknames$ignoreNextCall) {
                    this.styledNicknames$ignoreNextCall = true;
                    var holder = NicknameHolder.of(this);
                    if (holder != null && holder.styledNicknames$shouldDisplay()) {
                        Component name = holder.styledNicknames$getOutput();
                        if (name != null) {
                            this.styledNicknames$ignoreNextCall = false;
                            this.styledNicknames$cachedName = name;
                            this.styledNicknames$cachedAge = this.tickCount;
                            return name;
                        }
                    }
                    this.styledNicknames$ignoreNextCall = false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void styledNicknames$invalidateCache() {
        this.styledNicknames$cachedName = null;
        this.styledNicknames$cachedAge = - 999;
    }
}