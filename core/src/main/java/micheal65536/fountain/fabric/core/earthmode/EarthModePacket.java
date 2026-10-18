package micheal65536.fountain.fabric.core.earthmode;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import micheal65536.fountain.fabric.core.Main;
import micheal65536.fountain.fabric.core.itembehavior.EarthBlockBreakingServerPlayerInteractionManager;

public class EarthModePacket {
	public record EarthModePayload(boolean earthMode) implements CustomPayload {
		public static final CustomPayload.Id<EarthModePayload> ID = new CustomPayload.Id<>(Identifier.of("fountain", "earth_mode"));

		public static final PacketCodec<RegistryByteBuf, EarthModePayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, EarthModePayload::earthMode, EarthModePayload::new);

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public static void register() {
		PayloadTypeRegistry.playC2S().register(EarthModePayload.ID, EarthModePayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(EarthModePayload.ID, EarthModePacket::handle);
	}

	private static void handle(EarthModePayload payload, ServerPlayNetworking.Context context) {
		var player = context.player();
		boolean earthMode = payload.earthMode();

		((EarthModePlayer) player).setEarthMode(earthMode);
		((EarthBlockBreakingServerPlayerInteractionManager) player.interactionManager).resetBlockBreaking();
		((EarthModePlayer) player).applyAbilitiesForEarthMode();
		player.sendAbilitiesUpdate();

		if (earthMode) {
			Main.LOGGER.info("Enabled Earth mode for %s (requested by channel message)".formatted(player.getName().getString()));
		} else {
			Main.LOGGER.info("Disabled Earth mode for %s (requested by channel message)".formatted(player.getName().getString()));
		}
	}
}