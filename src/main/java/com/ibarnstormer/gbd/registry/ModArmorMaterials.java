package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.Main;
import com.simibubi.create.AllItems;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Main.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BeamReactorMaterial = ARMOR_MATERIALS.register("beam_reactor", () -> {
        EnumMap<ArmorItem.Type, Integer> map = Util.make(new EnumMap<>(ArmorItem.Type.class), e -> {
            e.put(ArmorItem.Type.BOOTS, 5);
            e.put(ArmorItem.Type.LEGGINGS, 8);
            e.put(ArmorItem.Type.CHESTPLATE, 10);
            e.put(ArmorItem.Type.HELMET, 5);
            e.put(ArmorItem.Type.BODY, 11);
        });
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Main.MODID, "beam_reactor")));

        return new ArmorMaterial(map, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(AllItems.STURDY_SHEET), layers, 4.0f, 0.15f);
    });

    public static void register(IEventBus modbus) {
        ARMOR_MATERIALS.register(modbus);
    }

}
