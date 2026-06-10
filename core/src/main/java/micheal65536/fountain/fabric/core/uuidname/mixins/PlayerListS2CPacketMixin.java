package micheal65536.fountain.fabric.core.uuidname.mixins;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerListS2CPacket.Action.class)
public class PlayerListS2CPacketMixin {

    @ModifyConstant(
        method = "method_46341(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Entry;)V",
        constant = @Constant(intValue = 16)
    )
    private static int increaseMaxNameLengthOnWrite(int original) {
        return 36;
    }

    @ModifyConstant(
        method = "method_46342(Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Serialized;Lnet/minecraft/network/PacketByteBuf;)V",
        constant = @Constant(intValue = 16)
    )
    private static int increaseMaxNameLengthOnRead(int original) {
        return 36;
    }
}
