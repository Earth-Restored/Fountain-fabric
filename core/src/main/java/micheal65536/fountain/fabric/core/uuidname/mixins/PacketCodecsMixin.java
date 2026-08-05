package micheal65536.fountain.fabric.core.uuidname.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = "net.minecraft.network.codec.PacketCodecs$20")
public class PacketCodecsMixin {

    @ModifyConstant(
        method = "*",
        constant = @Constant(intValue = 16)
    )
    private static int increaseMaxNameLength(int original) {
        return 36;
    }
}
