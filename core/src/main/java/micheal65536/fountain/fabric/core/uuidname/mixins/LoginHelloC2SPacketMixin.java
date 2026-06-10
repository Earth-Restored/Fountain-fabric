package micheal65536.fountain.fabric.core.uuidname.mixins;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LoginHelloC2SPacket.class)
public class LoginHelloC2SPacketMixin {
    
    @ModifyConstant(
        method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", 
        constant = @Constant(intValue = 16)
    )
    private static int increaseMaxNameLengthOnRead(int original) {
        return 36;
    }

    @ModifyConstant(
        method = "write",
        constant = @Constant(intValue = 16)
    )
    private static int increaseMaxNameLengthOnWrite(int original) {
        return 36;
    }
}