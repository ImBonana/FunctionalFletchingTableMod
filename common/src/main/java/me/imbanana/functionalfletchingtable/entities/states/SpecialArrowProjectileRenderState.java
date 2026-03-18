package me.imbanana.functionalfletchingtable.entities.states;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;

public class SpecialArrowProjectileRenderState extends ArrowRenderState {
    public String tipItem;
    public String shaftItem;
    public String fletchingItem;
    public String effectItem;
    public int potionColor;
}
