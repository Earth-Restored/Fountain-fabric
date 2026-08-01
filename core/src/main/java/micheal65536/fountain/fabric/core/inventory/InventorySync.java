package micheal65536.fountain.fabric.core.inventory;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import micheal65536.fountain.fabric.core.Main;
import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// provides network packets to flush the player's inventory to the bridge and to set the player's hotbar from the bridge
// TODO: it might be nicer to use a push system rather than a polling system for inventory sync, but that requires hooking all the places where items are added to the player's inventory
public class InventorySync {
	public record InventorySyncRequestPayload(boolean clearHotbar) implements CustomPayload {
		public static final CustomPayload.Id<InventorySyncRequestPayload> ID = new CustomPayload.Id<>(
				new Identifier("fountain", "inventory_sync_request"));

		public static final PacketCodec<RegistryByteBuf, InventorySyncRequestPayload> CODEC = PacketCodec.tuple(
				PacketCodecs.BOOL, InventorySyncRequestPayload::clearHotbar,
				InventorySyncRequestPayload::new);

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record InventorySyncResponsePayload(List<ItemStack> itemStacks, List<ItemStack> hotbarStacks)
			implements CustomPayload {
		public static final CustomPayload.Id<InventorySyncResponsePayload> ID = new CustomPayload.Id<>(
				new Identifier("fountain", "inventory_sync_response"));

		public static final PacketCodec<RegistryByteBuf, InventorySyncResponsePayload> CODEC = PacketCodec.tuple(
				ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), InventorySyncResponsePayload::itemStacks,
				ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), InventorySyncResponsePayload::hotbarStacks,
				InventorySyncResponsePayload::new);

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record SetHotbarRequestPayload(boolean initial, List<ItemStack> hotbarStacks) implements CustomPayload {
		public static final CustomPayload.Id<SetHotbarRequestPayload> ID = new CustomPayload.Id<>(
				new Identifier("fountain", "set_hotbar_request"));

		public static final PacketCodec<RegistryByteBuf, SetHotbarRequestPayload> CODEC = PacketCodec.tuple(
				PacketCodecs.BOOL, SetHotbarRequestPayload::initial,
				ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), SetHotbarRequestPayload::hotbarStacks,
				SetHotbarRequestPayload::new);

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public record SetHotbarResponsePayload(boolean success) implements CustomPayload {
		public static final CustomPayload.Id<SetHotbarResponsePayload> ID = new CustomPayload.Id<>(
				new Identifier("fountain", "set_hotbar_response"));

		public static final PacketCodec<RegistryByteBuf, SetHotbarResponsePayload> CODEC = PacketCodec.tuple(
				PacketCodecs.BOOL, SetHotbarResponsePayload::success,
				SetHotbarResponsePayload::new);

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public static void register() {
		PayloadTypeRegistry.playC2S().register(InventorySyncRequestPayload.ID, InventorySyncRequestPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(InventorySyncResponsePayload.ID, InventorySyncResponsePayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetHotbarRequestPayload.ID, SetHotbarRequestPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SetHotbarResponsePayload.ID, SetHotbarResponsePayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(InventorySyncRequestPayload.ID,
				InventorySync::handleInventorySyncRequest);
		ServerPlayNetworking.registerGlobalReceiver(SetHotbarRequestPayload.ID, InventorySync::handleSetHotbarRequest);
	}

	private static void handleInventorySyncRequest(InventorySyncRequestPayload payload,
			ServerPlayNetworking.Context context) {
		var player = context.player();

		if (!((EarthModePlayer) player).isEarthMode()) {
			Main.LOGGER.warn("Inventory sync request for player not in Earth mode");
			return;
		}

		PlayerInventory playerInventory = player.getInventory();

		List<ItemStack> itemStacks = new LinkedList<>();
		for (int slotIndex = 7; slotIndex < 36; slotIndex++) {
			ItemStack itemStack = playerInventory.getStack(slotIndex);
			if (!itemStack.isEmpty()) {
				itemStacks.add(itemStack);
				playerInventory.setStack(slotIndex, ItemStack.EMPTY);
			}
		}

		List<ItemStack> hotbarStacks = new ArrayList<>();
		for (int slotIndex = 0; slotIndex < 7; slotIndex++) {
			ItemStack itemStack = playerInventory.getStack(slotIndex);
			hotbarStacks.add(itemStack);
			if (payload.clearHotbar()) {
				playerInventory.setStack(slotIndex, ItemStack.EMPTY);
			}
		}

		ServerPlayNetworking.send(player, new InventorySyncResponsePayload(itemStacks, hotbarStacks));
	}

	private static void handleSetHotbarRequest(SetHotbarRequestPayload payload, ServerPlayNetworking.Context context) {
		var player = context.player();

		if (!((EarthModePlayer) player).isEarthMode()) {
			Main.LOGGER.warn("Set hotbar request for player not in Earth mode");
			return;
		}

		PlayerInventory playerInventory = player.getInventory();

		if (payload.initial()) {
			for (int slotIndex = 0; slotIndex < 36; slotIndex++) {
				playerInventory.setStack(slotIndex, ItemStack.EMPTY);
			}

			for (int slotIndex = 0; slotIndex < Math.min(7, payload.hotbarStacks().size()); slotIndex++) {
				playerInventory.setStack(slotIndex, payload.hotbarStacks().get(slotIndex));
			}
		} else {
			boolean isEmpty = true;
			for (int slotIndex = 0; slotIndex < 7; slotIndex++) {
				if (!playerInventory.getStack(slotIndex).isEmpty()) {
					Main.LOGGER.debug("Rejecting set hotbar request because hotbar is not empty");
					isEmpty = false;
					break;
				}
			}

			if (isEmpty) {
				for (int slotIndex = 0; slotIndex < Math.min(7, payload.hotbarStacks().size()); slotIndex++) {
					playerInventory.setStack(slotIndex, payload.hotbarStacks().get(slotIndex));
				}
			}

			ServerPlayNetworking.send(player, new SetHotbarResponsePayload(isEmpty));
		}
	}
}