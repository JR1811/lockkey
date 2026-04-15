package net.shirojr.init;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.shirojr.block.LockerBlock;

public interface LAKBlockStateProperties {
    EnumProperty<LockerBlock.Part> LOCKER_PART = EnumProperty.create("part", LockerBlock.Part.class);
    EnumProperty<LockerBlock.Type> LOCKER_TYPE = EnumProperty.create("type", LockerBlock.Type.class);

    static void initialize() {
        // static initialisation
    }
}
