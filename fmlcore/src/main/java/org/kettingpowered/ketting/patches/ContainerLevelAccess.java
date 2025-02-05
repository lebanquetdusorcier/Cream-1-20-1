package org.kettingpowered.ketting.patches;

import io.izzel.arclight.api.PluginPatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class redirects ContainerLevelAccess.create to craftCreate as mixins can overwrite create and mess up bukkit plugin compatibility
 */
@SuppressWarnings("unused")
public class ContainerLevelAccess implements PluginPatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger("ContainerLevelAccessPatcher");

    public void handleClass(ClassNode node, ClassRepo classRepo) {
        node.methods.forEach(m -> m.instructions.forEach(i -> {
            if (i instanceof MethodInsnNode min) {
                if (min.owner.equals("net/minecraft/world/inventory/ContainerLevelAccess") && min.name.equals("m_39289_") && min.desc.equals("(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/inventory/ContainerLevelAccess;")) {
                    LOGGER.debug("Patching ContainerLevelAccess.create call in {}:{}", node.name, m.name);
                    m.instructions.insertBefore(min, new MethodInsnNode(Opcodes.INVOKESTATIC, min.owner, "craftCreate", min.desc, true));
                    m.instructions.remove(min);
                }
            }
        }));
    }
}
