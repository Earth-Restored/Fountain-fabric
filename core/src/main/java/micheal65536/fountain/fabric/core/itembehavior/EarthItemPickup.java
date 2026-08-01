package micheal65536.fountain.fabric.core.itembehavior;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

public class EarthItemPickup {

	public static void giveItemToEarthPlayer(ServerPlayerEntity player, ItemStack itemStack, Vec3d pos, boolean showItemParticle) {
		if (!((EarthModePlayer) player).isEarthMode()) {
			throw new IllegalArgumentException();
		}
		if (itemStack == null || itemStack.isEmpty()) {
			return;
		}

		ItemStack itemStack1 = itemStack.copy();
		if (!player.getInventory().insertStack(itemStack)) {
			// TODO: player inventory is full, that shouldn't happen if the bridge is properly transferring items to the Genoa inventory
			return;
		}

		if (showItemParticle) {
			// TODO: send item particles to all Earth players
			ServerPlayNetworking.send(player, new ItemParticlePayload(itemStack1, pos));
		}
	}
}