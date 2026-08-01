package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class WoolyCowEntity extends CowEntity implements Shearable {
	// TODO: needs "prep" and "shake" behavior (I don't know what these are)

	private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(WoolyCowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private final DyeColor woolColor;

	public WoolyCowEntity(EntityType<? extends WoolyCowEntity> entityType, World world, @NotNull DyeColor woolColor) {
		super(entityType, world);
		this.woolColor = woolColor;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(SHEARED, false);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		PrioritizedGoal[] goals = this.goalSelector.getGoals().toArray(PrioritizedGoal[]::new);
		this.goalSelector.clear(goal -> true);
		for (PrioritizedGoal goal : goals) {
			if (goal.getPriority() < 5) {
				this.goalSelector.add(goal.getPriority(), goal.getGoal());
			} else {
				this.goalSelector.add(goal.getPriority() + 1, goal.getGoal());
			}
		}
		this.goalSelector.add(5, new EatGrassGoal(this));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Sheared", this.isSheared());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setSheared(nbt.getBoolean("Sheared"));
	}

	@Override
	public RegistryKey<LootTable> getLootTableId() {
		return !this.isSheared() ? RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("fountain", "entities/cow/wooly_cow_" + this.woolColor.getName().toLowerCase(Locale.ROOT))) : RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "entities/cow"));
	}

	@Override
	public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
		ItemStack itemStack = playerEntity.getStackInHand(hand);
		if (itemStack.isOf(Items.SHEARS) && this.isShearable()) {
			if (!this.getWorld().isClient) {
				this.sheared(SoundCategory.PLAYERS);
				this.emitGameEvent(GameEvent.SHEAR, playerEntity);
				EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
				itemStack.damage(1, playerEntity, slot);
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.CONSUME;
			}
		}
		return super.interactMob(playerEntity, hand);
	}

	public void setSheared(boolean sheared) {
		this.dataTracker.set(SHEARED, sheared);
	}

	public boolean isSheared() {
		return this.dataTracker.get(SHEARED);
	}

	@Override
	public void sheared(SoundCategory shearedSoundCategory) {
		this.getWorld().playSoundFromEntity(null, this, SoundEvent.of(Identifier.of("fountain", "entity.wooly_cow.shear")), shearedSoundCategory, 1.0f, 1.0f);

		this.setSheared(true);

		Item woolItem = Registries.ITEM.get(Identifier.of("minecraft", this.woolColor.getName().toLowerCase(Locale.ROOT) + "_wool"));
		int count = this.random.nextInt(3) + 1;
		for (int i = 0; i < count; i++) {
			ItemEntity itemEntity = this.dropItem(woolItem, 1);
			if (itemEntity != null) {
				itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
			}
		}
	}

	@Override
	public boolean isShearable() {
		return this.isAlive() && !this.isBaby() && !this.isSheared();
	}

	@Override
	public void onEatingGrass() {
		super.onEatingGrass();
		this.setSheared(false);
	}
}