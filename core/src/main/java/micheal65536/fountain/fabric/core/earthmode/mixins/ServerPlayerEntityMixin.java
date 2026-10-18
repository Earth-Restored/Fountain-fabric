package micheal65536.fountain.fabric.core.earthmode.mixins;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements EarthModePlayer {
    private boolean earthMode = false;

    @Override
    public boolean isEarthMode() {
        return this.earthMode;
    }

    @Override
    public void setEarthMode(boolean earthMode) {
        this.earthMode = earthMode;
    }

    public void applyAbilitiesForEarthMode() {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerAbilities abilities = player.getAbilities();
        player.interactionManager.getGameMode().setAbilities(abilities);

        var blockReach = player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
        var entityReach = player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);

        if (this.earthMode) {
            abilities.allowFlying = true;
            abilities.flying = true;
            abilities.creativeMode = false;

            if (blockReach != null)
                blockReach.setBaseValue(Double.MAX_VALUE);
            if (entityReach != null)
                entityReach.setBaseValue(Double.MAX_VALUE);
        } else {
            if (blockReach != null)
                blockReach.setBaseValue(4.5);
            if (entityReach != null)
                entityReach.setBaseValue(3.0);
        }
    }

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At(value = "HEAD"))
    private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        this.setEarthMode(((EarthModePlayer) oldPlayer).isEarthMode());
    }
}