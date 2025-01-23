package org.kettingpowered.ketting.velocity;

import io.netty.buffer.Unpooled;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public final class AmbassadorSupport {

    //Copied from https://github.com/adde0109/Proxy-Compatible-Forge/blob/1.20.x/src/main/resources/integrated_argument_types.json
    private static final Set<String> AMBASSADOR_ENTRIES = Set.of(
            "brigadier:bool",
            "brigadier:float",
            "brigadier:double",
            "brigadier:integer",
            "brigadier:long",
            "brigadier:string",

            "minecraft:entity",
            "minecraft:game_profile",
            "minecraft:block_pos",
            "minecraft:column_pos",
            "minecraft:vec3",
            "minecraft:vec2",
            "minecraft:block_state",
            "minecraft:block_predicate",
            "minecraft:item_stack",
            "minecraft:item_predicate",
            "minecraft:color",
            "minecraft:component",
            "minecraft:message",
            "minecraft:nbt_compound_tag",
            "minecraft:nbt_tag",
            "minecraft:nbt_path",
            "minecraft:objective",
            "minecraft:objective_criteria",
            "minecraft:operation",
            "minecraft:particle",
            "minecraft:angle",
            "minecraft:rotation",
            "minecraft:scoreboard_slot",
            "minecraft:score_holder",
            "minecraft:swizzle",
            "minecraft:team",
            "minecraft:item_slot",
            "minecraft:resource_location",
            "minecraft:function",
            "minecraft:entity_anchor",
            "minecraft:int_range",
            "minecraft:float_range",
            "minecraft:dimension",
            "minecraft:gamemode",
            "minecraft:time",

            "minecraft:resource_or_tag",
            "minecraft:resource_or_tag_key",
            "minecraft:resource",
            "minecraft:resource_key",
            "minecraft:template_mirror",
            "minecraft:template_rotation",
            "minecraft:heightmap",
            "minecraft:uuid"
    );

    //Copied from https://github.com/adde0109/Proxy-Compatible-Forge/blob/1.20.x/src/main/java/org/adde0109/pcf/mixin/command/WrappableArgumentNodeStubMixin.java
    private static final int MOD_ARGUMENT_INDICATOR = -256;
    public static void writeStub(FriendlyByteBuf buffer, String stubId, ArgumentTypeInfo.Template<?> argumentType, ResourceLocation suggestionId) {
        buffer.writeUtf(stubId);

        var typeInfo = argumentType.type();
        var identifier = ForgeRegistries.COMMAND_ARGUMENT_TYPES.getKey(typeInfo);
        var id = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getId(typeInfo);

        if (identifier != null && AMBASSADOR_ENTRIES.contains(identifier.toString())) {
            buffer.writeVarInt(id);
            ((ArgumentTypeInfo) typeInfo).serializeToNetwork(argumentType, buffer);
        } else {
            buffer.writeVarInt(MOD_ARGUMENT_INDICATOR);
            buffer.writeVarInt(id);

            FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
            ((ArgumentTypeInfo) typeInfo).serializeToNetwork(argumentType, extraData);

            buffer.writeVarInt(extraData.readableBytes());
            buffer.writeBytes(extraData);

            extraData.release();
        }

        if (suggestionId != null)
            buffer.writeResourceLocation(suggestionId);
    }
}
