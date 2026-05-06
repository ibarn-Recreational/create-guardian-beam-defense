package com.ibarnstormer.gbd.datagen;

import com.google.common.base.Suppliers;
import com.ibarnstormer.gbd.Main;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Main.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> APPEND_MODIFIER = LOOT_MODIFIERS.register("append_item", AppendItemModifier.CODEC);

    public static void register(IEventBus modEventBus) {
        LOOT_MODIFIERS.register(modEventBus);
    }


    public static class AppendItemModifier extends LootModifier {

        private final ItemStack appendedStack;

        public static final Supplier<MapCodec<AppendItemModifier>> CODEC = Suppliers.memoize(() ->
                RecordCodecBuilder.mapCodec(i -> codecStart(i).and(
                ItemStack.CODEC.fieldOf("item").forGetter(m -> m.appendedStack)
                ).apply(i, AppendItemModifier::new)));


        protected AppendItemModifier(LootItemCondition[] conditionsIn, ItemStack stack) {
            super(conditionsIn);
            appendedStack = stack;
        }

        @Override
        protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            ItemStack stack = appendedStack.copy();
            if(stack.getCount() <= 0) stack.setCount(1);
            generatedLoot.add(stack);
            return generatedLoot;
        }

        @Override
        public MapCodec<? extends IGlobalLootModifier> codec() {
            return CODEC.get();
        }
    }
}
