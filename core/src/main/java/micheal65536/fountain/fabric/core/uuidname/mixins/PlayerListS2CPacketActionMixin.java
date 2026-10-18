package micheal65536.fountain.fabric.core.uuidname.mixins;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerListS2CPacket.Action.class)
public class PlayerListS2CPacketActionMixin {

    @ModifyConstant(method = "*", constant = @Constant(intValue = 16))
    private static int increaseMaxNameLength(int original) {
        return 36;
    }
}