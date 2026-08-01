package micheal65536.fountain.fabric.core.itembehavior;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record ItemParticlePayload(ItemStack stack, Vec3d pos) implements CustomPayload {
    public static final CustomPayload.Id<ItemParticlePayload> ID = 
        new CustomPayload.Id<>(Identifier.of("fountain", "item_particle"));

    public static final PacketCodec<RegistryByteBuf, ItemParticlePayload> CODEC = PacketCodec.tuple(
        ItemStack.PACKET_CODEC, ItemParticlePayload::stack,
        PacketCodec.ofStatic(
            (buf, vec) -> {
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            },
            buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
        ), ItemParticlePayload::pos,
        ItemParticlePayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}