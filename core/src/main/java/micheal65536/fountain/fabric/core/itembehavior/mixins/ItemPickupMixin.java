package micheal65536.fountain.fabric.core.itembehavior.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import micheal65536.fountain.fabric.core.earthmode.EarthModePlayer;

// allows Earth players to pick up item entities by tapping on them
@Mixin(ItemEntity.class)
public abstract class ItemPickupMixin extends Entity {
	// this is the absolute f*cking ugliest cr*p in the entire mixin API and I hate it
	// dear Fabric developers: please just make a f*cking annotation like @Overwrite that allows overriding methods from the parent class while also supporting obfuscation/remapping instead of this completely braindead "extend the super class of the target class and then fudge around with the constructor" cr*p
	private ItemPickupMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (((EarthModePlayer) player).isEarthMode()) {
			((ItemEntity) (Object) this).onPlayerCollision(player);
			return ActionResult.CONSUME;
		} else {
			return ActionResult.PASS;
		}
	}
}