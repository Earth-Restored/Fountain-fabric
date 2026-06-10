package micheal65536.fountain.fabric.core.uuidname.mixins;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Pattern;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    private static final Pattern GUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    @Inject(method = "isUsernameValid", at = @At("HEAD"), cancellable = true)
    private static void allowGuidUsernames(String name, CallbackInfoReturnable<Boolean> cir) {
        if (name != null && GUID_PATTERN.matcher(name).matches()) {
            cir.setReturnValue(true);
        }
    }
}