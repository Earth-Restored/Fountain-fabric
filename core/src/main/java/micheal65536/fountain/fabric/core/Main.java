package micheal65536.fountain.fabric.core;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.core.earthmode.EarthModeCommand;
import micheal65536.fountain.fabric.core.earthmode.EarthModePacket;
import micheal65536.fountain.fabric.core.inventory.InventorySync;
import micheal65536.fountain.fabric.core.itembehavior.ItemParticlePayload;
import micheal65536.fountain.fabric.core.mobdespawn.MobDespawnGamerule;

public class Main implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain-core");

	@Override
	public void onInitializeServer() {
		MobDespawnGamerule.register();
		CommandRegistrationCallback.EVENT.register(EarthModeCommand.COMMAND_REGISTRATION_CALLBACK);
		EarthModePacket.register();
		InventorySync.register();
		PayloadTypeRegistry.playS2C().register(ItemParticlePayload.ID, ItemParticlePayload.CODEC);
	}
}