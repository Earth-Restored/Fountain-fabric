package micheal65536.fountain.fabric.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RainbowSheepEntity extends SheepVariantEntity
{
	public RainbowSheepEntity(EntityType<? extends SheepEntity> entityType, World world)
	{
		super(entityType, world, true, DyeColor.WHITE);
	}

	@Override
	public RegistryKey<LootTable> getLootTableId()
	{
		if (this.isSheared())
		{
			return super.getLootTableId();
		}
		else
		{
			return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("fountain", "entities/sheep/rainbow"));
		}
	}

	@Override
	public void sheared(SoundCategory shearedSoundCategory)
	{
		this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);

		this.setSheared(true);

		Item rainbowWool = Registries.ITEM.get(new Identifier("fountain", "rainbow_wool"));
		int count = this.random.nextInt(3) + 1;
		for (int i = 0; i < count; i++)
		{
			ItemEntity itemEntity = this.dropItem(rainbowWool, 1);
			if (itemEntity != null)
			{
				itemEntity.setVelocity(itemEntity.getVelocity().add(
						(this.random.nextFloat() - this.random.nextFloat()) * 0.1f,
						this.random.nextFloat() * 0.05f,
						(this.random.nextFloat() - this.random.nextFloat()) * 0.1f
				));
			}
		}
	}
}