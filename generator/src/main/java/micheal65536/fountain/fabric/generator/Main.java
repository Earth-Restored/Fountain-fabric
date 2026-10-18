package micheal65536.fountain.fabric.generator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.OperatorOnlyBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import micheal65536.fountain.fabric.generator.blocks.EarthConstraintBlock;
import micheal65536.fountain.fabric.generator.blocks.NonReplaceableAirBlock;
import micheal65536.fountain.fabric.generator.blocks.SolidAirBlock;
import micheal65536.fountain.fabric.generator.terraingen.EmptyChunkGenerator;
import micheal65536.fountain.fabric.generator.terraingen.WrapperChunkGenerator;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("fountain-generator");

    @Override
    public void onInitialize() {
        // blocks

        Block invisibleConstraintBlock = new EarthConstraintBlock();
        BlockItem invisibleConstraintBlockItem = new OperatorOnlyBlockItem(invisibleConstraintBlock, new Item.Settings());
        Block blendConstraintBlock = new EarthConstraintBlock();
        BlockItem blendConstraintBlockItem = new OperatorOnlyBlockItem(blendConstraintBlock, new Item.Settings());
        Block borderConstraintBlock = new EarthConstraintBlock();
        BlockItem borderConstraintBlockItem = new OperatorOnlyBlockItem(borderConstraintBlock, new Item.Settings());
        Block solidAirBlock = new SolidAirBlock();
        BlockItem solidAirBlockItem = new OperatorOnlyBlockItem(solidAirBlock, new Item.Settings());
        Block nonReplaceableAirBlock = new NonReplaceableAirBlock();
        BlockItem nonReplaceableAirBlockItem = new OperatorOnlyBlockItem(nonReplaceableAirBlock, new Item.Settings());

        Registry.register(Registries.BLOCK, Identifier.of("fountain", "invisible_constraint"), invisibleConstraintBlock);
        Registry.register(Registries.ITEM, Identifier.of("fountain", "invisible_constraint"), invisibleConstraintBlockItem);
        Registry.register(Registries.BLOCK, Identifier.of("fountain", "blend_constraint"), blendConstraintBlock);
        Registry.register(Registries.ITEM, Identifier.of("fountain", "blend_constraint"), blendConstraintBlockItem);
        Registry.register(Registries.BLOCK, Identifier.of("fountain", "border_constraint"), borderConstraintBlock);
        Registry.register(Registries.ITEM, Identifier.of("fountain", "border_constraint"), borderConstraintBlockItem);
        Registry.register(Registries.BLOCK, Identifier.of("fountain", "solid_air"), solidAirBlock);
        Registry.register(Registries.ITEM, Identifier.of("fountain", "solid_air"), solidAirBlockItem);
        Registry.register(Registries.BLOCK, Identifier.of("fountain", "non_replaceable_air"), nonReplaceableAirBlock);
        Registry.register(Registries.ITEM, Identifier.of("fountain", "non_replaceable_air"), nonReplaceableAirBlockItem);

        Registry.register(Registries.ITEM_GROUP, Identifier.of("fountain", "fountain"), FabricItemGroup.builder().displayName(Text.literal("fountain-generator")).icon(() -> new ItemStack(invisibleConstraintBlockItem)).entries((context, entries) -> {
            entries.add(invisibleConstraintBlockItem);
            entries.add(blendConstraintBlockItem);
            entries.add(borderConstraintBlockItem);
            entries.add(solidAirBlockItem);
            entries.add(nonReplaceableAirBlockItem);
        }).build());

        // chunk generators

        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of("fountain", "empty"), EmptyChunkGenerator.CODEC);
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of("fountain", "wrapper"), WrapperChunkGenerator.CODEC);
    }
}