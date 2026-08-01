package micheal65536.fountain.fabric.content;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.content.blocks.RainbowCarpetBlock;
import micheal65536.fountain.fabric.content.blocks.RainbowWoolBlock;
import micheal65536.fountain.fabric.content.entities.ChickenVariantEntity;
import micheal65536.fountain.fabric.content.entities.GenoaSlimeEntity;
import micheal65536.fountain.fabric.content.entities.RainbowSheepEntity;
import micheal65536.fountain.fabric.content.entities.SheepVariantEntity;
import micheal65536.fountain.fabric.content.entities.TropicalSlimeEntity;
import micheal65536.fountain.fabric.content.entities.WoolyCowEntity;
import micheal65536.fountain.fabric.content.entities.mixins.EntityTypeAccessor;

public class Main implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fountain-content");

	@Override
	public void onInitialize() {
		// blocks/items

		Block rainbowCarpetBlock = new RainbowCarpetBlock(AbstractBlock.Settings.copy(Registries.BLOCK.get(new Identifier("white_carpet"))).mapColor(DyeColor.RED));
		BlockItem rainbowCarpetBlockItem = new BlockItem(rainbowCarpetBlock, new Item.Settings());
		Registry.register(Registries.BLOCK, new Identifier("fountain", "rainbow_carpet"), rainbowCarpetBlock);
		Registry.register(Registries.ITEM, new Identifier("fountain", "rainbow_carpet"), rainbowCarpetBlockItem);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> entries.add(rainbowCarpetBlockItem));

		Block rainbowWoolBlock = new RainbowWoolBlock(AbstractBlock.Settings.copy(Registries.BLOCK.get(new Identifier("white_wool"))).mapColor(DyeColor.RED));
		BlockItem rainbowWoolBlockItem = new BlockItem(rainbowWoolBlock, new Item.Settings());
		Registry.register(Registries.BLOCK, new Identifier("fountain", "rainbow_wool"), rainbowWoolBlock);
		Registry.register(Registries.ITEM, new Identifier("fountain", "rainbow_wool"), rainbowWoolBlockItem);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> entries.add(rainbowWoolBlockItem));

		// entities

		this.registerEarthMobVariant("minecraft:chicken", "genoa:amber_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:bronzed_chicken", ChickenVariantEntity::new);
		// TODO: cluck_shroom
		// TODO: fancy_chicken
		this.registerEarthMobVariant("minecraft:chicken", "genoa:gold_crested_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:midnight_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:skewbald_chicken", ChickenVariantEntity::new);
		this.registerEarthMobVariant("minecraft:chicken", "genoa:stormy_chicken", ChickenVariantEntity::new);

		this.registerEarthMobVariant("minecraft:cow", "genoa:albino_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:ashen_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:cookie_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:cream_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:dairy_cow");
		// TODO: moo_bloom
		// TODO: moolip
		this.registerEarthMobVariant("minecraft:cow", "genoa:pinto_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:sunset_cow");
		this.registerEarthMobVariant("minecraft:cow", "genoa:umbra_cow", (EntityType.EntityFactory<WoolyCowEntity>) (entityType, world) -> new WoolyCowEntity(entityType, world, DyeColor.BLACK));
		this.registerEarthMobVariant("minecraft:cow", "genoa:wooly_cow", (EntityType.EntityFactory<WoolyCowEntity>) (entityType, world) -> new WoolyCowEntity(entityType, world, DyeColor.BROWN));

		// TODO: furnace_golem

		// TODO: jolly_llama

		this.registerEarthMobVariant("minecraft:pig", "genoa:mottled_pig");
		// TODO: mud_pig
		this.registerEarthMobVariant("minecraft:pig", "genoa:pale_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:piebald_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:pink_footed_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:sooty_pig");
		this.registerEarthMobVariant("minecraft:pig", "genoa:spotted_pig");

		this.registerEarthMobVariant("minecraft:rabbit", "genoa:bold_striped_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:freckled_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:harelequin_rabbit");
		// TODO: jumbo_rabbit
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:muddy_foot_rabbit");
		this.registerEarthMobVariant("minecraft:rabbit", "genoa:vested_rabbit");

		this.registerEarthMobVariant("minecraft:sheep", "genoa:flecked_sheep", SheepVariantEntity.createFactory(true, DyeColor.BROWN));
		// TODO: fuzzy_sheep
		// TODO: horned_sheep
		this.registerEarthMobVariant("minecraft:sheep", "genoa:inky_sheep", SheepVariantEntity.createFactory(true, DyeColor.BLACK));
		this.registerEarthMobVariant("minecraft:sheep", "genoa:long_nosed_sheep", SheepVariantEntity.createFactory(true, DyeColor.BROWN));
		this.registerEarthMobVariant("minecraft:sheep", "genoa:patched_sheep", SheepVariantEntity.createFactory(true, DyeColor.WHITE));
		this.registerEarthMobVariant("minecraft:sheep", "genoa:rainbow_sheep", RainbowSheepEntity::new);
		this.registerEarthMobVariant("minecraft:sheep", "genoa:rocky_sheep", SheepVariantEntity.createFactory(true, DyeColor.GRAY));

		this.registerEarthMobVariant("minecraft:slime", "genoa:genoa_slime", (EntityType.EntityFactory<GenoaSlimeEntity>) (entityType, world) -> new GenoaSlimeEntity(entityType, world, 4));
		this.registerEarthMobVariant("minecraft:slime", "genoa:genoa_slime_half", (EntityType.EntityFactory<GenoaSlimeEntity>) (entityType, world) -> new GenoaSlimeEntity(entityType, world, 2));
		this.registerEarthMobVariant("minecraft:slime", "genoa:genoa_slime_quarter", (EntityType.EntityFactory<GenoaSlimeEntity>) (entityType, world) -> new GenoaSlimeEntity(entityType, world, 1));
		this.registerEarthMobVariant("minecraft:slime", "genoa:tropical_slime", TropicalSlimeEntity::new);

		// TODO: melon_golem

		// TODO
	}

	private <T extends MobEntity> void registerEarthMobVariant(@NotNull String baseType, @NotNull String variantType) {
		this.registerEarthMobVariant(baseType, variantType, null);
	}

	@SuppressWarnings("unchecked")
	private <T extends U, U extends MobEntity> void registerEarthMobVariant(@NotNull String baseType, @NotNull String variantType, @Nullable EntityType.EntityFactory<T> factory) {
		Identifier baseId = Identifier.tryParse(baseType);
		Identifier variantId = Identifier.tryParse(variantType);

		if (baseId == null || variantId == null) {
			throw new IllegalArgumentException("Invalid identifier: " + baseType + " or " + variantType);
		}

		EntityType<U> entityType = (EntityType<U>) Registries.ENTITY_TYPE.get(baseId);

		EntityType.EntityFactory<T> entityFactory = (factory != null) ? factory : ((EntityTypeAccessor<T>) entityType).getFactory();

		EntityType.Builder<T> builder = EntityType.Builder.create(entityFactory, entityType.getSpawnGroup()).dimensions(entityType.getDimensions().width(), entityType.getDimensions().height()).maxTrackingRange(entityType.getMaxTrackDistance()).trackingTickInterval(entityType.getTrackTickInterval());

		if (entityType.isFireImmune())
			builder.makeFireImmune();
		if (!entityType.isSaveable())
			builder.disableSaving();
		if (!entityType.isSummonable())
			builder.disableSummon();
		if (entityType.isSpawnableFarFromPlayer())
			builder.spawnableFarFromPlayer();

		EntityType<T> newEntityType = builder.build(variantType);

		((EntityTypeAccessor<?>) newEntityType).setLootTableId(entityType.getLootTableId());

		Registry.register(Registries.ENTITY_TYPE, variantId, newEntityType);

		if (DefaultAttributeRegistry.hasDefinitionFor(entityType)) {
			FabricDefaultAttributeRegistry.register(newEntityType, DefaultAttributeRegistry.get(entityType));
		}

		Identifier baseEggId = Identifier.tryParse(baseType + "_spawn_egg");
		Identifier variantEggId = Identifier.tryParse(variantType + "_spawn_egg");

		SpawnEggItem spawnEggItem = (SpawnEggItem) Registries.ITEM.get(baseEggId);
		SpawnEggItem newSpawnEggItem = new SpawnEggItem(newEntityType, spawnEggItem.getColor(0), spawnEggItem.getColor(1), new Item.Settings());
		Registry.register(Registries.ITEM, variantEggId, newSpawnEggItem);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(newSpawnEggItem));
	}
}